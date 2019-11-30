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

package io.geekidea.updaterecord.persistence.mapper;

import io.geekidea.updaterecord.api.entity.UpdateRecordLog;
import io.geekidea.updaterecord.api.entity.UpdateRecordColumnLog;
import io.geekidea.updaterecord.api.entity.UpdateRecordTableLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <pre>
 * 系统修改记录 Mapper 接口
 * </pre>
 *
 * @author geekidea
 * @since 2019-11-13
 */
public interface UpdateRecordMapper {


    /**
     * 保存修改记录
     *
     * @param updateRecordLog
     * @return
     */
    @Insert({"INSERT INTO update_record_log (id, commit_id, user_id, user_name, name, ip, area, path, url, server_name, module_name, package_name, class_name, method_name, request_method, token, thread_name," +
            "       before_all_value, after_all_value, diff_all_value, update_all_desc, table_total, column_total, add_model_total, update_model_total, delete_model_total, remark, version, create_time, update_time) VALUES (" +
            "       uuid_short(), #{commitId}, #{userId}, #{userName}, #{name}, #{ip}, #{area}, #{path}, #{url}, #{serverName}, #{moduleName}, #{packageName}, #{className}, #{methodName}, #{requestMethod}, #{token}, #{threadName}, " +
            "       #{beforeAllValue}, #{afterAllValue}, #{diffAllValue}, #{updateAllDesc}, #{tableTotal}, #{columnTotal}, #{addModelTotal}, #{updateModelTotal}, #{deleteModelTotal}, #{remark}, #{version}, #{createTime}, #{updateTime})"})
    int saveUpdateRecordLog(UpdateRecordLog updateRecordLog);

    /**
     * 批量保存修改记录表详细
     *
     * @param updateRecordTableLogs
     * @return
     */
    @Insert({"<script>" +
            "INSERT INTO update_record_table_log (id, commit_id, server_name, module_name, method_id, table_name, table_desc, entity_name, id_column_name, id_property_name, id_value, before_value, after_value, diff_value, " +
            "       before_version, after_version, update_desc, total, add_mode_count, update_mode_count, delete_mode_count, remark, version, create_time, update_time) VALUES" +
            "   <foreach collection=\"list\" item=\"item\" separator=\",\">" +
            "       (uuid_short(), #{item.commitId}, #{item.serverName}, #{item.moduleName}, #{item.methodId}, #{item.tableName}, #{item.tableDesc}, #{item.entityName}, #{item.idColumnName}, #{item.idPropertyName}, #{item.idValue}, " +
            "       #{item.beforeValue}, #{item.afterValue}, #{item.diffValue}, #{item.beforeVersion}, #{item.afterVersion}, #{item.updateDesc}, #{item.total}, #{item.addModeCount}, #{item.updateModeCount}, #{item.deleteModeCount}, #{item.remark}, #{item.version}, #{item.createTime}, #{item.updateTime})" +
            "   </foreach>" +
            "</script>"})
    int saveUpdateRecordTableLog(@Param("list") List<UpdateRecordTableLog> updateRecordTableLogs);

    /**
     * 批量保存修改记录列详细
     *
     * @param updateRecordColumnLogs
     * @return
     */
    @Insert({"<script>" +
            "INSERT INTO update_record_column_log (id, commit_id, table_name, id_value, column_name, column_desc, " +
            "       before_value, after_value, mode, before_version, after_version, remark, version, create_time, update_time) VALUES" +
            "   <foreach collection=\"list\" item=\"item\" separator=\",\">" +
            "       (uuid_short(), #{item.commitId}, #{item.tableName}, #{item.idValue}, #{item.columnName}, #{item.columnDesc}, " +
            "       #{item.beforeValue}, #{item.afterValue}, #{item.mode}, #{item.beforeVersion}, #{item.afterVersion}, #{item.remark}, #{item.version}, #{item.createTime}, #{item.updateTime})" +
            "   </foreach>" +
            "</script>"})
    int saveUpdateRecordColumnLog(@Param("list") Set<UpdateRecordColumnLog> updateRecordColumnLogs);

}
