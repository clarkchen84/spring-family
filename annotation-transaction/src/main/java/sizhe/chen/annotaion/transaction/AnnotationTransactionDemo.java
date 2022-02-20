package sizhe.chen.annotaion.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import sizhe.chen.annotaion.transaction.exception.RollbackException;
import sizhe.chen.annotaion.transaction.service.FooService;

/**
 * @Author: sizhe.chen
 * @Date: Create in 1:32 下午 2022/2/20
 * @Description:
 * @Modified:
 * @Version:
 */

@SpringBootApplication
@Slf4j
public class AnnotationTransactionDemo implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FooService fooService;
    public static void main(String[] args) {
        SpringApplication.run(AnnotationTransactionDemo.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        fooService.insertRecord();
        log.info("AAA {} :" ,jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR ='aaa'",Long.class) );
        try {
            fooService.insertThenRollBack();
        }catch (RollbackException e){
            log.info("BBB {} :" ,jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR ='BBB'",Long.class) );
        }
        try {
            fooService.invokeInsertThenRollBack();
        }catch (RollbackException e){
            log.info("BB {} :" ,jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR ='BBB'",Long.class) );
        }
    }
}
