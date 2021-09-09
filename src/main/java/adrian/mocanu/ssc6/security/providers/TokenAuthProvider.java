package adrian.mocanu.ssc6.security.providers;

import adrian.mocanu.ssc6.security.authentications.TokenAuthentication;
import adrian.mocanu.ssc6.security.managers.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthProvider implements AuthenticationProvider {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();

        if (!token.equals("") && tokenManager.contains(token)) {
            //our token should have authorities
            return  new TokenAuthentication(token, null, null);
        }
        throw new BadCredentialsException("Nasol");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TokenAuthentication.class.equals(aClass);
    }
}
