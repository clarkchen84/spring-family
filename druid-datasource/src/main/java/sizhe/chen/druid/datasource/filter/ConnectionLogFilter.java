package sizhe.chen.druid.datasource.filter;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:44 下午 2022/2/18
 * @Description:
 * @Modified:
 * @Version:
 */
@Slf4j
public class ConnectionLogFilter extends FilterEventAdapter {
    @Override
    public void connection_connectBefore(FilterChain chain, Properties info) {
        log.info("before connected");
    }

    @Override
    public void connection_connectAfter(ConnectionProxy connection) {
        log.info("after connected");
    }
}
