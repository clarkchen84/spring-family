package sizhe.chen.annotaion.transaction.service;

import org.springframework.transaction.UnexpectedRollbackException;
import sizhe.chen.annotaion.transaction.exception.RollbackException;

/**
 * @Author: sizhe.chen
 * @Date: Create in 1:24 下午 2022/2/20
 * @Description:
 * @Modified:
 * @Version:
 */

public interface FooService {
    public void insertRecord();

    public void insertThenRollBack() throws RollbackException;

    public void invokeInsertThenRollBack() throws RollbackException;

}
