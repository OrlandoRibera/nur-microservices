package cateringapi;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import annotations.Generated;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {
		"controllers",
		"infrastructure.repositories",
		"infrastructure.eventhub",
		"infrastructure.outbox",
		"infrastructure.config",
		"command",
		"publisher",
		"dto",
		"mappers"
})
@EntityScan("infrastructure.model")
@EnableJpaRepositories(basePackages = { "infrastructure.repositories" })
@EnableTransactionManagement
@EnableScheduling
@Generated
public class CateringApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(CateringApiApplication.class, args);
	}

	@Bean
	Pipeline pipeline(
			ObjectProvider<Command.Handler> commandHandlers,
			ObjectProvider<Notification.Handler> notificationHandlers,
			ObjectProvider<Command.Middleware> middlewares) {
		return new Pipelinr()
				.with(() -> commandHandlers.stream())
				.with(() -> notificationHandlers.stream())
				.with(() -> middlewares.stream());
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Catering Microservice API")
						.version("1.0.0")
						.description(
								"Microservice responsible for managing food packages, cooking, packaging, and dispatching meals")
						.contact(new Contact()
								.name("Catering Team")
								.email("catering@example.com")
								.url("https://github.com/OrlandoRibera/nur-microservices"))
						.license(new License()
								.name("MIT License")
								.url("https://opensource.org/licenses/MIT")))
				.servers(List.of(
						new Server().url("https://catering-guedavfugtbtdmbj.brazilsouth-01.azurewebsites.net")
								.description("Production Server")));
	}
}
