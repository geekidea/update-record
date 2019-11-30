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
 * 系统修改记录
 * </pre>
 *
 * @author geekidea
 * @since 2019-11-13
 */
@Data
@Accessors(chain = true)
public class UpdateRecordLog implements Serializable {
    private static final long serialVersionUID = 2949060961942011548L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 当次会话提交唯一标识不能为空
     */
    private String commitId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 日志名称
     */
    private String name;

    /**
     * IP
     */
    private String ip;

    /**
     * 区域
     */
    private String area;

    /**
     * 全路径
     */
    private String path;

    /**
     * 访问路径
     */
    private String url;

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 请求方式，GET/POST
     */
    private String requestMethod;

    /**
     * token
     */
    private String token;

    /**
     * 线程名称
     */
    private String threadName;

    /**
     * 修改之前的JSON值
     */
    private String beforeAllValue;

    /**
     * 修改之后的JSON值
     */
    private String afterAllValue;

    /**
     * 变更的所有值
     */
    private String diffAllValue;

    /**
     * 所有修改内容描述
     */
    private String updateAllDesc;

    /**
     * 修改表数量
     */
    private Integer tableTotal;

    /**
     * 修改列数量
     */
    private Integer columnTotal;

    /**
     * 添加模式数量
     */
    private Integer addModelTotal;

    /**
     * 修改模式数量
     */
    private Integer updateModelTotal;

    /**
     * 删除模式数量
     */
    private Integer deleteModelTotal;

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
