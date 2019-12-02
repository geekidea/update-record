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

#### update_record_log 某个请求的修改日志记录

| id | commit\_id | user\_id | user\_name | name | ip | area | path | url | server\_name | module\_name | package\_name | class\_name | method\_name | request\_method | token | thread\_name | before\_all\_value | after\_all\_value | diff\_all\_value | update\_all\_desc | table\_total | column\_total | add\_model\_total | update\_model\_total | delete\_model\_total | remark | version | create\_time | update\_time |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 98486541914996744 | 73bde3352ce14d0b8252ac303c2becc4 | 1 | admin | 支付 | 127.0.0.1 | 上海 | http://localhost:9000/account/pay | /account/pay | spring-boot-mybatis-sample | user | io.geekidea.updaterecord.samples.controller | AccountController | pay | GET | NULL | http-nio-9000-exec-6 | \[{"balance":"856.00","update\_time":"2019-12-02 20:48:39","version":154}\] | \[{"balance":"855.00","update\_time":"2019-12-02 20:49:04","version":154}\] | {"table-0-tb\_account-id-1":\[{"name":"balance","before":"856.00","after":"855.00","mode":2},{"name":"update\_time","before":"2019-12-02 20:48:39","after":"2019-12-02 20:49:04","mode":2}\]} | 余额 由 856.00 修改为 855.00<br/>update\_time 由 2019-12-02 20:48:39 修改为 2019-12-02 20:49:04 | 1 | 2 | 0 | 2 | 0 | update... | v1.0 | 2019-12-02 20:49:05 | NULL |

#### update_record_table_log 请求对应的表修改记录

| id | commit\_id | server\_name | module\_name | method\_id | table\_name | table\_desc | entity\_name | id\_column\_name | id\_property\_name | id\_value | before\_value | after\_value | diff\_value | before\_version | after\_version | update\_desc | total | add\_mode\_count | update\_mode\_count | delete\_mode\_count | remark | version | create\_time | update\_time |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 98486541914996745 | 73bde3352ce14d0b8252ac303c2becc4 | spring-boot-mybatis-sample | user | io.geekidea.updaterecord.samples.mapper.AccountMapper.updateById | tb\_account | 账户 | Account | id | id | 1 | {"balance":"856.00","update\_time":"2019-12-02 20:48:39","version":154} | {"balance":"855.00","update\_time":"2019-12-02 20:49:04","version":154} | \[{"name":"balance","before":"856.00","after":"855.00","mode":2},{"name":"update\_time","before":"2019-12-02 20:48:39","after":"2019-12-02 20:49:04","mode":2}\] | 154 | 155 | 余额 由 856.00 修改为 855.00<br/>update\_time 由 2019-12-02 20:48:39 修改为 2019-12-02 20:49:04 | 2 | 0 | 2 | 0 | NULL | v1.0 | 2019-12-02 20:49:05 | NULL |

#### update_record_column_log 请求对应的表中的列修改记录

| id | commit\_id | table\_name | id\_value | column\_name | column\_desc | before\_value | after\_value | mode | before\_version | after\_version | remark | version | create\_time | update\_time |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 98486541914996746 | 73bde3352ce14d0b8252ac303c2becc4 | tb\_account | 1 | balance | 余额 | 856.00 | 855.00 | 2 | 154 | 155 | NULL | v1.0 | 2019-12-02 20:49:05 | NULL |
| 98486541914996747 | 73bde3352ce14d0b8252ac303c2becc4 | tb\_account | 1 | update\_time | update\_time | 2019-12-02 20:48:39 | 2019-12-02 20:49:04 | 2 | 154 | 155 | NULL | v1.0 | 2019-12-02 20:49:05 | NULL |



