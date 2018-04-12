package br.com.avelar.frontend.security;

import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .antMatchers("/javax.faces.resource/**").permitAll()
                .and()
            .formLogin()
                .loginPage("/login.xhtml")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/index.xhtml", true)
                .failureUrl("/login.xhtml?error=true")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.xhtml")
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
            .passwordEncoder(new BCryptPasswordEncoder())
            .dataSource(getDataSource())
            .usersByUsernameQuery("select username,password, enabled from users where username=?")
            .authoritiesByUsernameQuery("select username, role from user_roles where username=?");
    }

    public DataSource getDataSource() {
        EntityManagerFactoryImpl hibernateEmf = 
                (EntityManagerFactoryImpl) Persistence.createEntityManagerFactory("pu");
        DatasourceConnectionProviderImpl provider = 
                    (DatasourceConnectionProviderImpl) hibernateEmf
                                                         .getSessionFactory()
                                                             .getServiceRegistry()
                                                             .getService(ConnectionProvider.class);
        return provider.getDataSource();
    }

}
