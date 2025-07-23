package rentmything.rentmything.Configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Configure {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/oauth2/**", "/logout").permitAll() // allow login
                                                                                                      // flow
                                                .requestMatchers("/rentMyThing/RegAsOwner",
                                                                "/rentMyThing/RegAsCustomer",
                                                                "/rentMyThing/addProduct")
                                                .permitAll() // secure
                                                // profile
                                                // update
                                                .anyRequest().permitAll())
                                .oauth2Login(oauth -> oauth
                                                .defaultSuccessUrl("/oauth2/success", true))
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/rentMyThing/logged-out") // optional frontend page
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .permitAll());

                return http.build();
        }
}
