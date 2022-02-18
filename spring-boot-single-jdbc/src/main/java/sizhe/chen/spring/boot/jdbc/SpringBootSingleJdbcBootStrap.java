package sizhe.chen.spring.boot.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;


/**
 * @Author: sizhe.chen
 * @Date: Create in 9:20 上午 2022/2/18
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
public class SpringBootSingleJdbcBootStrap implements CommandLineRunner {
    @Autowired
    private DataSource dataSource;
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSingleJdbcBootStrap.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        Connection connection = dataSource.getConnection();
        System.out.println(dataSource);
        System.out.println(connection);
        connection.close();
    }
}
