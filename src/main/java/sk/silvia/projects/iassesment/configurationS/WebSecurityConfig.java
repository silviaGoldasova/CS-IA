package sk.silvia.projects.iassesment.configurationS;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import sk.silvia.projects.iassesment.dao.UserRepository;
import sk.silvia.projects.iassesment.entity.MyUser;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/*", "/schedule/home", "/schedule/*", "/h2", "/console/*", "/h2_console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                ;

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        MyUser userM = new MyUser("user", DigestUtils.sha1Hex("password"), "password");
        userRepository.save(userM);
        int count = (int) userRepository.count();

        UserDetails user = User.withDefaultPasswordEncoder()
                        .username(userRepository.findAll().get(count-1).getUsername())
                        .password(userRepository.findAll().get(count-1).getPassword())
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

}