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

package io.geekidea.updaterecord.persistence.service.impl;

import io.geekidea.updaterecord.api.entity.UpdateRecordLog;
import io.geekidea.updaterecord.api.entity.UpdateRecordColumnLog;
import io.geekidea.updaterecord.api.entity.UpdateRecordTableLog;
import io.geekidea.updaterecord.api.service.UpdateRecordService;
import io.geekidea.updaterecord.persistence.mapper.UpdateRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * <pre>
 * 系统修改记录 服务实现类
 * </pre>
 *
 * @author geekidea
 * @since 2019-11-13
 */
@Slf4j
@Service
public class UpdateRecordServiceImpl implements UpdateRecordService {

    @Autowired
    private UpdateRecordMapper updateRecordMapper;


    @Override
    public void saveUpdateRecordLog(UpdateRecordLog updateRecordLog) {
        int result = updateRecordMapper.saveUpdateRecordLog(updateRecordLog);
        if (result == 0) {
            throw new RuntimeException("保存updateRecordLog失败");
        }
    }

    @Override
    public void saveUpdateRecordTableLog(List<UpdateRecordTableLog> updateRecordTableLogs) {
        int result = updateRecordMapper.saveUpdateRecordTableLog(updateRecordTableLogs);
        if (result == 0) {
            throw new RuntimeException("保存updateRecordTableLogs失败");
        }
    }

    @Override
    public void saveUpdateRecordColumnLog(Set<UpdateRecordColumnLog> updateRecordColumnLogs) {
        int result = updateRecordMapper.saveUpdateRecordColumnLog(updateRecordColumnLogs);
        if (result == 0) {
            throw new RuntimeException("保存updateRecordColumnLogs失败");
        }
    }

    @Override
    public void save(UpdateRecordLog updateRecordLog, List<UpdateRecordTableLog> updateRecordTableLogs, Set<UpdateRecordColumnLog> updateRecordColumnLogs) {
        saveUpdateRecordLog(updateRecordLog);
        saveUpdateRecordTableLog(updateRecordTableLogs);
        saveUpdateRecordColumnLog(updateRecordColumnLogs);
    }
}
