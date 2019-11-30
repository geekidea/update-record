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

package io.geekidea.updaterecord.annotation;

import java.lang.annotation.*;

/**
 * <pre>
 * 用于实体类上
 * 表修改记录描述
 * 默认修改时，实体类会被记录
 * 类描述：{@code @UpdateRecordEntity("系统用户")}
 * 设置该类不记录：{@code @UpdateRecordIgnore}
 * 完整配置：{@code @UpdateRecordEntity(name="sys_user", value="系统用户", module="system",separator=",")}
 * </pre>
 *
 * @author geekidea
 * @date 2019-11-09
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UpdateRecordTable {

    /**
     * 数据库表名，默认实体类字段为表名的驼峰命名
     * 例如：
     * 实体类：SysUser
     * 表名：sys_user
     *
     * @return
     */
    String name() default "";

    /**
     * 表描述
     *
     * @return
     */
    String value() default "";

    /**
     * 分隔符
     *
     * @return
     */
    String separator() default "\n";

}
