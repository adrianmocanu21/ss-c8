package adrian.mocanu.ssc6.service;

import adrian.mocanu.ssc6.entities.User;
import adrian.mocanu.ssc6.repositories.UserRepository;
import adrian.mocanu.ssc6.security.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findUserByUsername(s);
        User u = userOpt.orElseThrow( () -> new UsernameNotFoundException("User does not exist"));
        return new SecurityUser(u);
    }
}
