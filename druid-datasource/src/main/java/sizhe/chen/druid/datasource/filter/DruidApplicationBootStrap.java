package sizhe.chen.druid.datasource.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:49 下午 2022/2/18
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@Slf4j
public class DruidApplicationBootStrap implements CommandLineRunner {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DruidApplicationBootStrap.class,args);
    }


    @Override
    public void run(String... args) throws Exception {
        log.info(dataSource.toString());
        log.info(dataSource.getConnection().toString());
    }
}
