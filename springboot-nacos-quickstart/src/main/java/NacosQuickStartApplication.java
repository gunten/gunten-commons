import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gunten
 * 2022/11/20
 */
@SpringBootApplication
@NacosPropertySource(dataId = "example", autoRefreshed = true)
public class NacosQuickStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosQuickStartApplication.class, args);
    }
}
