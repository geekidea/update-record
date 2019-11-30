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

create table if not exists update_record_column_log
(
    id bigint null comment '主键',
    commit_id varchar(100) not null comment '当次会话提交唯一标识',
    table_name varchar(100) null comment '表名称',
    id_value varchar(100) null comment '修改表的主键值',
    column_name varchar(100) null comment '列名',
    column_desc varchar(100) null comment '列描述',
    before_value text null comment '修改之前的值',
    after_value text null comment '修改之后的值',
    mode int null comment '修改模式 1：添加/2：修改：3：删除',
    before_version varchar(100) null comment '修改之前的版本',
    after_version varchar(100) null comment '修改之后的版本',
    remark varchar(200) null comment '备注',
    version varchar(100) null comment '服务版本',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp null comment '修改时间'
)
    comment '系统修改列记录日志';

create index update_record_column_log_commit_id_index
    on update_record_column_log (commit_id);

create table if not exists update_record_log
(
    id bigint(18) not null comment '主键'
        primary key,
    commit_id varchar(100) not null comment '当次会话提交唯一标识',
    user_id varchar(100) null comment '用户ID',
    user_name varchar(32) null comment '用户名称',
    name varchar(200) null comment '日志名称',
    ip varchar(15) null comment 'IP',
    area varchar(100) null comment '区域',
    path varchar(500) null comment '全路径',
    url varchar(100) null comment '访问路径',
    server_name varchar(100) null comment '服务名称',
    module_name varchar(100) null comment '模块名称',
    package_name varchar(200) null comment '包名',
    class_name varchar(100) null comment '类名',
    method_name varchar(100) null comment '方法名称',
    request_method varchar(10) null comment '请求方式，GET/POST',
    token varchar(1000) null comment 'token',
    thread_name varchar(100) null comment '线程名称',
    before_all_value text null comment '修改之前的JSON值',
    after_all_value text null comment '修改之后的JSON值',
    diff_all_value text null comment '变更的所有值',
    update_all_desc text null comment '所有修改内容描述',
    table_total int null comment '修改表数量',
    column_total int null comment '修改列数量',
    add_model_total int null comment '添加模式数量',
    update_model_total int null comment '修改模式数量',
    delete_model_total int null comment '删除模式数量',
    remark varchar(200) null comment '备注',
    version varchar(100) null comment '服务版本',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime null comment '修改时间',
    constraint update_record_log_commit_id_uindex
        unique (commit_id)
)
    comment '系统修改记录日志';

create table if not exists update_record_table_log
(
    id bigint(18) not null comment '主键'
        primary key,
    commit_id varchar(100) null comment '当次会话提交唯一标识',
    server_name varchar(100) null comment '服务名称',
    module_name varchar(100) null comment '模块名称',
    method_id varchar(300) null comment '方法ID',
    table_name varchar(100) null comment '修改的表名称',
    table_desc varchar(100) null comment '表描述',
    entity_name varchar(100) null comment '修改的实体名称',
    id_column_name varchar(100) null comment '修改的表主键列名',
    id_property_name varchar(100) null comment '修改的表主键属性名称',
    id_value varchar(100) null comment '修改的表主键值',
    before_value text null comment '修改之前的JSON值',
    after_value text null comment '修改之后的JSON值',
    diff_value text null comment '变更的值',
    before_version varchar(100) null comment '修改之前的版本',
    after_version varchar(100) null comment '修改之后的版本',
    update_desc text null comment '修改内容描述',
    total int null comment '修改列总数量',
    add_mode_count int null comment '添加模式数量',
    update_mode_count int null comment '修改模式数量',
    delete_mode_count int null comment '删除模式数量',
    remark varchar(200) null comment '备注',
    version varchar(100) null comment '服务版本',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime null comment '修改时间'
)
    comment '系统修改表记录';

create index update_record_table_log_commit_id_index
    on update_record_table_log (commit_id);

