package fudan.se.project;

import fudan.se.project.domain.Authority;
import fudan.se.project.domain.User;
import fudan.se.project.repository.AuthorityRepository;
import fudan.se.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * This is a function to create some basic entities when the application starts.
     * Now we are using a In-Memory database, so you need it.
     * You can change it as you like.
     */
    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // Create authorities if not exist.
                Authority adminAuthority = getOrCreateAuthority("Admin", authorityRepository);
                Authority contributorAuthority = getOrCreateAuthority("Contributor", authorityRepository);
                Authority reviewerAuthority = getOrCreateAuthority("Reviewer", authorityRepository);

                // Create an admin if not exists.
                if (userRepository.findByUsername("admin") == null) {
                    User admin = new User(
                            "admin",
                            passwordEncoder.encode("password"),
                            "libowen",
                            new HashSet<>(Collections.singletonList(adminAuthority))
                    );
                    userRepository.save(admin);
                }
            }

            private Authority getOrCreateAuthority(String authorityText, AuthorityRepository authorityRepository) {
                Authority authority = authorityRepository.findByAuthority(authorityText);
                if (authority == null) {
                    authority = new Authority(authorityText);
                    authorityRepository.save(authority);
                }
                return authority;
            }
        };
    }
}

