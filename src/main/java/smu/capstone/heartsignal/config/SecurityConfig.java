package smu.capstone.heartsignal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .headers()
                .and()
                    .authorizeExchange()
                        .pathMatchers("/static/**", "/", "/api/**").permitAll()
                        .anyExchange().authenticated()
                .and()
                    .oauth2Login()
                .and()
                .build();
    }
}
