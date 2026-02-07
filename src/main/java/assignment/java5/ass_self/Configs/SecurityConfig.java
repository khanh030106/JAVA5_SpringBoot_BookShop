package assignment.java5.ass_self.Configs;

import assignment.java5.ass_self.Controllers.OAuth2LoginHandler;
import assignment.java5.ass_self.Services.CustomOAUth2UserService;
import assignment.java5.ass_self.Services.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            OAuth2LoginHandler oAuth2LoginHandler,
            CustomUserDetailService customUserDetailService) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/bookseller/home",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        .requestMatchers(
                                "/bookseller/cart/**",
                                "/bookseller/profile/**"
                        ).authenticated()

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/staff/**").hasAnyRole("CUSTOMER")

                        .anyRequest().permitAll()
                )

                .userDetailsService(customUserDetailService)

                //  LOGIN BẰNG EMAIL + PASSWORD
                .formLogin(form -> form
                        .loginPage("/bookseller/login")
                        .loginProcessingUrl("/login") // form action
                        .usernameParameter("email") // email
                        .passwordParameter("password")
                        .defaultSuccessUrl("/bookseller/home", true)
                        .failureUrl("/bookseller/login?FormError=true")
                        .permitAll()
                )

                //  LOGIN GOOGLE
                .oauth2Login(oauth -> oauth
                        .loginPage("/bookseller/login")
                        .defaultSuccessUrl("/bookseller/home", true)
                        .failureUrl("/bookseller/login?OAuth2Error=true")
                        .successHandler(oAuth2LoginHandler)
                )

                //  LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/bookseller/login")
                );

        return http.build();
    }
}

