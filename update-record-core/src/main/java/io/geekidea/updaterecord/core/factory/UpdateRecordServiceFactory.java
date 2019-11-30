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

package io.geekidea.updaterecord.core.factory;

import io.geekidea.updaterecord.api.service.UpdateRecordService;
import io.geekidea.updaterecord.core.service.impl.DefaultUpdateRecordServiceImpl;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 修改记录服务工厂类
 *
 * @author geekidea
 * @date 2019-11-23
 **/
@Component
public class UpdateRecordServiceFactory {
    /**
     * 修改服务实现类bean名称
     */
    private static final String UPDATE_RECORD_SERVICE_IMPL = "updateRecordServiceImpl";
    private UpdateRecordService updateRecordService;

    /**
     * 构造函数中注入UpdateRecordService接口的所有实现类
     * 通常有三种情况
     * 1. 默认至少有一个实现类 DefaultUpdateRecordServiceImpl
     * 2. 依赖 update-record-persistence 模块时，注入：UpdateRecordServiceImpl
     * 3. 如果需要自定义，则不依赖 update-record-persistence 模块，注入自定义实现类
     *
     * @param map
     */
    public UpdateRecordServiceFactory(Map<String, UpdateRecordService> map) {
        if (MapUtils.isEmpty(map)) {
            updateRecordService = new DefaultUpdateRecordServiceImpl();
        } else if (map.containsKey(UPDATE_RECORD_SERVICE_IMPL)) {
            updateRecordService = map.get(UPDATE_RECORD_SERVICE_IMPL);
        } else {
            map.remove(UPDATE_RECORD_SERVICE_IMPL);
            updateRecordService = map.values().iterator().next();
        }
    }

    public UpdateRecordService getUpdateRecordService() {
        return updateRecordService;
    }

}
