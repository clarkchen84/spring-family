### spring 中的数据库操作

#### spring 数据源的相关配置
1. 通用
   * spring.datasource.url=jdbc:mysql://localhost/test
   * spring.datasource.username=dbuser
   * spring.datasource.password=dbpass
   * spring.datasrouce.driver-class=com.mysql.jdbc.Driver
2. 初始化内嵌数据库
   * spring.datasource.initialization-mode=embedded|always|never
   * spring.datasource.schema与spring.datasource.data确定初始化SQL文件
   * spring.datasource.platform=hsqldb|h2|oracle|mysql|postgreql



#### spring 中的多数据源配置

1. 与SpringBoot协同工作 行哦啊，下面两种方法    

   * 配置@Primary类型的Bean

   * 排除springboot 的自动配置

     * DataSourceAutoConfiguration
     * DataSouceTransactionManagerAutoConfiguration
     * JdbcTemplateAutoConfiguration

     ``` java
     @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
             DataSourceTransactionManagerAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
     @Slf4j
     public class MultDatasourceBootStrap implements CommandLineRunner {
         @Autowired
         @Qualifier("fooDatasource")
         private DataSource fooDataSource;
         @Autowired
         @Qualifier("barDatasource")
         private DataSource barDatasource;
     
         @Bean
         @ConfigurationProperties("foo.datasource")
         public DataSourceProperties fooDatasourceProperties(){
             return new DataSourceProperties();
         }
         @Bean
         @ConfigurationProperties("bar.datasource")
         public DataSourceProperties barDatasourceProperties(){
             return new DataSourceProperties();
         }
     
         @Bean
         public DataSource fooDatasource(     @Autowired @Qualifier("fooDatasourceProperties")DataSourceProperties dataSourceProperties ){
             return dataSourceProperties.initializeDataSourceBuilder().build();
     
         }
         @Bean
         public DataSource barDatasource(@Autowired @Qualifier("barDatasourceProperties")DataSourceProperties dataSourceProperties ){
     
             return dataSourceProperties.initializeDataSourceBuilder().build();
         }
     
         @Bean
         @Autowired
         public PlatformTransactionManager fooTransactionManager( @Qualifier("fooDatasource")DataSource dataSource){
             return new DataSourceTransactionManager(dataSource);
         }
         @Bean
         @Autowired
         public PlatformTransactionManager barTransactionManager(  @Qualifier("barDatasource")DataSource dataSource){
             return new DataSourceTransactionManager(dataSource);
         }
     
     ```

     

2. Hikari Datasource 的常用配置

   1. spring.datasource.hikari.maximumPoolSize=10
   2. spring.datasource.hikari.minimumIdle=10
   3. spring.datasource.hikari.idleTimeout=600000
   4. spring.datasource.hikari.connectionTimeout=30000
   5. spring.datasource.hikari.maxLifetime=1800000

3. Druid Datasource 的一般配置

   ``` properties
   #application.properties
   spring.output.ansi.enabled=ALWAYS
   spring.datasource.url=jdbc:h2:mem:foo
   spring.datasource.username=sa
   spring.datasource.password=
   # 启用ConfigFilter
   spring.datasource.druid.filter.config.enabled=true
   spring.datasource.druid.initial-size=5
   spring.datasource.druid.max-active=5
   spring.datasource.druid.min-idle=5
   spring.datasource.druid.filters=conn,config,stat,slf4j
   ```

   ``` properties
   #META-INF/druid-filter.properties
   druid.filters.conn=sizhe.chen.druid.datasource.filter.ConnectionLogFilter
   ```

   ``` java
   @Slf4j
   public class ConnectionLogFilter extends FilterEventAdapter {
       @Override
       public void connection_connectBefore(FilterChain chain, Properties info) {
           log.info("before connected");
       }
   
       @Override
       public void connection_connectAfter(ConnectionProxy connection) {
           log.info("after connected");
       }
   }
   ```
