package sizhe.chen.annotaion.transaction.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sizhe.chen.annotaion.transaction.exception.RollbackException;
import sizhe.chen.annotaion.transaction.service.FooService;

/**
 * @Author: sizhe.chen
 * @Date: Create in 1:24 下午 2022/2/20
 * @Description:
 * @Modified:
 * @Version:
 */
@Service
@Slf4j
public class FooServiceImpl implements FooService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void insertRecord() {
        jdbcTemplate.execute("insert into foo(bar) values('aaa')");
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class )
    public void insertThenRollBack() throws RollbackException {
        jdbcTemplate.execute("insert into foo(bar) values('BBB')");
        throw  new RollbackException();

    }

    /**
     * 由于类的内部调用是没有走到代理方法的，所有没有执行回滚的注解
     * @throws RollbackException
     */
    @Override
    public void invokeInsertThenRollBack() throws RollbackException {
        insertThenRollBack();
    }
}
