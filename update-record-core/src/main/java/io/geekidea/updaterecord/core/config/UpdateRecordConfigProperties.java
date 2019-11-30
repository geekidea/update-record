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

package io.geekidea.updaterecord.core.config;

import io.geekidea.updaterecord.core.constant.UpdateRecordConstant;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 修改记录配置属性
 *
 * @author geekidea
 * @date 2019-11-13
 **/
@Component
public class UpdateRecordConfigProperties {

    public static boolean DEBUG = false;
    public static String SERVER_NAME;
    public static String VERSION = "1.0";
    public static String ADD_MODEL_FORMAT = "%s 添加值：%s";
    public static String UPDATE_MODEL_FORMAT = "%s 由 %s 修改为 %s";
    public static String DELETE_MODEL_FORMAT = "删除%s";
    public static String TABLE_SEPARATOR = UpdateRecordConstant.DEFAULT_SEPARATOR;
    public static String COLUMN_SEPARATOR = UpdateRecordConstant.DEFAULT_SEPARATOR;
    public static String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    public static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
    public static Set<String> INCLUDE_TABLES = new HashSet<>();
    public static Set<String> EXCLUDE_TABLES = new HashSet<>();
    public static Set<String> INCLUDE_COLUMNS = new HashSet<>();
    public static Set<String> EXCLUDE_COLUMNS = new HashSet<>();

}
