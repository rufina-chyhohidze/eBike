package be.kdg.integration4;

import be.kdg.integration4.config.DotenvInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Slf4j
@SpringBootApplication
public class StartApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(StartApplication.class)
                .initializers(new DotenvInitializer())
                .run(args);
    }

}
