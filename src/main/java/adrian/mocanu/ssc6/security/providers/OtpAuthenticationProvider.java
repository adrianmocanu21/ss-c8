package adrian.mocanu.ssc6.security.providers;

import adrian.mocanu.ssc6.entities.Otp;
import adrian.mocanu.ssc6.repositories.OtpRepository;
import adrian.mocanu.ssc6.security.authentications.OtpAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String otp = (String) authentication.getCredentials();
        Optional<Otp> o = otpRepository.findOtpByUsername(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() ->  "read");
        if (o.isPresent() && o.get().getOtp().equals(otp)) {
            return new OtpAuthentication(username, otp, authorities);
        }

        throw new BadCredentialsException("Bad credentials!");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OtpAuthentication.class.equals(aClass);
    }
}
