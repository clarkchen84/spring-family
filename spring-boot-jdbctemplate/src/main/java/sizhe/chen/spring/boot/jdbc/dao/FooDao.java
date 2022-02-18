package sizhe.chen.spring.boot.jdbc.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import sizhe.chen.spring.boot.jdbc.domain.Foo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 4:37 下午 2022/2/18
 * @Description:
 * @Modified:
 * @Version:
 */
@Slf4j
@Repository
public class FooDao {
    @Autowired
    private JdbcTemplate jdbcTemplate ;
    @Autowired
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void insertBatch(){
        jdbcTemplate.batchUpdate("INSERT INTO FOO(BAR) VALUES(?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setString(1, "b-" + i);
                    }

                    @Override
                    public int getBatchSize() {
                        return 2;
                    }
                });
        List<Foo> list = new ArrayList<>();
        list.add(Foo.builder().id(100L).bar("b-100").build());
        list.add(Foo.builder().id(101L).bar("b-101").build());
        namedParameterJdbcTemplate.batchUpdate("INSERT INTO FOO(id,BAR) VALUES(:id,:bar)",SqlParameterSourceUtils.createBatch(list));

    }

    public void insertData(){
        Arrays.asList("b","c").forEach(bar ->{
            jdbcTemplate.update("INSERT INTO FOO (BAR) VALUES (?)",bar);
        });

        HashMap<String,String> row= new HashMap<>();
        row.put("BAR","d");
        Number id = simpleJdbcInsert.executeAndReturnKey(row);
        log.info("id of d: {}",id.longValue());
    }

    public void listData(){
        log.info("Count: {}",jdbcTemplate.queryForObject("SELECT COUNT(*)" +
                " FROM FOO ",Long.class));
        List<String> bars = jdbcTemplate.queryForList("SELECT Bar" +
                " FROM FOO ",String.class);
        bars.forEach( bar -> log.info("bar :{} "  ,bar));

        List<Foo> list = jdbcTemplate.query("SELECT * FROM FOO", new RowMapper<Foo>() {
            @Override
            public Foo mapRow(ResultSet resultSet, int i) throws SQLException {
                return Foo.builder().id(resultSet.getLong(1)).
                        bar(resultSet.getString(2)).build();
            }
        });
        list.forEach(foo -> log.info("foo info {}",foo));
    }
}
