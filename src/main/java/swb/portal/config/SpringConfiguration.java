package swb.portal.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import swb.portal.ad.AdFactory;
import swb.portal.user.UserFactory;


@Configuration
public class SpringConfiguration {


    @Bean
    public UserFactory userFactory() {
        return new UserFactory(new BCryptPasswordEncoder());
    }

    @Bean
    public AdFactory adFactory() {
        return new AdFactory();
    }
}
