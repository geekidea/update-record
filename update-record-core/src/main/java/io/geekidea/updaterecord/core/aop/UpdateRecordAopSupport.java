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

package io.geekidea.updaterecord.core.aop;

import io.geekidea.updaterecord.annotation.UpdateRecord;
import io.geekidea.updaterecord.api.entity.UpdateRecordLog;
import io.geekidea.updaterecord.api.entity.UpdateRecordColumnLog;
import io.geekidea.updaterecord.api.entity.UpdateRecordTableLog;
import io.geekidea.updaterecord.api.service.UpdateRecordService;
import io.geekidea.updaterecord.core.cache.UpdateRecordCache;
import io.geekidea.updaterecord.core.cache.UpdateRecordThreadLocalCache;
import io.geekidea.updaterecord.core.config.UpdateRecordConfigProperties;
import io.geekidea.updaterecord.core.constant.UpdateRecordConstant;
import io.geekidea.updaterecord.core.factory.UpdateRecordServiceFactory;
import io.geekidea.updaterecord.core.util.IpUtil;
import io.geekidea.updaterecord.core.util.Jackson;
import io.geekidea.updaterecord.core.util.UuidUtil;
import io.geekidea.updaterecord.core.vo.FieldCompareVo;
import io.geekidea.updaterecord.core.vo.UpdateRecordItemVo;
import io.geekidea.updaterecord.core.vo.UpdateRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <pre>
 *     修改记录AOP Support
 * </pre>
 *
 * @author geekidea
 * @date 2019-11-10
 */
@Slf4j
@Component
public class UpdateRecordAopSupport {

    /**
     * 返回值对象名称
     */
    protected static final String RESULT = "result";

    /**
     * 异常对象参数名称
     */
    protected static final String EXCEPTION = "exception";

    /**
     * 默认的token名称
     */
    protected static final String TOKEN = "token";

    @Autowired
    protected UpdateRecordService updateRecordService;

    /**
     * 方法执行之前，子类重写，标注切点
     *
     * @param joinPoint
     * @throws Throwable
     */
    protected void doBefore(JoinPoint joinPoint) throws Throwable {

    }

    /**
     * 方法正常执行并成功返回，子类重写，标注切点和返回值属性名称为：result
     *
     * @param joinPoint
     * @param result
     * @throws Throwable
     */
    protected void doAfterReturning(JoinPoint joinPoint, Object result) throws Throwable {

    }

    /**
     * 方法执行之后
     *
     * @param joinPoint
     * @throws Throwable
     */
    protected void doAfter(JoinPoint joinPoint) throws Throwable {

    }

    /**
     * 方法跑异常处理
     *
     * @param exception
     * @throws Throwable
     */
    protected void doAfterThrowing(Throwable exception) throws Throwable {

    }


    /**
     * 方法执行之前，默认处理支持
     *
     * @param joinPoint
     * @throws Throwable
     */
    protected void doBeforeHandle(JoinPoint joinPoint) throws Throwable {
        try {
            log.debug("调用目标方法之前");
            UpdateRecordVo updateRecordVo = new UpdateRecordVo();
            updateRecordVo.setCommitId(UuidUtil.getUuid());
            updateRecordVo.setThreadName(Thread.currentThread().getName());
            updateRecordVo.setUpdateRecordItemVos(new ArrayList<>());

            // 获取请求信息
            UpdateRecordLog updateRecordLog = handleRequestInfo(joinPoint, updateRecordVo);
            updateRecordVo.setUpdateRecordLog(updateRecordLog);

            // 设置到线程变量中
            UpdateRecordThreadLocalCache.set(updateRecordVo);
        } catch (Exception e) {
            log.error("获取请求信息错误", e);
        }
    }

