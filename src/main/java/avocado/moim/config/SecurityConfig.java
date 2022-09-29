package avocado.moim.config;

import avocado.moim.config.jwt.AuthenticationFilter;
import avocado.moim.config.jwt.AuthorizationFilter;
import avocado.moim.config.jwt.CustomLogoutSuccessHandler;
import avocado.moim.user.repository.UserRepository;
import avocado.moim.user.service.UserService;
import avocado.moim.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final UserRepository userRepository;
    private final Environment env;
    private final UserService userService;
    private final AuthenticationUtils utils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyCustomDsl())
                .and()
                // 권한에 따라 url 접근 제한
                .authorizeRequests(authorize -> authorize.antMatchers("/api/user/**")
                        .access("hasRole('USER') or hasRole('ADMIN')")
                        .antMatchers("/api/admin/**")
                        .access("hasRole('ADMIN')")
                        .anyRequest().permitAll()
                )
                .logout()
                .logoutUrl("/users/logout")
                .logoutSuccessHandler(new CustomLogoutSuccessHandler(env, userService))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new AuthenticationFilter(authenticationManager, env, userService, utils))
                    .addFilter(new AuthorizationFilter(authenticationManager, userRepository, env));
        }
    }
}
