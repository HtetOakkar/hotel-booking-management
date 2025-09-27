package com.example.hotelbookingmanagement.config;

import com.example.hotelbookingmanagement.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig{

//    private final PasswordEncoder passwordEncoder;
//
//    private final UserDetailsServiceImpl userDetailsService;
//
//    private final String [] PUBLIC_URLS = {"/users/login",
//            "/users/register",
//            "/staffs/login",
//            "/admins/login",
//            "/",
//            "/rooms",
//            "/amenities",
//            "/contacts",
//            "/about-us",
//            "/bookings",
//            "/users/register",
//            "/css/**",
//            "/js/**",
//            "/images/**"};
//
//
//
//    @Bean
//    public DaoAuthenticationProvider authProvider() {
//        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//        auth.setUserDetailsService(userDetailsService);
//        auth.setPasswordEncoder(passwordEncoder);
//        return auth;
//    }
//
//
//    // 🔹 Admin security
//    @Bean
//    @Order(1)
//    public SecurityFilterChain adminChain(HttpSecurity http) throws Exception {
//        http.antMatcher("/admins/**")
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers(PUBLIC_URLS).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin(form -> form
//                        .loginPage("/admins/login")
//                        .loginProcessingUrl("/admins/login-process")
//                        .successHandler(new RoleBasedAuthenticationSuccessHandler("ROLE_ADMIN", "/admins/dashboard", "/admins/login"))
//                        .failureUrl("/admins/login?error=true")
//                        .permitAll()
//                )
//                .logout(logout -> logout.logoutUrl("/admins/logout")
//                        .logoutSuccessUrl("/admins/login"))
//                .authenticationProvider(authProvider());
//        return http.build();
//    }
//
//    // 🔹 Staff security
//    @Bean
//    @Order(2)
//    public SecurityFilterChain staffChain(HttpSecurity http) throws Exception {
//        http.antMatcher("/staffs/**")
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers(PUBLIC_URLS).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin(form -> form
//                        .loginPage("/staffs/login")
//                        .loginProcessingUrl("/staffs/login-process")
//                        .successHandler(new RoleBasedAuthenticationSuccessHandler("ROLE_STAFF", "/staffs/dashboard", "/staffs/login"))
//                        .failureUrl("/staffs/login?error=true")
//                        .permitAll()
//                )
//                .logout(logout -> logout.logoutUrl("/staffs/logout")
//                        .logoutSuccessUrl("/staffs/login"))
//                .authenticationProvider(authProvider());
//        return http.build();
//    }
//
//    // 🔹 User security
//    @Bean
//    @Order(3)
//    public SecurityFilterChain userChain(HttpSecurity http) throws Exception {
//        http.antMatcher("/users/**")
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers(PUBLIC_URLS).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin(form -> form
//                        .loginPage("/users/login")
//                        .loginProcessingUrl("/users/login")
//                        .successHandler(new RoleBasedAuthenticationSuccessHandler("ROLE_USER", "/users/profile", "/users/login"))
//                        .failureUrl("/users/login?error=true")
//                        .permitAll()
//                )
//                .rememberMe(rememberMe -> rememberMe
//                        .key("01997322-ca2d-72b1-8f8a-6b2ddb8b85e1")
//                        .tokenValiditySeconds(86400)
//                        .userDetailsService(userDetailsService)
//                )
//                .logout(logout -> logout.logoutUrl("/users/logout")
//                        .logoutSuccessUrl("/"))
//                .authenticationProvider(authProvider());
//        return http.build();
//    }

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    private final String [] PUBLIC_RESOURCES = { "/css/**", "/js/**", "/images/**" };

    // This single bean will be used by all security chains automatically.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // 👑 ADMIN Security Chain (Highest Priority)
    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .antMatcher("/admins/**")
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/admins/login").permitAll()
                        .antMatchers(PUBLIC_RESOURCES).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admins/login")
                        .loginProcessingUrl("/admins/login-process")
                        .usernameParameter("username")
                        .successHandler(new RoleBasedAuthenticationSuccessHandler("ROLE_ADMIN", "/admins/dashboard", "/admins/login"))
                        .failureUrl("/admins/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/admins/logout")
                        .logoutSuccessUrl("/admins/login")
                );
        return http.build();
    }
    // 👤 USER Security Chain
    @Bean
    @Order(2)
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/users/profile/**").hasRole("USER") // Secure user-specific pages
                        .anyRequest().permitAll() // All other pages are public
                )
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .loginProcessingUrl("/users/login-process")
                        .usernameParameter("username")
                        .successHandler(new RoleBasedAuthenticationSuccessHandler("ROLE_USER", "/users/profile", "/users/login"))
                        .failureUrl("/users/login?error=true")
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("01997322-ca2d-72b1-8f8a-6b2ddb8b85e1")
                        .tokenValiditySeconds(86400)
                        .userDetailsService(userDetailsService)
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }

}

