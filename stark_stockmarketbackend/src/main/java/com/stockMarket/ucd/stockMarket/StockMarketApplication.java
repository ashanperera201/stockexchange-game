package com.stockMarket.ucd.stockMarket;
import akka.actor.ActorSystem;
import com.stockMarket.ucd.stockMarket.config.DefaultProfileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
public class StockMarketApplication {
	private static final Logger log = LoggerFactory.getLogger(StockMarketApplication.class);
	private final Environment env;

	//Akka Actors System Initiation
    public static final ActorSystem system = ActorSystem.create("StockMarketActorSystem");

	//private final InitializeCompanyConfig initializeCompanyConfig;

	public StockMarketApplication(Environment env) {
		this.env = env;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(StockMarketApplication.class);
		DefaultProfileUtil.addDefaultProfile(app);
		Environment env = app.run(args).getEnvironment();
		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}
		try {
			log.info("\n----------------------------------------------------------\n\t" +
							"Application '{}' is running! Access URLs:\n\t" +
							"Local: \t\t{}://localhost:{}\n\t" +
							"External: \t{}://{}:{}\n\t" +
							"Profile(StockMarket_Application): \t{}\n----------------------------------------------------------",
					env.getProperty("spring.application.name"),
					protocol,
					env.getProperty("server.port"),
					protocol,
					InetAddress.getLocalHost().getHostAddress(),
					env.getProperty("server.port"),
					env.getActiveProfiles());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}


	}

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("*");
            }
        };
    }



}