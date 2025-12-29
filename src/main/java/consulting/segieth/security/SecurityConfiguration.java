package consulting.segieth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;

import consulting.segieth.security.ui.LoginView;

@EnableWebSecurity 
@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure your static resources with public access
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/public/**")
            .permitAll());

        http.oauth2Login(Customizer.withDefaults());
        
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            
            // HIER kommt der Aufruf hin:
            configurer.oauth2LoginPage(
                "/oauth2/authorization/azure", // Einstiegspunkt
                "/login-success"               // Wohin nach Erfolg?
            );
        });        
        /*
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            configurer.oauth2LoginPage(
                        "/oauth2/authorization/github", 
                        "{baseUrl}/session-ended"         
            );
        });
        */
        // Configure Vaadin's security using VaadinSecurityConfigurer
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> { 
            // This is important to register your login view to the
            // navigation access control mechanism:
            configurer.loginView(LoginView.class); 

            // You can add any possible extra configurations of your own
            // here (the following is just an example):
            // configurer.enableCsrfConfiguration(false);
        });

        return http.build();
    }

    /**
     * Demo UserDetailsManager which only provides two hardcoded
     * in memory users and their roles.
     * NOTE: This shouldn't be used in real world applications.
     */
    @Bean
    public UserDetailsManager userDetailsService() {
        UserDetails user =
                User.withUsername("user")
                        .password("{noop}user")
                        .roles("USER")
                        .build();
        UserDetails admin =
                User.withUsername("admin")
                        .password("{noop}admin")
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
