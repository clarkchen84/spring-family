package sizhe.chen.springboot.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author: sizhe.chen
 * @Date: Create in 9:01 下午 2022/2/13
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@Slf4j
public class DataSourceDemoBootStrap implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DataSourceDemoBootStrap.class,args);
    }
    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        showConnection();
    }

    private void showConnection() throws SQLException {
        log.info("user log:" + dataSource.toString());
        Connection connection = dataSource.getConnection();
        log.info("user log:" + connection.toString());
        connection.close();
    }
}
