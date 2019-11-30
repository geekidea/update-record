/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.geekidea.updaterecord.core.support;

import io.geekidea.updaterecord.annotation.UpdateRecordColumn;
import io.geekidea.updaterecord.annotation.UpdateRecordIgnore;
import io.geekidea.updaterecord.api.entity.UpdateRecordTableLog;
import io.geekidea.updaterecord.api.interceptor.UpdateRecordInterceptor;
import io.geekidea.updaterecord.api.vo.TableVo;
import io.geekidea.updaterecord.core.cache.UpdateRecordThreadLocalCache;
import io.geekidea.updaterecord.core.compare.UpdateRecordComparator;
import io.geekidea.updaterecord.core.config.UpdateRecordConfigProperties;
import io.geekidea.updaterecord.core.constant.UpdateRecordConstant;
import io.geekidea.updaterecord.core.exception.UpdateRecordException;
import io.geekidea.updaterecord.core.util.PluginUtil;
import io.geekidea.updaterecord.core.vo.UpdateRecordItemVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 修改记录mybatis拦截器支持类
 * 抽象类，需子类继承重写
 *
 * @author geekidea
 * @date 2019-11-16
 * @see UpdateRecordMybatisInterceptor 原生mybatis拦截器实现类
 * @see UpdateRecordMybatisPlusInterceptor mybatis-plus拦截器实现类
 **/
@Slf4j
public abstract class UpdateRecordInterceptorSupport implements UpdateRecordInterceptor {

    private static final Map<Class<?>, TableVo> TABLE_VO_CACHE_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Set<String>> MAPPER_METHOD_IGNORE_CACHE_MAP = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            // 获取代理对象
            StatementHandler statementHandler = PluginUtil.getTarget(invocation.getTarget());
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
            // 只拦截update方法
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(UpdateRecordConstant.DELEGATE_MAPPEDSTATEMENT);
            if (SqlCommandType.UPDATE != mappedStatement.getSqlCommandType()) {
                return invocation.proceed();
            }

            // 是否执行,当前线程不存在值，则跳过，当前类/方如果存在@UpdateRecordIgnore，则跳过
            boolean empty = UpdateRecordThreadLocalCache.empty();
            if (empty) {
                return invocation.proceed();
            }

            // 获取执行方法id
            String methodId = mappedStatement.getId();
            int lastIndex = methodId.lastIndexOf(".");
            String mapperClassName = methodId.substring(0, lastIndex);
            String mapperMethodName = methodId.substring(lastIndex + 1);
            Class<?> mapperClass = Class.forName(mapperClassName);

            // 如果类上有忽略注解，则跳过
            UpdateRecordIgnore classUpdateRecordIgnore = mapperClass.getAnnotation(UpdateRecordIgnore.class);
            if (classUpdateRecordIgnore != null) {
                return invocation.proceed();
            }

            // 如果mapper方法上有忽略注解，则跳过
            boolean isIgnoreMethod = isIgnoreMapperMethod(mapperClass, mapperMethodName);
            if (isIgnoreMethod) {
                return invocation.proceed();
            }

            // 获取BoundSql对象
            BoundSql boundSql = (BoundSql) metaObject.getValue(UpdateRecordConstant.DELEGATE_BOUNDSQL);

            // 获取实体参数对象
            Object paramObject = getParameterObject(boundSql.getParameterObject());
            if (paramObject instanceof Map) {
                log.warn("Map parameter is not supported");
                return invocation.proceed();
            }

            // 获取真实修改的字段列表
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            if (parameterMappings == null) {
                log.warn("Not Found update column");
                return invocation.proceed();
            }

            // 获取实体参数对象类型
            Class<?> cls = paramObject.getClass();

            // 获取实体对应的表和列名信息
            TableVo tableVo = getCacheTableVo(cls);
            if (!tableVo.isRecord()) {
                return invocation.proceed();
            }

            Object idValueObject = getFieldValue(cls, paramObject, tableVo.getIdPropertyName());
            if (idValueObject == null) {
                log.warn("Update Method Primary key cannot be empty");
                return invocation.proceed();
            }

