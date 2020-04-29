package ru.geekbrains.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.geekbrains.repository.UserRepository;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserAuthService(userRepository);
    }

    @Autowired
    public void authConfigure(AuthenticationManagerBuilder auth,
                              UserDetailsService userDetailsService,
                              PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(provider);
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest()
                    .hasAnyRole("GUEST", "MANAGER", "ADMIN", "SUPERADMIN")
                    .and()
                    .httpBasic(Customizer.withDefaults())
                    .csrf().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    @Configuration
    @Order(2)
    public static class UiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    //чтобы стили и картинки применялись для всех пользователей
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/welcome").permitAll()
                    .antMatchers("/product").permitAll()
                    .antMatchers("/product/**").hasAnyRole("MANAGER", "ADMIN", "SUPERADMIN")

                    //TODO сначала нужно создать суперадмина через форму, т.к. в БД напрямую нельзя из-за шифрования пароля
                    .antMatchers("/user").hasAnyRole("ADMIN", "SUPERADMIN")
//                    .antMatchers("/user/**").permitAll()//временно открываем всем
                    .antMatchers("/user/**").hasRole("SUPERADMIN")

                    .anyRequest().authenticated()
                    .and()
                    .formLogin(Customizer.withDefaults());
        }
    }
}
