package cl.ucm.coffee.service;

import cl.ucm.coffee.persitence.entity.UserEntity;
import cl.ucm.coffee.persitence.entity.UserRoleEntity;
import cl.ucm.coffee.persitence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private Set<String> invalidatedTokens = new HashSet<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));

        System.out.println(userEntity);

        String[] roles = userEntity.getRoles().stream().map(UserRoleEntity::getRole).toArray(String[]::new);

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(this.grantedAuthorities(roles))
                .accountLocked(userEntity.getLocked())
                .disabled(userEntity.getDisabled())
                .build();
    }

    public void blockUser(String username) {
        UserEntity user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setLocked(true);
        userRepository.save(user);
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }

    private String[] getAuthorities(String role) {
        if ("ADMIN".equals(role) || "CLIENT".equals(role)) {
            return new String[]{"random_order"};
        }
        return new String[]{};
    }

    private List<GrantedAuthority> grantedAuthorities(String[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            for (String authority : this.getAuthorities(role)) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }
        return authorities;
    }
}
