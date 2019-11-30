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
 * 作用于mapper修改方法上
 * 当修改参数为map时，可在方法上加上该注解，映射表和某个实体关联
 * 类描述：{@code @UpdateRecordRelation(Account.class)}
 * </pre>
 *
 * @author geekidea
 * @date 2019-11-27
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UpdateRecordParam {

    Class<?> value();

}