#### 事物的传播特性
| 传播性                    | 值   | 描述                                                         | 备注                                                         |
| ------------------------- | ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| PROPERGATION_REQUIRED     | 0    | 当前有事物就用当前的，没有就用新的                           | 默认的传播特性，如果没有，就开启一个事务；如果有，就加入当前事务（方法B看到自己已经运行在 方法A的事务内部，就不再起新的事务，直接加入方法A） |
| PROPERGATION_SUPPORTS     | 1    | 事物可有可无不是必须的                                       |                                                              |
| PROPERGATION_MANDARY      | 2    | 当前一定要有事物，没有就抛出异常                             |                                                              |
| PROPERGATION_REQUIRES_NEW | 3    | 无论有无事物，都要起一个新的事物                             | 如果没有，就开启一个事务；如果有，就将当前事务挂起。（方法A所在的事务就会挂起，方法B会起一个新的事务，等待方法B的事务完成以后，方法A才继续执行） |
| PROPERGATION_NOT_SUPPORT  | 4    | 不支持事物，按没有事物的方式运行                             |                                                              |
| PROPERGATION_NEVER        | 5    | 不支持事物，如果有事物就抛出异常                             |                                                              |
| PROPERGATION_NESTED       | 6    | 当前有事物，就在当前事物里再起一个新的事物\|里面事物的回滚，不会影响外面的事物 |                                                              |


1. serviceA 和 serviceB 都声明了事务，默认情况下，propagation=PROPAGATION_REQUIRED，整个service调用过程中，只存在一个共享的事务，当有任何异常发生的时候，所有操作回滚。
    ``` java
    @Transactional

    public void service(){

    serviceA();

    serviceB();

    }

    @Transactional

    serviceA();

    @Transactional

    serviceB();
    ```
2. PROPAGATION_SUPPORTS,由于serviceA运行时没有事务，这时候，如果底层数据源defaultAutoCommit=true，那么sql1是生效的，如果defaultAutoCommit=false，那么sql1无效，如果service有@Transactional标签，serviceA共用service的事务(不再依赖defaultAutoCommit)，此时，serviceA全部被回滚。
    ``` java
        public void service(){

            serviceA();

            throw new RunTimeException();

        }

        @Transactional(propagation=Propagation.SUPPORTS)

        serviceA();

        serviceA执行时当前没有事务，所以service中抛出的异常不会导致 serviceA回滚。

        再看一个小例子，代码如下：

        public void service(){

        serviceA();

        }

        @Transactional(propagation=Propagation.SUPPORTS)

        serviceA(){

        do sql 1

        1/0;

        do sql 2

        }
    ```
3. PROPAGATION_MANDATORY,这种情况执行 service会抛出异常，如果defaultAutoCommit=true，则serviceB是不会回滚的，defaultAutoCommit=false，则serviceB执行无效。
    ``` java 
    public void service(){

    serviceB();

    serviceA();

    }

    serviceB(){

    do sql

    }

    @Transactional(propagation=Propagation.MANDATORY)

    serviceA(){

    do sql

    }

    ```
4. PROPAGATN_REQUIRES_NEW 说明：如果当前存在事务，先把当前事务相关内容封装到一个实体，然后重新创建一个新事务，接受这个实体为参数，用于事务的恢复。更直白的说法就是暂停当前事务(当前无事务则不需要)，创建一个新事务。 针对这种情况，两个事务没有依赖关系，可以实现新事务回滚了，但外部事务继续执行。
    * 当调用service接口时，由于serviceA使用的是REQUIRES_NEW，它会创建一个新的事务，但由于serviceA抛出了运行时异常，导致serviceA整个被回滚了，而在service方法中，捕获了异常，所以serviceB是正常提交的。 注意，service中的try … catch 代码是必须的，否则service也会抛出异常，导致serviceB也被回滚。
    ``` java
    @Transactional

    public void service(){

    serviceB();

    try{

    serviceA();

    }catch(Exception e){

    }

    }

    serviceB(){

    do sql

    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)

    serviceA(){

    do sql 1

    1/0;

    do sql 2

    }
    ```
5. Propagation.NOT_SUPPORTED 说明：如果当前存在事务，挂起当前事务，然后新的方法在没有事务的环境中执行，没有spring事务的环境下，sql的提交完全依赖于 defaultAutoCommit属性值 。
    ``` java
       @Transactional
       // 当调用service方法的时候，执行到serviceA方法中的1/0代码时，抛出了异常，
       //由于.serviceA处于无事务环境下，所以 sql1是否生效取决于defaultAutoCommit的值，
       //当defaultAutoCommit=true时，sql1是生效的，但是service由于抛出了异常，
       //所以serviceB会被回滚。
        public void service(){
    
        serviceB();
    
        serviceA();
    
        }
    
        serviceB(){
    
        do sql
    
        }
    
        @Transactional(propagation=Propagation.NOT_SUPPORTED)
    
        serviceA(){
    
        do sql 1
    
        1/0;
    
        do sql 2
    
        }
    
    ```
