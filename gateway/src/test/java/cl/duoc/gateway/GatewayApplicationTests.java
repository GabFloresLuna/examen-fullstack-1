package cl.duoc.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
    org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration.class,
    org.springframework.cloud.gateway.config.GatewayAutoConfiguration.class
})
class GatewayApplicationTests {
    @Test
    void contextLoads() {}
}
