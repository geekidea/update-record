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
 * 系统修改表记录，一个表一行记录
 * </pre>
 *
 * @author geekidea
 * @since 2019-11-13
 */
@Data
@Accessors(chain = true)
public class UpdateRecordTableLog implements Serializable {
    private static final long serialVersionUID = 4763544733112439800L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 当次会话提交唯一标识
     */
    private String commitId;

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 方法ID
     */
    private String methodId;

    /**
     * 修改的表名称
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableDesc;

    /**
     * 修改的实体名称
     */
    private String entityName;

    /**
     * 修改的表主键列名
     */
    private String idColumnName;

    /**
     * 修改的表主键属性名称
     */
    private String idPropertyName;

    /**
     * 修改的表主键值
     */
    private String idValue;

    /**
     * 修改之前的JSON值
     */
    private String beforeValue;

    /**
     * 修改之后的JSON值
     */
    private String afterValue;

    /**
     * 变更的值
     */
    private String diffValue;

    /**
     * 修改之前的版本
     */
    private String beforeVersion;

    /**
     * 修改之后的版本
     */
    private String afterVersion;

    /**
     * 修改内容描述
     */
    private String updateDesc;

    /**
     * 修改列总数量
     */
    private Integer total;

    /**
     * 添加模式数量
     */
    private Integer addModeCount;

    /**
     * 修改模式数量
     */
    private Integer updateModeCount;

    /**
     * 删除模式数量
     */
    private Integer deleteModeCount;

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