    /**
     * 获取请求信息
     *
     * @param joinPoint
     * @param updateRecordVo
     * @return
     */
    protected UpdateRecordLog handleRequestInfo(JoinPoint joinPoint, UpdateRecordVo updateRecordVo) {
        // 获取当前的HttpServletRequest对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        Class cls = method.getDeclaringClass();
        String className = cls.getSimpleName();
        String packageName = cls.getPackage().getName();

        String moduleName = null;
        UpdateRecord updateRecord = (UpdateRecord) cls.getAnnotation(UpdateRecord.class);
        if (updateRecord != null) {
            moduleName = updateRecord.module();
        }

        String name = null;
        updateRecord = method.getAnnotation(UpdateRecord.class);
        if (updateRecord != null) {
            if (StringUtils.isNotBlank(updateRecord.module())){
                moduleName = updateRecord.module();
            }
            if (StringUtils.isNotBlank(updateRecord.value())){
                name = updateRecord.value();
            }
        }

        // 创建修改记录对象
        UpdateRecordLog updateRecordLog = new UpdateRecordLog();
        // 服务名称
        updateRecordLog.setServerName(UpdateRecordConfigProperties.SERVER_NAME);
        // 服务版本
        updateRecordLog.setVersion(UpdateRecordConfigProperties.VERSION);
        // 模块名称
        updateRecordLog.setModuleName(moduleName);
        // 业务名称
        updateRecordLog.setName(name);

        // 设置当前方法的包名/类名/方法名
        updateRecordLog
                .setPackageName(packageName)
                .setClassName(className)
                .setMethodName(methodName);


        // 请求信息
        String path = request.getRequestURL().toString();
        String url = request.getRequestURI();
        String ip = IpUtil.getRequestIp();
        String requestMethod = request.getMethod();
        String token = getToken(request);
        // 设置请求全路径/IP地址/请求方式/设置token/线程名称
        updateRecordLog
                .setPath(path)
                .setUrl(url)
                .setIp(ip)
                .setRequestMethod(requestMethod)
                .setToken(token)
                .setThreadName(Thread.currentThread().getName());
        return updateRecordLog;

    }

    /**
     * 获取token名称
     *
     * @param request
     * @return
     */
    protected String getToken(HttpServletRequest request) {
        return request.getHeader(TOKEN);
    }

    /**
     * 方法正常执行完成处理
     *
     * @param joinPoint
     * @param result
     * @throws Throwable
     */
    @Async
    protected void doAfterReturningHandle(JoinPoint joinPoint, Object result) throws Throwable {
        try {
            log.debug("Aop执行成功");
            // 从线程变量中获取
            UpdateRecordVo updateRecordVo = UpdateRecordThreadLocalCache.get();
            String commitId = updateRecordVo.getCommitId();
            // 当前请求的所有修改表记录集合，如果为空，则跳过
            List<UpdateRecordItemVo> list = updateRecordVo.getUpdateRecordItemVos();
            if (CollectionUtils.isEmpty(list)) {
                return;
            }

            // 修改记录
            UpdateRecordLog updateRecordLog = updateRecordVo.getUpdateRecordLog();
            updateRecordLog.setCommitId(commitId);
            // 所有修改表的集合
            List<UpdateRecordTableLog> updateRecordTableLogs = new ArrayList<>();
            // 所有修改列的集合
            Set<UpdateRecordColumnLog> updateRecordColumnLogs = new LinkedHashSet<>();
            StringBuilder builder = new StringBuilder();
            // 修改数量统计
            // 修改表总数
            int tableTotal = 0;
            // 修改列总数
            int columnTotal = 0;
            // 添加模式总数
            int addModelTotal = 0;
            // 修改模式总数
            int updateModelTotal = 0;
            // 删除模式总数
            int deleteModelTotal = 0;

            // 修改之前的map集合
            List<Map<String, Object>> beforeAllListMap = new ArrayList<>();
            // 修改之后的map集合
            List<Map<String, Object>> afterAllListMap = new ArrayList<>();
            // 变更集合
            Map<String, Set<FieldCompareVo>> diffAllMap = new LinkedHashMap<>();
            // 拆分符
            String tableSeparator = UpdateRecordConfigProperties.TABLE_SEPARATOR;
            // 批量比较
            int size = list.size();
            for (int i = 0; i < size; i++) {
                UpdateRecordItemVo itemVo = list.get(i);
                UpdateRecordTableLog updateRecordTableLog = itemVo.getUpdateRecordTableLog();
                updateRecordTableLog.setCommitId(commitId)
                        .setServerName(updateRecordLog.getServerName())
                        .setModuleName(updateRecordLog.getModuleName())
                        .setVersion(updateRecordLog.getVersion());

                // 修改之前的map集合
                Map<String, Object> beforeMap = itemVo.getBeforeMap();
                // 修改之后的map集合
                Map<String, Object> afterMap = itemVo.getAfterMap();
                // 实际修改的集合
                Set<FieldCompareVo> diffSet = itemVo.getDiffSet();
                // 所有修改之前的map集合
                beforeAllListMap.add(beforeMap);
                // 所有修改之后的map集合
                afterAllListMap.add(afterMap);
                // 表修改之前的json
                updateRecordTableLog.setBeforeValue(Jackson.toJsonString(beforeMap));
                // 表修改之后的json
                updateRecordTableLog.setAfterValue(Jackson.toJsonString(afterMap));

                if (CollectionUtils.isEmpty(diffSet)) {
                    updateRecordTableLog.setDiffValue(null);
                } else {
                    updateRecordTableLog.setDiffValue(Jackson.toJsonString(diffSet));
                    String diffMapKey = generateDiffMapKey(i, updateRecordTableLog);
                    diffAllMap.put(diffMapKey, diffSet);
                }

                String updateDesc = updateRecordTableLog.getUpdateDesc();
                if (StringUtils.isNotBlank(updateDesc)) {
                    builder.append(updateDesc + tableSeparator);
                }

                tableTotal++;
                columnTotal += updateRecordTableLog.getTotal();
                addModelTotal += updateRecordTableLog.getAddModeCount();
                updateModelTotal += updateRecordTableLog.getUpdateModeCount();
                deleteModelTotal += updateRecordTableLog.getDeleteModeCount();

                // 表中列的修改记录集合
                Set<UpdateRecordColumnLog> details = itemVo.getSysUpdateRecordColumnLogs();
                // 当前请求所有的表集合
                updateRecordTableLogs.add(updateRecordTableLog);
                // 当前请求所有的详细列表集合
                updateRecordColumnLogs.addAll(details);
            }

            // 设置beforeValue集合
            updateRecordLog
                    .setBeforeAllValue(Jackson.toJsonString(beforeAllListMap))
                    .setAfterAllValue(Jackson.toJsonString(afterAllListMap));

            if (MapUtils.isEmpty(diffAllMap)) {
                updateRecordLog.setDiffAllValue(null);
            } else {
                updateRecordLog.setDiffAllValue(Jackson.toJsonString(diffAllMap));
            }

            // 去掉最后一个分隔符
            String updateAllDesc = builder.toString();
            if (StringUtils.isNotBlank(updateAllDesc)){
                int lastIndex = updateAllDesc.lastIndexOf(tableSeparator);
                updateAllDesc = updateAllDesc.substring(0,lastIndex);
            }

            // 设置修改记录
            updateRecordLog
                    .setUpdateAllDesc(updateAllDesc)
                    .setTableTotal(tableTotal)
                    .setColumnTotal(columnTotal)
                    .setAddModelTotal(addModelTotal)
                    .setUpdateModelTotal(updateModelTotal)
                    .setDeleteModelTotal(deleteModelTotal)
                    .setCreateTime(new Date());

            // 将处理后的修改记录对象，返回给子类，自定义处理
            handleUpdateRecord(updateRecordLog, updateRecordTableLogs, updateRecordColumnLogs);

            // 保存修改记录
            save(updateRecordLog, updateRecordTableLogs, updateRecordColumnLogs);
        } catch (Exception e) {
            log.error("处理修改记录失败", e);
        } finally {
            // 释放资源
            remove();
        }
    }

