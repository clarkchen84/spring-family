package sizhe.chen.spring.boot.jdbc.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: sizhe.chen
 * @Date: Create in 4:47 下午 2022/2/18
 * @Description:
 * @Modified:
 * @Version:
 */

@Data
@Builder
public class Foo {
    private Long id;
    private String bar;
}
