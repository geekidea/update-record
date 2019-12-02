# update-record

> Update-record is a mybatis plugin. Combined with spring AOP, it records the changes of the specified request / data table before and after modification, and records the modification log to the database。

## Website
[update-record](http://geekidea.io/update-record/)

## Quick Start

### 1. Create a log table for update record
[update-record-log table](https://github.com/geekidea/update-record/blob/master/sql/mysql-upload-record.sql)

### 2. update-record dependency into the project
- 目前最新版是 1.0-SNAPSHOT 快照版本
```xml
<dependency>
    <groupId>io.geekidea.spring.boot</groupId>
    <artifactId>update-record-mybatis-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 3. Config Bean
```java
@Configuration
public class UpdateRecordConfig {

    @Bean
    public UpdateRecordMybatisInterceptor updateRecordMybatisInterceptor(){
        return new UpdateRecordMybatisInterceptor();
    }

}
```

### 4. Create Aop
- Specify which controllers request modification logs
```java
@Slf4j
@Aspect
@Component
public class UpdateRecordAop extends UpdateRecordAopSupport {

    /**
     * 切点
     */
    private static final String POINTCUT = "execution(public * io.geekidea.updaterecord..controller..*.*(..))";

    /**
     * 方法执行之前
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before(POINTCUT)
    @Override
    protected void doBefore(JoinPoint joinPoint) throws Throwable {
        super.doBeforeHandle(joinPoint);
    }

    /**
     * 方法正常执行并成功返回
     *
     * @param joinPoint
     * @param result
     * @throws Throwable
     */
    @AfterReturning(value = POINTCUT, returning = RESULT)
    @Override
    protected void doAfterReturning(JoinPoint joinPoint, Object result) throws Throwable {
        super.doAfterReturningHandle(joinPoint, result);
    }

    /**
     * 异常处理
     *
     * @param exception
     * @throws Throwable
     */
    @AfterThrowing(value = POINTCUT, throwing = EXCEPTION)
    @Override
    protected void doAfterThrowing(Throwable exception) throws Throwable {
        super.doAfterThrowingHandle(exception);
    }

    @Override
    protected String getToken(HttpServletRequest request) {
        return super.getToken(request);
    }

    @Override
    protected void handleUpdateRecord(UpdateRecordLog updateRecordLog, List<UpdateRecordTableLog> updateRecordTableLogs, Set<UpdateRecordColumnLog> updateRecordColumnLogs) {
        // 需自己完善当前登陆用户ID和用户名称，IP对应的区域，备注信息等
        updateRecordLog.setUserId("1");
        updateRecordLog.setUserName("admin");
        updateRecordLog.setArea("上海");
        updateRecordLog.setRemark("update...");
    }

    @Async
    @Override
    protected void save(UpdateRecordLog updateRecordLog, List<UpdateRecordTableLog> updateRecordTableLogs, Set<UpdateRecordColumnLog> updateRecordColumnLogs) {
        super.save(updateRecordLog, updateRecordTableLogs, updateRecordColumnLogs);
    }
}
```

### 5. controller类和方法上使用注解
```java
@UpdateRecord(module = "user")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/pay")
    @UpdateRecord("支付")
    public String pay(@RequestParam Long userId, @RequestParam BigDecimal money) throws Exception{
        accountService.pay(userId,money);
        return "支付成功";
    }

}
```
### 6. 实体类和字段上使用注解
```java
@Data
@Accessors(chain = true)
@UpdateRecordTable(name = "tb_account", value = "账户")
public class Account implements Serializable {

    private static final long serialVersionUID = -2852768164693452983L;

    @UpdateRecordId
    private Long id;

    /**
     * 用户ID
     */
    @UpdateRecordColumn(name = "user_id",value = "用户ID")
    private Long userId;

    /**
     * 余额
     */
    @UpdateRecordColumn("余额")
    private BigDecimal balance;

    /**
     * 用于记录版本
     */
    @UpdateRecordVersion
    private Integer version;

    @UpdateRecordColumn
    private Date createTime;

    private Date updateTime;

}
```

### 7. 数据库记录信息
```text
+--------------------------------+
| update_record_column_log       |
| update_record_log              |
| update_record_table_log        |
+--------------------------------+
```

- update_record_log 某个请求的修改日志记录
```text
mysql> select * from update_record_log;
+-------------------+----------------------------------+---------+-----------+--------+-----------+--------+-----------------------------------+--------------+----------------------------+-------------+---------------------------------------------+-------------------+-------------+----------------+-------+----------------------+--------------------------------------------------------------------------+--------------------------------------------------------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+------------------------------------------------------------------------------------------------------+-------------+--------------+-----------------+--------------------+--------------------+-----------+---------+---------------------+-------------+
| id                | commit_id                        | user_id | user_name | name   | ip        | area   | path                              | url          | server_name                | module_name | package_name                                | class_name        | method_name | request_method | token | thread_name          | before_all_value                                                         | after_all_value                                                          | diff_all_value                                                                                                                                                                            | update_all_desc                                                                                      | table_total | column_total | add_model_total | update_model_total | delete_model_total | remark    | version | create_time         | update_time |
+-------------------+----------------------------------+---------+-----------+--------+-----------+--------+-----------------------------------+--------------+----------------------------+-------------+---------------------------------------------+-------------------+-------------+----------------+-------+----------------------+--------------------------------------------------------------------------+--------------------------------------------------------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+------------------------------------------------------------------------------------------------------+-------------+--------------+-----------------+--------------------+--------------------+-----------+---------+---------------------+-------------+
| 98486541914996744 | 73bde3352ce14d0b8252ac303c2becc4 | 1       | admin     | 支付   | 127.0.0.1 | 上海   | http://localhost:9000/account/pay | /account/pay | spring-boot-mybatis-sample | user        | io.geekidea.updaterecord.samples.controller | AccountController | pay         | GET            | NULL  | http-nio-9000-exec-6 | [{"balance":"856.00","update_time":"2019-12-02 20:48:39","version":154}] | [{"balance":"855.00","update_time":"2019-12-02 20:49:04","version":154}] | {"table-0-tb_account-id-1":[{"name":"balance","before":"856.00","after":"855.00","mode":2},{"name":"update_time","before":"2019-12-02 20:48:39","after":"2019-12-02 20:49:04","mode":2}]} | 余额 由 856.00 修改为 855.00
update_time 由 2019-12-02 20:48:39 修改为 2019-12-02 20:49:04           |           1 |            2 |               0 |                  2 |                  0 | update... | v1.0    | 2019-12-02 20:49:05 | NULL        |
+-------------------+----------------------------------+---------+-----------+--------+-----------+--------+-----------------------------------+--------------+----------------------------+-------------+---------------------------------------------+-------------------+-------------+----------------+-------+----------------------+--------------------------------------------------------------------------+--------------------------------------------------------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+------------------------------------------------------------------------------------------------------+-------------+--------------+-----------------+--------------------+--------------------+-----------+---------+---------------------+-------------+
1 row in set (0.00 sec)
```

<table border="1" style="border-collapse:collapse">
<tr><th>id</th><th>commit_id</th><th>server_name</th><th>module_name</th><th>method_id</th><th>table_name</th><th>table_desc</th><th>entity_name</th><th>id_column_name</th><th>id_property_name</th><th>id_value</th><th>before_value</th><th>after_value</th><th>diff_value</th><th>before_version</th><th>after_version</th><th>update_desc</th><th>total</th><th>add_mode_count</th><th>update_mode_count</th><th>delete_mode_count</th><th>remark</th><th>version</th><th>create_time</th><th>update_time</th></tr>
<tr><td>98486541914996745</td><td>73bde3352ce14d0b8252ac303c2becc4</td><td>spring-boot-mybatis-sample</td><td>user</td><td>io.geekidea.updaterecord.samples.mapper.AccountMapper.updateById</td><td>tb_account</td><td>账户</td><td>Account</td><td>id</td><td>id</td><td>1</td><td>{&quot;balance&quot;:&quot;856.00&quot;,&quot;update_time&quot;:&quot;2019-12-02 20:48:39&quot;,&quot;version&quot;:154}</td><td>{&quot;balance&quot;:&quot;855.00&quot;,&quot;update_time&quot;:&quot;2019-12-02 20:49:04&quot;,&quot;version&quot;:154}</td><td>[{&quot;name&quot;:&quot;balance&quot;,&quot;before&quot;:&quot;856.00&quot;,&quot;after&quot;:&quot;855.00&quot;,&quot;mode&quot;:2},{&quot;name&quot;:&quot;update_time&quot;,&quot;before&quot;:&quot;2019-12-02 20:48:39&quot;,&quot;after&quot;:&quot;2019-12-02 20:49:04&quot;,&quot;mode&quot;:2}]</td><td>154</td><td>155</td><td>余额 由 856.00 修改为 855.00<br/>update_time 由 2019-12-02 20:48:39 修改为 2019-12-02 20:49:04</td><td>2</td><td>0</td><td>2</td><td>0</td><td>NULL</td><td>v1.0</td><td>2019-12-02 20:49:05</td><td>NULL</td></tr></table>

- update_record_table_log 请求对应的表修改记录
```text
mysql> select * from update_record_table_log;
+-------------------+----------------------------------+----------------------------+-------------+------------------------------------------------------------------+------------+------------+-------------+----------------+------------------+----------+------------------------------------------------------------------------+------------------------------------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------+----------------+---------------+------------------------------------------------------------------------------------------------------+-------+----------------+-------------------+-------------------+--------+---------+---------------------+-------------+
| id                | commit_id                        | server_name                | module_name | method_id                                                        | table_name | table_desc | entity_name | id_column_name | id_property_name | id_value | before_value                                                           | after_value                                                            | diff_value                                                                                                                                                    | before_version | after_version | update_desc                                                                                          | total | add_mode_count | update_mode_count | delete_mode_count | remark | version | create_time         | update_time |
+-------------------+----------------------------------+----------------------------+-------------+------------------------------------------------------------------+------------+------------+-------------+----------------+------------------+----------+------------------------------------------------------------------------+------------------------------------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------+----------------+---------------+------------------------------------------------------------------------------------------------------+-------+----------------+-------------------+-------------------+--------+---------+---------------------+-------------+
| 98486541914996745 | 73bde3352ce14d0b8252ac303c2becc4 | spring-boot-mybatis-sample | user        | io.geekidea.updaterecord.samples.mapper.AccountMapper.updateById | tb_account | 账户       | Account     | id             | id               | 1        | {"balance":"856.00","update_time":"2019-12-02 20:48:39","version":154} | {"balance":"855.00","update_time":"2019-12-02 20:49:04","version":154} | [{"name":"balance","before":"856.00","after":"855.00","mode":2},{"name":"update_time","before":"2019-12-02 20:48:39","after":"2019-12-02 20:49:04","mode":2}] | 154            | 155           | 余额 由 856.00 修改为 855.00
update_time 由 2019-12-02 20:48:39 修改为 2019-12-02 20:49:04           |     2 |              0 |                 2 |                 0 | NULL   | v1.0    | 2019-12-02 20:49:05 | NULL        |
+-------------------+----------------------------------+----------------------------+-------------+------------------------------------------------------------------+------------+------------+-------------+----------------+------------------+----------+------------------------------------------------------------------------+------------------------------------------------------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------+----------------+---------------+------------------------------------------------------------------------------------------------------+-------+----------------+-------------------+-------------------+--------+---------+---------------------+-------------+
1 row in set (0.00 sec)
```

- update_record_column_log 请求对应的表中的列修改记录
```text
mysql> select * from update_record_column_log;
+-------------------+----------------------------------+------------+----------+-------------+-------------+---------------------+---------------------+------+----------------+---------------+--------+---------+---------------------+-------------+
| id                | commit_id                        | table_name | id_value | column_name | column_desc | before_value        | after_value         | mode | before_version | after_version | remark | version | create_time         | update_time |
+-------------------+----------------------------------+------------+----------+-------------+-------------+---------------------+---------------------+------+----------------+---------------+--------+---------+---------------------+-------------+
| 98486541914996746 | 73bde3352ce14d0b8252ac303c2becc4 | tb_account | 1        | balance     | 余额        | 856.00              | 855.00              |    2 | 154            | 155           | NULL   | v1.0    | 2019-12-02 20:49:05 | NULL        |
| 98486541914996747 | 73bde3352ce14d0b8252ac303c2becc4 | tb_account | 1        | update_time | update_time | 2019-12-02 20:48:39 | 2019-12-02 20:49:04 |    2 | 154            | 155           | NULL   | v1.0    | 2019-12-02 20:49:05 | NULL        |
+-------------------+----------------------------------+------------+----------+-------------+-------------+---------------------+---------------------+------+----------------+---------------+--------+---------+---------------------+-------------+
2 rows in set (0.00 sec)
```


