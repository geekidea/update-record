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

import io.geekidea.updaterecord.annotation.UpdateRecord;
import io.geekidea.updaterecord.core.config.UpdateRecordConfigProperties;
import io.geekidea.updaterecord.core.util.Jackson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * @author geekidea
 * @date 2019-11-29
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(UpdateRecordProperties.class)
@MapperScan("io.geekidea.updaterecord.persistence.mapper")
public class UpdateRecordAutoConfiguration implements InitializingBean {

    @Autowired
    private UpdateRecordProperties updateRecordProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (updateRecordProperties == null) {
            log.error("updateRecordProperties is null");
            return;
        }
        log.debug("updateRecordProperties:" + Jackson.toJsonString(updateRecordProperties));
        if (StringUtils.isNotBlank(updateRecordProperties.getAddModelFormat())) {
            UpdateRecordConfigProperties.ADD_MODEL_FORMAT = updateRecordProperties.getAddModelFormat();
        }
        if (StringUtils.isNotBlank(updateRecordProperties.getServerName())) {
            UpdateRecordConfigProperties.SERVER_NAME = updateRecordProperties.getServerName();
        }
        if (StringUtils.isNotBlank(updateRecordProperties.getVersion())) {
            UpdateRecordConfigProperties.VERSION = updateRecordProperties.getVersion();
        }
        if (StringUtils.isNotBlank(updateRecordProperties.getUpdateModelFormat())) {
            UpdateRecordConfigProperties.UPDATE_MODEL_FORMAT = updateRecordProperties.getUpdateModelFormat();
        }
        if (StringUtils.isNotBlank(updateRecordProperties.getDeleteModelFormat())) {
            UpdateRecordConfigProperties.DELETE_MODEL_FORMAT = updateRecordProperties.getDeleteModelFormat();
        }
        if (StringUtils.isNotBlank(updateRecordProperties.getTableSeparator())) {
            UpdateRecordConfigProperties.TABLE_SEPARATOR = updateRecordProperties.getTableSeparator();
        }
        if (StringUtils.isNotBlank(updateRecordProperties.getColumnSeparator())) {
            UpdateRecordConfigProperties.COLUMN_SEPARATOR = updateRecordProperties.getColumnSeparator();
        }
        if (StringUtils.isNotBlank(updateRecordProperties.getDateFormatPattern())) {
            UpdateRecordConfigProperties.DATE_FORMAT_PATTERN = updateRecordProperties.getDateFormatPattern();
            UpdateRecordConfigProperties.DATE_FORMAT = new SimpleDateFormat(updateRecordProperties.getDateFormatPattern());
        }
        if (StringUtils.isNotBlank(updateRecordProperties.getDateTimeFormatPattern())) {
            UpdateRecordConfigProperties.DATE_TIME_FORMAT_PATTERN = updateRecordProperties.getDateTimeFormatPattern();
            UpdateRecordConfigProperties.DATE_TIME_FORMAT = new SimpleDateFormat(updateRecordProperties.getDateTimeFormatPattern());
        }

        if (CollectionUtils.isNotEmpty(updateRecordProperties.getIncludeTables())) {
            UpdateRecordConfigProperties.INCLUDE_TABLES = updateRecordProperties.getIncludeTables();
        }
        if (CollectionUtils.isNotEmpty(updateRecordProperties.getExcludeTables())) {
            UpdateRecordConfigProperties.EXCLUDE_TABLES = updateRecordProperties.getExcludeTables();
        }
        if (CollectionUtils.isNotEmpty(updateRecordProperties.getIncludeColumns())) {
            UpdateRecordConfigProperties.INCLUDE_COLUMNS = updateRecordProperties.getIncludeColumns();
        }
        if (CollectionUtils.isNotEmpty(updateRecordProperties.getExcludeColumns())) {
            UpdateRecordConfigProperties.EXCLUDE_COLUMNS = updateRecordProperties.getExcludeColumns();
        }
    }

}
