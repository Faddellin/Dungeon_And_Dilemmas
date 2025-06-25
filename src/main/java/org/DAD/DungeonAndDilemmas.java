package org.DAD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
    basePackages = "org.DAD.application.repository",
    repositoryBaseClass = org.DAD.application.repository.repositoryImpl.BaseRepositoryImpl.class
)
@SpringBootApplication
public class DungeonAndDilemmas {
    public static void main(String[] args) {
        SpringApplication.run(DungeonAndDilemmas.class, args);
    }
}