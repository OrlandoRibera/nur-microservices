package cateringapi;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import annotations.Generated;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.repositories.FoodRepository;
import infrastructure.repositories.food.FoodJpaRepository;
import infrastructure.repositories.foodpackage.FoodPackageJpaRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = {"controllers", "infrastructure.repositories", "infrastructure.eventhub", "command", "query", "event", "core"})
@EntityScan("infrastructure.model")
@EnableJpaRepositories(basePackages = {"infrastructure.repositories"})
@EnableTransactionManagement
@Generated
public class CateringApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(CateringApiApplication.class, args);
	}

	@Bean(name = "foodRepository")
	public FoodRepository foodRepository() {
		return new FoodJpaRepository();
	}

	@Bean(name = "foodPackageRepository")
	public FoodPackageRepository foodPackageRepository() {
		return new FoodPackageJpaRepository();
	}

	@Bean
	Pipeline pipeline(
		ObjectProvider<Command.Handler> commandHandlers,
		ObjectProvider<Notification.Handler> notificationHandlers,
		ObjectProvider<Command.Middleware> middlewares
	) {
		return new Pipelinr()
			.with(commandHandlers::stream)
			.with(notificationHandlers::stream)
			.with(middlewares::orderedStream);
	}
}
