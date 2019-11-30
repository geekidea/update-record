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
 * 用于Controller方法上
 * 记录当前请求修改记录
 * 类描述：{@code @UpdateRecord("系统用户")}
 * 完整配置：{@code @UpdateRecordEntity(value="系统用户", module="system",includes={},excludes={})}
 * </pre>
 *
 * @author geekidea
 * @date 2019-11-09
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UpdateRecord {

    /**
     * 业务方法描述
     *
     * @return
     */
    String value() default "";

    /**
     * 模块名称
     *
     * @return
     */
    String module() default "";

}
