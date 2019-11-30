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

import io.geekidea.updaterecord.api.entity.UpdateRecordLog;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 当前请求的所有修改记录对象VO
 *
 * @author geekidea
 * @date 2019-11-10
 **/
@Data
@Accessors(chain = true)
public class UpdateRecordVo implements Serializable {
    private static final long serialVersionUID = -7801167457120562470L;

    /**
     * 线程名称
     */
    private String threadName;

    /**
     * 当次修改会话的唯一标识
     */
    private String commitId;

    /**
     * 修改记录
     */
    private UpdateRecordLog updateRecordLog;

    /**
     * 修改记录,当前mapper方法信息
     */
    private List<UpdateRecordItemVo> updateRecordItemVos = new ArrayList<>();

}
