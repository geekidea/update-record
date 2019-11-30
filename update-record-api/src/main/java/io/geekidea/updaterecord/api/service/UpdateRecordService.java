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

package io.geekidea.updaterecord.api.service;


import io.geekidea.updaterecord.api.entity.UpdateRecordLog;
import io.geekidea.updaterecord.api.entity.UpdateRecordColumnLog;
import io.geekidea.updaterecord.api.entity.UpdateRecordTableLog;

import java.util.List;
import java.util.Set;

/**
 * <pre>
 * 系统修改记录 服务接口
 * </pre>
 *
 * @author geekidea
 * @since 2019-11-13
 */
public interface UpdateRecordService {

    /**
     * 保存修改记录
     *
     * @param updateRecordLog 修改记录
     */
    void saveUpdateRecordLog(UpdateRecordLog updateRecordLog);

    /**
     * 保存单个表的修改记录
     *
     * @param updateRecordTableLogs 单个表的修改记录
     */
    void saveUpdateRecordTableLog(List<UpdateRecordTableLog> updateRecordTableLogs);

    /**
     * 保存字段的详细修改记录
     *
     * @param updateRecordColumnLogs 字段的详细修改记录
     */
    void saveUpdateRecordColumnLog(Set<UpdateRecordColumnLog> updateRecordColumnLogs);

    /**
     * 保存请求的修改记录
     *
     * @param updateRecordLog        修改记录
     * @param updateRecordTableLogs  单个表的修改记录
     * @param updateRecordColumnLogs 字段的详细修改记录
     */
    void save(UpdateRecordLog updateRecordLog, List<UpdateRecordTableLog> updateRecordTableLogs, Set<UpdateRecordColumnLog> updateRecordColumnLogs);

}
