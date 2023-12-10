package com.store.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static com.store.management.config.SecurityRole.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_PATHS = {
            "/swagger-ui.html",
            "/webjars/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
    };

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange((exchanges) -> exchanges
                        .pathMatchers(SWAGGER_PATHS).permitAll()
                        .pathMatchers(HttpMethod.GET, "/products", "/products/*").authenticated()
                        .pathMatchers(HttpMethod.PUT, "/products/*/updatePrice").hasAnyRole(COMMERCIAL.name(), ADMIN.name())
                        .pathMatchers(HttpMethod.PUT, "/products/*/updateStock").hasAnyRole(WAREHOUSE.name(), ADMIN.name())
                        .pathMatchers(HttpMethod.POST, "/products").hasRole(ADMIN.name())
                        .pathMatchers(HttpMethod.DELETE, "/products/**").hasRole(ADMIN.name())
                )
                .formLogin(withDefaults())
                .logout(withDefaults())
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        return new MapReactiveUserDetailsService(admin(), commercial(), warehouse());
    }

    private static UserDetails admin() {
        return User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles(ADMIN.name(), COMMERCIAL.name(), WAREHOUSE.name())
                .build();
    }

    private static UserDetails commercial() {
        return User.withDefaultPasswordEncoder()
                .username("commercial")
                .password("commercial")
                .roles(COMMERCIAL.name())
                .build();
    }

    private static UserDetails warehouse() {
        return User.withDefaultPasswordEncoder()
                .username("warehouse")
                .password("warehouse")
                .roles(WAREHOUSE.name())
                .build();
    }
}