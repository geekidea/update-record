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
import java.util.Map;

/**
 * 表信息对象
 *
 * @author geekidea
 * @date 2019-11-16
 **/
@Data
@Accessors(chain = true)
public class TableVo implements Serializable {

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableDesc;

    /**
     * 主键列名称
     */
    private String idColumnName;

    /**
     * 主键属性名称
     */
    private String idPropertyName;

    /**
     * 版本列名称
     */
    private String versionColumnName;

    /**
     * 版本属性名称
     */
    private String versionPropertyName;

    /**
     * 属性名对象map
     */
    private Map<String, ColumnVo> propertyMap;

    /**
     * 列名对象map
     */
    private Map<String, ColumnVo> columnMap;

    /**
     * 是否有字段需要记录
     * 默认记录
     */
    private boolean record = true;
}
