package thro.inf.RestApplikationKleiderkreisel.konfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import thro.inf.RestApplikationKleiderkreisel.server.konto.KontoDetailsService;
import thro.inf.RestApplikationKleiderkreisel.server.konto.KontoSpeicher;

public class TauschWebSecurityConfigureAdapter extends WebSecurityConfigurerAdapter {

    private final KontoDetailsService kontoDetailsService;

    @Bean
    @Override
    protected UserDetailsService userDetailsService(){
        return kontoDetailsService;
    }

    @Autowired
    public TauschWebSecurityConfigureAdapter(KontoSpeicher kontoSpeicher){
        kontoDetailsService = new KontoDetailsService(kontoSpeicher);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.authorizeRequests().antMatchers("/**").permitAll()
                .anyRequest().fullyAuthenticated().and().httpBasic();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(kontoDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

}
