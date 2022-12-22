package com.techelevator.tebucks;

import com.techelevator.tebucks.service.IrsAuthenticationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TEBucksApplication {


    public static void main(String[] args) {

        SpringApplication.run(TEBucksApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("https://tebucks.netlify.app");
                registry.addMapping("/*").allowedOrigins("https://tebucks.netlify.app");
                registry.addMapping("/transfers/*").allowedOrigins("https://tebucks.netlify.app");
                registry.addMapping("/api/**").allowedOrigins("https://tebucks.netlify.app");
                registry.addMapping("/api/transfers/*").allowedOrigins("https://tebucks.netlify.app");
            }
        };
    }

}
