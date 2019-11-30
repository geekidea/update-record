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

package io.geekidea.updaterecord.core.vo;

import io.geekidea.updaterecord.api.entity.UpdateRecordColumnLog;
import io.geekidea.updaterecord.api.entity.UpdateRecordTableLog;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 表记录修改对象VO
 * @author geekidea
 * @date 2019-11-12
 **/
@Data
@Accessors(chain = true)
public class UpdateRecordItemVo implements Serializable {
    private static final long serialVersionUID = 3151389392610979285L;

    /**
     * 修改记录项
     */
    private UpdateRecordTableLog updateRecordTableLog;

    /**
     * 修改记录详细对象集合
     */
    private Set<UpdateRecordColumnLog> sysUpdateRecordColumnLogs = new LinkedHashSet<>();

    /**
     * 修改之前的map
     */
    private Map<String, Object> beforeMap;

    /**
     * 修改之后的map
     */
    private Map<String, Object> afterMap;

    /**
     * 变更值对象列表
     * <code>
     *   {
     *      "table-0-foo_bar-id-1":{"name":"foo","before":"hello","after":"你好","mode":2},
     *      "table-1-foo_bar-id-2":{"name":"foo","before":"world","after":"世界","mode":2}
     *   }
     * </code>
     */
    private Set<FieldCompareVo> diffSet;
}
