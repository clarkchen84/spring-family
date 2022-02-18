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

   