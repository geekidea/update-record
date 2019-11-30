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

package io.geekidea.updaterecord.api.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * 系统修改列记录日志-一个列一行记录
 * </pre>
 *
 * @author geekidea
 * @since 2019-11-13
 */
@Data
@Accessors(chain = true)
public class UpdateRecordColumnLog implements Serializable {
    private static final long serialVersionUID = 2416834069379601428L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 当次会话提交唯一标识
     */
    private String commitId;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 修改表的主键值
     */
    private String idValue;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列描述
     */
    private String columnDesc;

    /**
     * 修改之前的值
     */
    private String beforeValue;

    /**
     * 修改之后的值
     */
    private String afterValue;

    /**
     * 修改模式 1：添加/2：修改：3：删除
     */
    private Integer mode;

    /**
     * 修改之前的版本
     */
    private String beforeVersion;

    /**
     * 修改之后的版本
     */
    private String afterVersion;

    /**
     * 备注
     */
    private String remark;

    /**
     * 服务版本
     */
    private String version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
