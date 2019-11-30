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

import lombok.Data;

import java.io.Serializable;

/**
 * 字段比较对象VO
 *
 * @author geekidea
 * @date 2019-11-10
 **/
@Data
public class FieldCompareVo implements Serializable {
    private static final long serialVersionUID = -2648342375115327440L;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 修改之前的值
     */
    private Object before;

    /**
     * 修改之后的值
     */
    private Object after;

    /**
     * 1: 添加
     * before: null, after:123
     * 2: 修改
     * before: 123, after:456
     * 3: 删除
     * before: 123, after:null
     */
    private Integer mode;

}
