package sizhe.chen.spring.data;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Properties;

/**
 * @Author: sizhe.chen
 * @Date: Create in 9:29 下午 2022/2/13
 * @Description:
 * @Modified:
 * @Version:
 */
@Configuration
@EnableTransactionManagement
public class DataSourceDemo {

    @Autowired
    private DataSource myDataSource;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DataSourceDemo.class);
        context.refresh();
        showBeans(context);
        showDataSourceDemo(context);
        context.close();
    }
    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("driverClassName","org.h2.Driver");
        properties.setProperty("url","jdbc:h2:mem:testdb");
        properties.setProperty("username","sa");
        return BasicDataSourceFactory.createDataSource(properties);
    }

    @Bean
    public PlatformTransactionManager transactionManager( DataSource dataSource){
        return  new DataSourceTransactionManager(dataSource);
    }

    private static void showBeans(ApplicationContext context){
        System.out.println("bears: " +Arrays.toString(context.getBeanDefinitionNames()));
    }

    private static void showDataSourceDemo(ApplicationContext context){

        DataSourceDemo dataSourceDemo = context.getBean("dataSourceDemo",DataSourceDemo.class);
        System.out.println("Datasource: "+dataSourceDemo.myDataSource);
    }


}
