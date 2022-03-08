package net.billscan.billscan

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.config.annotation.web.builders.WebSecurity


@Configuration
@EnableWebSecurity
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userDetailsService: UserDetailsService;

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(
                    "static/**",
                    "/css/**",
                    "/img/**",
                    "/js/**").permitAll()
                .antMatchers(
                    "/login",
                    "/registration",
                    "/forgot-password",
                    "/reset-password",
                    "/logout").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .and()
            .rememberMe()
                .key("uniqueAndSecret")
                .userDetailsService(userDetailsService)
                .and()
            .logout()
                .permitAll()
                .logoutSuccessUrl("/login?logout=successful")
    }

    override fun configure(web: WebSecurity) {
        web
            .ignoring()
            .antMatchers("/resources/**", "/static/**")
    }
}
