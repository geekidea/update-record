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

import io.geekidea.updaterecord.core.constant.UpdateRecordConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 *
 * @author geekidea
 * @date 2019-12-06
 **/
public class UpdateRecordUtil {

    /**
     * 将集合转换成字符串，使用分隔符串联
     *
     * @param list
     * @param separator
     * @return
     */
    public static String join(List<String> list, String separator) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (StringUtils.isEmpty(separator)) {
            separator = UpdateRecordConstant.SEPARATOR;
        }
        return StringUtils.join(list, separator);
    }

}
