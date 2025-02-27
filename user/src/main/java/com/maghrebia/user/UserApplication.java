package com.maghrebia.user;

import com.maghrebia.user.entity.Role;
import com.maghrebia.user.repository.RoleRepository;
import com.maghrebia.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
    @Bean
    CommandLineRunner initAdminAccount(UserService userService) {
        return args -> {
            userService.createAdmin();
        };
    }
}
