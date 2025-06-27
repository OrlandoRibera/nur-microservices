package infrastructure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TestConfiguration
@ComponentScan(basePackages = "infrastructure")
@EntityScan(basePackages = "infrastructure.model")
@EnableJpaRepositories(basePackages = "infrastructure.repositories")
public class TestConfig {
} 