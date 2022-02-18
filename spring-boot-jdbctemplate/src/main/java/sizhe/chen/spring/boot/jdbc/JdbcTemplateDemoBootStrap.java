package sizhe.chen.spring.boot.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import sizhe.chen.spring.boot.jdbc.dao.FooDao;

import javax.sql.DataSource;

@SpringBootApplication
public class JdbcTemplateDemoBootStrap  implements CommandLineRunner {
    @Autowired
    private FooDao fooDao;

    public static void main(String[] args) {
        SpringApplication.run(JdbcTemplateDemoBootStrap.class, args);
    }

    @Bean
    @Autowired
    public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate){
        SimpleJdbcInsert simpleJdbcInsert =new  SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.setTableName("FOO");
        simpleJdbcInsert.setGeneratedKeyName("ID");
        return simpleJdbcInsert;
    }

    @Bean
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return namedParameterJdbcTemplate;

    }


    @Override
    public void run(String... args) throws Exception {
        fooDao.insertData();
        fooDao.insertBatch();
        fooDao.listData();

    }
}
