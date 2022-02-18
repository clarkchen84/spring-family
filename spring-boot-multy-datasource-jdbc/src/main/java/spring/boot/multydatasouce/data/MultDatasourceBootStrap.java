package spring.boot.multydatasouce.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:14 上午 2022/2/18
 * @Description:
 * @Modified:
 * @Version:
 */
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




    public static void main(String[] args) {
        SpringApplication.run(MultDatasourceBootStrap.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info(fooDataSource.getConnection().toString());
        log.info(barDatasource.getConnection().toString());
    }
}