6.  PROPAGATION_NEVER说明： 如果当前存在事务，则抛出异常，否则在无事务环境上执行代码。

    ``` java 

    public void service(){

    serviceB();

    serviceA();

    }

    serviceB(){

    do sql

    }

    @Transactional(propagation=Propagation.NEVER)

    serviceA(){

    do sql 1

    1/0;

    do sql 2

    }

    //上面的示例调用service后，若defaultAutoCommit=true，
    //则serviceB方法及serviceA中的sql1都会生效。
    ```
7. PROPAGATION_NESTED说明： 如果当前存在事务，则使用 SavePoint 技术把当前事务状态进行保存，然后底层共用一个连接，当NESTED内部出错的时候，自行回滚到 SavePoint这个状态，只要外部捕获到了异常，就可以继续进行外部的事务提交，而不会受到内嵌业务的干扰，但是，如果外部事务抛出了异常，整个大事务都会回滚。注意： spring配置事务管理器要主动指定 nestedTransactionAllowed=true，如下所示：
    ``` xml
    <bean id=“dataTransactionManager”

    class=“org.springframework.jdbc.datasource.DataSourceTransactionManager”>

    <property name=“dataSource” ref=“dataDataSource” />

    <property name=“nestedTransactionAllowed” value=“true” />

    </bean>
    ```
    ``` java

    @Transactional

    public void service(){

    serviceA();

    try{

    serviceB();

    }catch(Exception e){

    }

    }

    serviceA(){

    do sql

    }

    @Transactional(propagation=Propagation.NESTED)

    serviceB(){

    do sql1

    1/0;

    do sql2

    }
    //sserviceB是一个内嵌的业务，内部抛出了运行时异常，
    //所以serviceB整个被回滚了，由于service捕获了异常，
    //所以serviceA是可以正常提交的。
    ```
   ``` java
    @Transactional
    
    public void service(){
    
    serviceA();
    
    serviceB();
    
    1/0;
    
    }
    
    @Transactional(propagation=Propagation.NESTED)
    
    serviceA(){
    
    do sql
    
    }
    
    serviceB(){
    
    do sql
    
    }
    //由于service抛出了异常，所以会导致整个service方法被回滚。
    //（这就是跟PROPAGATION_REQUIRES_NEW不一样的地方了，
    //NESTED方式下的内嵌业务会受到外部事务的异常而回滚	。）
   ```

#### 数据库事物的隔离特性

| 隔离性                  | 值   | 脏读 | 不可重复读 | 幻读 |
| ----------------------- | ---- | ---- | ---------- | ---- |
| ISOLATION_READ_UNCOMMIT | 1    | T    | T          | T    |
| ISOLATION_READ_COMMIT   | 2    | F    | T          | T    |
| ISOLATION_REPEATED_READ | 3    | F    | F          | T    |
| ISOLATION_SERIALIZABLE  | 4    | F    | F          | F    |

#### 编程式事物

1. TransactionTemplate 
   * TransactionCallBack
   * TransactionCallBackWithOutResult
2. PlatformTransactionManager
   * 可以传入TransactionDefinition进行定义

#### 基于注解的配置方式

1. 开启注解的方式

   1. @EnableTransactionManagement
   2. ``<tx:annotation-driver>``

2. 一些配置

   1. proxyTargetClass
   2. mode
   3. order

3. @Transactional

   * transactionManager
   * propagation
   * isolate
   * timeout
   * readOnly
   * 如何回滚

4. 由于使用注解的方式相当于使用的是代理的方式， 进行调用，如果同一个类内的函数调用，相当于不能使用代理，所以注解也就不生效，参考下面的例子。

   ``` java
       @Override
       @Transactional(rollbackFor = RollbackException.class )
       public void insertThenRollBack() throws RollbackException {
           jdbcTemplate.execute("insert into foo(bar) values('BBB')");
           throw  new RollbackException();
   
       }
   
       /**
        * 由于类的内部调用是没有走到代理方法的，所有没有执行回滚的注解
        * @throws RollbackException
        */
       @Override
       public void invokeInsertThenRollBack() throws RollbackException {
           insertThenRollBack();
       }
   ```

   