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

package io.geekidea.updaterecord.mybatis;

import io.geekidea.updaterecord.annotation.*;
import io.geekidea.updaterecord.api.vo.ColumnVo;
import io.geekidea.updaterecord.api.vo.TableVo;
import io.geekidea.updaterecord.core.config.UpdateRecordConfigProperties;
import io.geekidea.updaterecord.core.constant.UpdateRecordConstant;
import io.geekidea.updaterecord.core.exception.UpdateRecordException;
import io.geekidea.updaterecord.core.support.UpdateRecordInterceptorSupport;
import io.geekidea.updaterecord.core.util.ConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 原生mybatis拦截器实现类
 *
 * @author geekidea
 * @date 2019-11-16
 **/
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class UpdateRecordMybatisInterceptor extends UpdateRecordInterceptorSupport {

    @Override
    public Object getParameterObject(Object parameterObject) {
        return parameterObject;
    }

    @Override
    public TableVo getTableVo(Class<?> cls) {
        // 获取TableVo，并保存到map缓存中
        TableVo tableVo = new TableVo();
        String entityName = cls.getSimpleName();
        tableVo.setEntityName(entityName);

        // 判断是否忽略
        UpdateRecordIgnore updateRecordIgnoreTable = cls.getAnnotation(UpdateRecordIgnore.class);
        if (updateRecordIgnoreTable != null) {
            return tableVo;
        }

        // 获取实体类注解信息
        UpdateRecordTable updateRecordTable = cls.getAnnotation(UpdateRecordTable.class);
        String tableName;
        String tableDesc;
        if (updateRecordTable != null) {
            tableName = updateRecordTable.name();
            tableDesc = updateRecordTable.value();
        } else {
            tableName = ConverterUtil.camelToUnderline(entityName);
            tableDesc = tableName;
        }
        tableVo.setTableName(tableName);
        tableVo.setTableDesc(tableDesc);

        // 全局判断是否排除
        if (excludeTable(tableName)) {
            tableVo.setRecord(false);
            return tableVo;
        }

        // 获取列
        Field[] fields = cls.getDeclaredFields();
        if (ArrayUtils.isEmpty(fields)) {
            return tableVo;
        }
        // 属性名对象map
        Map<String, ColumnVo> propertyMap = new ConcurrentHashMap<>(fields.length);
        // 列名对象map
        Map<String, ColumnVo> columnMap = new ConcurrentHashMap<>(fields.length);
        int idCount = 0;
        int versionCount = 0;

        for (Field field : fields) {
            field.setAccessible(true);
            // 属性名称
            String propertyName = field.getName();
            // 排除字段
            if (UpdateRecordConstant.DEFAULT_EXCLUDE_FIELDS.contains(propertyName)) {
                continue;
            }

            // 列名称
            String columnName = null;
            // 获取列描述
            String columnDesc = null;
            // 列对象
            ColumnVo columnVo = new ColumnVo();
            columnVo.setPropertyName(propertyName);
            columnVo.setTypeClass(field.getType());

            // 判断是否忽略
            UpdateRecordIgnore updateRecordIgnore = field.getAnnotation(UpdateRecordIgnore.class);
            if (updateRecordIgnore != null) {
                continue;
            }

            // 判断是否是主键
            UpdateRecordId updateRecordId = field.getAnnotation(UpdateRecordId.class);
            if (updateRecordId != null) {
                idCount++;
                if (idCount > 1) {
                    throw new UpdateRecordException("exist many UpdateRecordId, only one");
                }
                String idColumnName;
                String name = updateRecordId.name();
                if (StringUtils.isBlank(name)) {
                    idColumnName = propertyName;
                } else {
                    idColumnName = name;
                }
                tableVo.setIdColumnName(idColumnName);
                tableVo.setIdPropertyName(propertyName);
                continue;
            }

            // 获取列信息
            UpdateRecordColumn updateRecordColumn = field.getAnnotation(UpdateRecordColumn.class);
            if (updateRecordColumn != null) {
                columnName = updateRecordColumn.name();
                columnDesc = updateRecordColumn.value();
                columnVo.setSort(updateRecordColumn.sort());
            }
            if (StringUtils.isBlank(columnName)) {
                columnName = ConverterUtil.camelToUnderline(propertyName);
            }
            if (StringUtils.isBlank(columnDesc)) {
                columnDesc = columnName;
            }
            columnVo.setColumnName(columnName);
            columnVo.setColumnDesc(columnDesc);

            if (excludeColumn(columnName)) {
                continue;
            }

            // 获取版本号
            UpdateRecordVersion updateRecordVersion = field.getAnnotation(UpdateRecordVersion.class);
            if (updateRecordVersion != null) {
                versionCount++;
                if (versionCount > 1) {
                    throw new UpdateRecordException("exist many UpdateRecordVersion, only one");
                }
                tableVo.setVersionColumnName(columnName);
                tableVo.setVersionPropertyName(propertyName);
                columnVo.setColumnDesc(UpdateRecordConstant.VERSION_DESC);
            }
            // 添加到map集合
            propertyMap.put(propertyName, columnVo);
            columnMap.put(columnName, columnVo);
        }
        // 设置map集合
        tableVo.setColumnMap(columnMap);
        tableVo.setPropertyMap(propertyMap);

        // 是否有字段需要记录
        if (MapUtils.isEmpty(columnMap)) {
            tableVo.setRecord(false);
        } else {
            tableVo.setRecord(true);
        }

        return tableVo;
    }

    @Override
    public Map<String, Object> getUpdateAfterMap(Class<?> cls, Object object, List<ParameterMapping> parameterMappings, TableVo tableVo) {
        Map<String, Object> afterMap = new LinkedHashMap<>();
        Map<String, ColumnVo> propertyMap = tableVo.getPropertyMap();
        parameterMappings.forEach(parameterMapping -> {
            String propertyName = parameterMapping.getProperty();
            if (propertyMap.containsKey(propertyName)) {
                Object value = getFieldValue(cls, object, propertyName);
                String columnName = getColumnName(cls, object, propertyName);
                afterMap.put(columnName, value);
            }
        });
        return afterMap;
    }

    @Override
    public String getColumnName(Class<?> cls, Object object, String propertyName) {
        // TODO 默认驼峰转下划线
        return ConverterUtil.camelToUnderline(propertyName);
    }

    @Override
    public Object getVersionValue(Map<String, Object> map) {
        String versionColumn = getVersionColumn();
        return map.get(versionColumn);
    }

    @Override
    public Object getAfterVersionValue(Object beforeVersionValue, Map<String, Object> afterMap) {
        if (beforeVersionValue == null) {
            return null;
        }
        if (beforeVersionValue instanceof Integer) {
            Integer value = (Integer) beforeVersionValue;
            return ++value;
        }

        if (beforeVersionValue instanceof Long) {
            Long value = (Long) beforeVersionValue;
            return ++value;
        }
        return null;
    }
}