    /**
     * 将处理后的修改记录对象，返回给子类，自定义处理
     * @param updateRecordLog
     * @param updateRecordTableLogs
     * @param updateRecordColumnLogs
     */
    protected void handleUpdateRecord(UpdateRecordLog updateRecordLog, List<UpdateRecordTableLog> updateRecordTableLogs, Set<UpdateRecordColumnLog> updateRecordColumnLogs) {

    }


    /**
     * 方法异常处理
     *
     * @param exception
     * @throws Throwable
     */
    protected void doAfterThrowingHandle(Throwable exception) throws Throwable {
        remove();
    }

    /**
     * 释放资源
     */
    protected void remove() {
        UpdateRecordThreadLocalCache.remove();
    }

    /**
     * 生成变更map的key
     *
     * @param i
     * @param updateRecordTableLog
     * @return
     */
    private static String generateDiffMapKey(int i, UpdateRecordTableLog updateRecordTableLog) {
        String tableName = updateRecordTableLog.getTableName();
        String idColumnName = updateRecordTableLog.getIdColumnName();
        String idValue = updateRecordTableLog.getIdValue();
        // table-0-foo_bar-id-1
        return String.format(UpdateRecordConstant.DIFF_MAP_KEY, UpdateRecordConstant.TABLE, i, tableName, idColumnName, idValue);
    }

    /**
     * 保存
     *
     * @param updateRecordLog
     * @param updateRecordTableLogs
     * @param updateRecordColumnLogs
     */
    protected void save(UpdateRecordLog updateRecordLog, List<UpdateRecordTableLog> updateRecordTableLogs, Set<UpdateRecordColumnLog> updateRecordColumnLogs) {
        boolean format = UpdateRecordConfigProperties.DEBUG;
        log.info("updateRecordLog：{}", Jackson.toJsonString(updateRecordLog, true));
        log.info("updateRecordTableLogs：{}", Jackson.toJsonString(updateRecordTableLogs, true));
        log.info("updateRecordColumnLogs：{}", Jackson.toJsonString(updateRecordColumnLogs, true));
        updateRecordService.save(updateRecordLog, updateRecordTableLogs, updateRecordColumnLogs);
    }

    @Autowired
    public void setUpdateRecordServiceFactory(UpdateRecordServiceFactory updateRecordServiceFactory) {
        updateRecordService = updateRecordServiceFactory.getUpdateRecordService();
    }

}
