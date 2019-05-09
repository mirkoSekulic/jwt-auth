package com.auth.jwtauth.modules.security.config;

import com.auth.jwtauth.modules.security.jwt.JWTFilter;
import com.auth.jwtauth.modules.security.jwt.TokenUtil;
import com.auth.jwtauth.modules.security.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private TokenUtil tokenUtil;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTFilter jwtFilter()
    {
        return new JWTFilter(tokenUtil);
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder,
                                        CustomUserDetailsServiceImpl userDetailsService) throws Exception
    {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }


    /**
     * Method for enabling / disabling specific application routes
     *
     * @param http - HTTP request
     * @throws Exception - Exception
     */
    @Override
    @SuppressWarnings("squid:S4502")
    protected void configure(HttpSecurity http) throws Exception
    {
        http

                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .headers().frameOptions().disable().and()

                .headers().cacheControl().disable().and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                // .exceptionHandling().authenticationEntryPoint(http401UnauthorizedEntryPoint).and()

                .authorizeRequests()

                // allow anonymous resource requests on login and reset password
                .antMatchers("/authenticate").permitAll()
                .antMatchers(HttpMethod.GET, "/v2/*", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()
                // any other request needs authentication
                .anyRequest().authenticated();

        // Custom JWT based security filter
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Autowired
    public void setTokenUtil(TokenUtil tokenUtil)
    {
        this.tokenUtil = tokenUtil;
    }
}
