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

package io.geekidea.updaterecord.core.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 修改记录常量
 *
 * @author geekidea
 * @date 2019-11-13
 **/
public interface UpdateRecordConstant {

    /**
     * 表名称
     */
    String TABLE = "table";

    /**
     * 变更map key
     * table-0-foo_bar-id-1
     */
    String DIFF_MAP_KEY = "%s-%d-%s-%s-%s";

    /**
     * 分隔符
     */
    String SEPARATOR = "\r\n";


    String DELEGATE_MAPPEDSTATEMENT = "delegate.mappedStatement";

    String DELEGATE_BOUNDSQL = "delegate.boundSql";

    String H_TARGET = "h.target";

    String ET = "et";
    String VERSION_COLUMN = "version";

    String VERSION_DESC = "版本";

    String SELECT_SQL = "select %s from %s where %s = ?";

    List<String> DEFAULT_EXCLUDE_FIELDS = Arrays.asList("serialVersionUID");

    String DEFAULT_SEPARATOR = "\n";
}