            // 处理修改记录
            handle(invocation, methodId, paramObject, parameterMappings, cls, tableVo, idValueObject);
        } catch (Exception e) {
            log.error("修改记录异常", e);
        }
        return invocation.proceed();
    }

    /**
     * 处理修改记录
     *
     * @param invocation
     * @param methodId
     * @param paramObject
     * @param parameterMappings
     * @param cls
     * @param tableVo
     * @param idValueObject
     * @throws Exception
     */
    private void handle(Invocation invocation, String methodId, Object paramObject, List<ParameterMapping> parameterMappings, Class<?> cls, TableVo tableVo, Object idValueObject) throws Exception {
        // 修改项
        UpdateRecordTableLog updateRecordTableLog = new UpdateRecordTableLog();
        // 设置当前会话唯一标识
        updateRecordTableLog.setCommitId(UpdateRecordThreadLocalCache.getCommitId());
        // 方法ID
        updateRecordTableLog.setMethodId(methodId);
        // 创建时间
        updateRecordTableLog.setCreateTime(new Date());
        // 实体名称
        updateRecordTableLog.setEntityName(cls.getSimpleName());
        // 表名称
        updateRecordTableLog.setTableName(tableVo.getTableName());
        // 表描述
        updateRecordTableLog.setTableDesc(tableVo.getTableDesc());
        // ID列名/字段名称/ID值
        String idValue = idValueObject.toString();
        updateRecordTableLog.setIdColumnName(tableVo.getIdColumnName());
        updateRecordTableLog.setIdPropertyName(tableVo.getIdPropertyName());
        updateRecordTableLog.setIdValue(idValue);

        // 修改之后的参数map
        Map<String, Object> afterMap = getUpdateAfterMap(cls, paramObject, parameterMappings, tableVo);
        // 修改列字符串
        Set<String> propertySet = afterMap.keySet();
        String columnString = propertySet.toString();
        String updateColumnString = columnString.substring(1, columnString.length() - 1);

        // 获取参数对象
        Object[] args = invocation.getArgs();
        // 获取连接对象
        Connection connection = (Connection) args[0];
        // 组装根据ID查询的SQL语句
        String selectSql = String.format(UpdateRecordConstant.SELECT_SQL, updateColumnString, tableVo.getTableName(), tableVo.getIdColumnName());
        // 获取修改之前的map
        Map<String, Object> beforeMap = getBeforeMapById(connection, selectSql, idValue);

        // 获取修改前后版本值
        Object beforeVersionValueObject = getVersionValue(beforeMap);
        Object afterVersionValueObject = getAfterVersionValue(beforeVersionValueObject, afterMap);
        String beforeVersionValue = beforeVersionValueObject == null ? null : beforeVersionValueObject.toString();
        String afterVersionValue = afterVersionValueObject == null ? null : afterVersionValueObject.toString();

        // 比较修改前后map，核心方法
        UpdateRecordItemVo updateRecordItemVo = UpdateRecordComparator.compare(beforeMap, afterMap, beforeVersionValue, afterVersionValue, updateRecordTableLog, tableVo.getColumnMap());
        // 添加到当前线程变量的集合中，等待事务成功后，统一保存
        UpdateRecordThreadLocalCache.add(updateRecordItemVo);
    }

    /**
     * 判断是否忽律该方法
     *
     * @param mapperClass
     * @param methodId
     * @return
     */
    private boolean isIgnoreMapperMethod(Class<?> mapperClass, String mapperMethodName) {
        if (MAPPER_METHOD_IGNORE_CACHE_MAP.containsKey(mapperClass)) {
            Set<String> set = MAPPER_METHOD_IGNORE_CACHE_MAP.get(mapperClass);
            if (set != null && set.contains(mapperMethodName)) {
                return true;
            } else {
                return false;
            }
        }

        // 获取所有方法，判断是否有忽律注解
        Method[] methods = mapperClass.getDeclaredMethods();
        Set<String> ignoreMethodNames = new HashSet<>();
        for (Method method : methods) {
            UpdateRecordIgnore updateRecordIgnore = method.getAnnotation(UpdateRecordIgnore.class);
            if (updateRecordIgnore != null) {
                ignoreMethodNames.add(method.getName());
            }
        }
        MAPPER_METHOD_IGNORE_CACHE_MAP.put(mapperClass, ignoreMethodNames);
        if (ignoreMethodNames.contains(mapperMethodName)) {
            return true;
        } else {
            return false;
        }
    }

    private TableVo getCacheTableVo(Class<?> cls) {
        // 如果缓存中存在，则返回
        if (TABLE_VO_CACHE_MAP.containsKey(cls)) {
            return TABLE_VO_CACHE_MAP.get(cls);
        }
        TableVo tableVo = getTableVo(cls);
        TABLE_VO_CACHE_MAP.put(cls, tableVo);
        return tableVo;
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 获取实体对象属性值
     *
     * @param cls
     * @param object
     * @param propertyName
     * @return
     */
    protected Object getFieldValue(Class<?> cls, Object object, String propertyName) {
        try {
            Field field = cls.getDeclaredField(propertyName);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取列名描述
     *
     * @param cls
     * @param object
     * @param propertyName
     * @return
     */
    protected Object getColumnDesc(Class<?> cls, Object object, String propertyName) {
        try {
            Field field = cls.getDeclaredField(propertyName);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            UpdateRecordColumn updateRecordColumn = field.getAnnotation(UpdateRecordColumn.class);

            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通过id获取修改前的对象
     *
     * @param connection
     * @param sql
     * @param id
     */
    protected Map<String, Object> getBeforeMapById(Connection connection, String sql, Object id) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                Map<String, Object> map = new LinkedHashMap<>(columnCount);
                if (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        // 获取列名
                        String columnName = resultSetMetaData.getColumnName(i);
                        int columnType = resultSetMetaData.getColumnType(i);
                        // 获取列值
                        Object value = getResultSetColumnValue(resultSet, i, columnType);
                        // 添加到map
                        map.put(columnName, value);
                    }
                }
                return map;
            }
        } catch (Exception e) {
            throw new UpdateRecordException("获取修改之前的对象失败", e);
        }
    }

    protected Object getResultSetColumnValue(ResultSet resultSet, int i, int columnType) throws SQLException {
        Object value;
        if (Types.DATE == columnType) {
            java.sql.Date date = resultSet.getDate(i);
            value = date == null ? null : new java.util.Date(date.getTime());
        } else if (Types.TIME == columnType || Types.TIME_WITH_TIMEZONE == columnType) {
            Time time = resultSet.getTime(i);
            value = time == null ? null : new java.util.Date(time.getTime());
        } else if (Types.TIMESTAMP == columnType || Types.TIMESTAMP_WITH_TIMEZONE == columnType) {
            Timestamp timestamp = resultSet.getTimestamp(i);
            value = timestamp == null ? null : new java.util.Date(timestamp.getTime());
        } else {
            value = resultSet.getObject(i);
        }
        return value;
    }

    @Override
    public String getVersionColumn() {
        return UpdateRecordConstant.VERSION_COLUMN;
    }


    /**
     * 全局排除表
     *
     * @param tableName
     * @return
     */
    protected boolean excludeTable(String tableName) {
        Set<String> includeTables = UpdateRecordConfigProperties.INCLUDE_TABLES;
        Set<String> excludeTables = UpdateRecordConfigProperties.EXCLUDE_TABLES;

        if (CollectionUtils.isNotEmpty(excludeTables)) {
            if (excludeTables.contains(tableName)) {
                return true;
            }
        }

        if (CollectionUtils.isNotEmpty(includeTables)) {
            if (!includeTables.contains(tableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 全局排除列
     *
     * @param columnName
     * @return
     */
    protected boolean excludeColumn(String columnName) {
        Set<String> includeColumns = UpdateRecordConfigProperties.INCLUDE_COLUMNS;
        Set<String> excludeColumns = UpdateRecordConfigProperties.EXCLUDE_COLUMNS;
        if (CollectionUtils.isNotEmpty(excludeColumns)) {
            if (excludeColumns.contains(columnName)) {
                return true;
            }
        }
        if (CollectionUtils.isNotEmpty(includeColumns)) {
            if (!includeColumns.contains(columnName)) {
                return true;
            }
        }
        return false;
    }

}
