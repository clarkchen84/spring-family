package sizhe.chen.jdbc.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @Author: sizhe.chen
 * @Date: Create in 11:56 上午 2022/2/20
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@Slf4j
public class ProgramingTransactionDemo implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ProgramingTransactionDemo.class,args);
    }

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate template;

    @Override
    public void run(String... args) throws Exception {
        log.info("COUNT BEFORE TRANSACTION ");
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                template.update("insert into foo(id,bar) values(1,'aaa')");
                log.info("COUNT IN TRANSACTION:{} " , getCount());
                transactionStatus.setRollbackOnly();
            }
        });
        log.info("COUNT AFTER TRANSACTION:{} " , getCount());

    }

    private long getCount(){
        return (long) template.queryForList("SELECT COUNT(*) AS CNT FROM FOO").get(0).get("CNT");
    }
}
