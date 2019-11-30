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

package io.geekidea.updaterecord.mybatis.spring.boot.autoconfigure;

import io.geekidea.updaterecord.core.config.UpdateRecordConfigProperties;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * @author geekidea
 * @date 2019-11-29
 **/
@Data
@ConfigurationProperties(prefix = UpdateRecordProperties.UPDATE_RECORD_PREFIX)
public class UpdateRecordProperties {

    public static final String UPDATE_RECORD_PREFIX = "update-record";

    /**
     * 是否启用debug模式， 默认为false
     */
    private boolean debug = false;

    /**
     * 当前服务名称
     */
    private String serverName;

    /**
     * 服务版本
     */
    private String version = "1.0";

    /**
     * 添加模式格式化字符串
     */
    private String addModelFormat = "%s 添加值：%s";

    /**
     * 修改模式格式化字符串
     */
    private String updateModelFormat = "%s 由 %s 修改为 %s";

    /**
     * 删除模式格式化字符串
     */
    private String deleteModelFormat = "删除%s";

    /**
     * 表记录之间的拆分符
     */
    private String tableSeparator = "\n";

    /**
     * 列记录之间的拆分符
     */
    private String columnSeparator = "\n";

    /**
     * 年月日格式化表达式
     */
    private String dateFormatPattern = "yyyy-MM-dd";

    /**
     * 年月日时分秒格式化表达式
     */
    private String dateTimeFormatPattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 全局包含的表
     */
    private Set<String> includeTables;

    /**
     * 全局排除的表
     */
    private Set<String> excludeTables;

    /**
     * 全局包含字段集合
     */
    private Set<String> includeColumns;

    /**
     * 全局排除字段集合
     */
    private Set<String> excludeColumns;


}
