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

package io.geekidea.updaterecord.core.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

/**
 * 转换工具类
 *
 * @author geekidea
 * @date 2019-11-17
 **/
public class ConverterUtil {

    /**
     * 驼峰命名转下划线命名
     *
     * @param cameString
     * @return
     */
    public static String camelToUnderline(String cameString) {
        return CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(cameString);
    }
}
