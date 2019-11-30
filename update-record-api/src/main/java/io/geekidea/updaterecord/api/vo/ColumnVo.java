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

package io.geekidea.updaterecord.api.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 列信息对象
 * @author geekidea
 * @date 2019-11-24
 **/
@Data
@Accessors(chain = true)
public class ColumnVo implements Serializable {
    private static final long serialVersionUID = -189450303083131452L;

    /**
     * 属性名称
     */
    private String propertyName;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列名描述
     */
    private String columnDesc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 类型Class
     */
    private Class<?> typeClass;
}
