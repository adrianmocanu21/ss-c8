package adrian.mocanu.ssc6.security.filters;

import adrian.mocanu.ssc6.entities.Otp;
import adrian.mocanu.ssc6.repositories.OtpRepository;
import adrian.mocanu.ssc6.security.authentications.OtpAuthentication;
import adrian.mocanu.ssc6.security.authentications.UsernamePasswordAuthentication;
import adrian.mocanu.ssc6.security.managers.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Component
public class UsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        //Step 1: username & password
        //Step 2: username & otp
        String username = httpServletRequest.getHeader("username");
        String password = httpServletRequest.getHeader("password");
        String otp = httpServletRequest.getHeader("otp");

        if (otp == null) {
            //step 1
            UsernamePasswordAuthentication a = new UsernamePasswordAuthentication(username, password);
            authenticationManager.authenticate(a);
            // we generate an OTP
            Otp otpEntity = generateOtp(username);
            otpRepository.save(otpEntity);
        } else {
            //step 2
            OtpAuthentication a = new OtpAuthentication(username, otp);
            authenticationManager.authenticate(a);
            //  we issue a token
            String token = UUID.randomUUID().toString();
            tokenManager.add(token);
            httpServletResponse.setHeader("Authorization", token );
        }
    }

    private Otp generateOtp(String username) {
        String code = String.valueOf(new Random().nextInt(9999) + 1000);
        Otp otpEntity = new Otp();
        otpEntity.setUsername(username);
        otpEntity.setOtp(code);
        return otpEntity;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
       return  !request.getServletPath().equals("/login");
    }
}
