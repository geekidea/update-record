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
 * 用于实体类属性字段上
 * 字段修改记录描述
 * 默认实体类字段会被记录
 * 字段描述：{@code @UpdateRecordColumn("手机号码")}
 * 设置字段不记录：{@code @UpdateRecordIgnore}
 * 完整配置：{@code @UpdateRecordColumn(name="user_name", value="用户名称", sort=1)}
 * </pre>
 *
 * @author geekidea
 * @date 2019-11-09
 **/
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UpdateRecordColumn {

    /**
     * 数据库列名，默认实体类字段为列名的驼峰命名
     * 例如：
     * 属性名：createTime
     * 列名：create_time
     *
     * @return
     */
    String name() default "";

    /**
     * 字段描述
     *
     * @return
     */
    String value() default "";

    /**
     * 排序
     *
     * @return
     */
    int sort() default 0;

}
