package com.food.auth.config;

import com.food.auth.filter.AccessTokenAuthenticationFilter;
import com.food.common.annotation.RequestPathScanner;
import com.food.common.user.enumeration.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;
import java.util.Set;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final RequestPathScanner scanner;
    private final AccessTokenAuthenticationFilter accessTokenAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS);

        Map<Role, Set<String>> requestPaths = scanner.scanRequestMethods();
        http.authorizeHttpRequests(
                (auth) -> auth
                        .antMatchers(requestPaths.get(Role.CUSTOMER).toArray(new String[0])).hasRole(Role.CUSTOMER.name())
                        .antMatchers(requestPaths.get(Role.STORE_OWNER).toArray(new String[0])).hasRole(Role.STORE_OWNER.name())
                        .antMatchers(requestPaths.get(Role.MANAGER).toArray(new String[0])).hasRole(Role.MANAGER.name())
                        .antMatchers(requestPaths.get(Role.ALL).toArray(new String[0])).permitAll()
                    .anyRequest().authenticated())
                .addFilterBefore(accessTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/resources/**");
    }


}
