package fudan.se.project;

import fudan.se.project.domain.Authority;
import fudan.se.project.domain.User;
import fudan.se.project.repository.AuthorityRepository;
import fudan.se.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
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
    PlatformTransactionManager transactionManager(DataSourceProperties dataSourceProperties) {
        return new DataSourceTransactionManager(datasource(dataSourceProperties));
    }

    @Bean
    DataSource datasource(DataSourceProperties dataSourceProperties) {
        return new DriverManagerDataSource(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
    }
}

