package cl.ucm.coffee.web.config;

import cl.ucm.coffee.service.UserSecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private  UserDetailsService userDetailsService;
    @Autowired
    private UserSecurityService userSecurityService;

//    @Autowired
//    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
//        this.jwtUtil = jwtUtil;
//        this.userDetailsService = userDetailsService;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Validar que sea un Header Authorization valido
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("=====>"+authHeader);
        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Validar que el JWT sea valido
        String jwt = authHeader.split(" ")[1].trim();

        if (!this.jwtUtil.isValid(jwt) || userSecurityService.isTokenInvalidated(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Cargar el usuario del UserDetailsService
        String username = this.jwtUtil.getUsername(jwt);
        User user = (User) this.userDetailsService.loadUserByUsername(username);

        // 4. Cargar al usuario en el contexto de seguridad.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          user.getUsername(), user.getPassword(), user.getAuthorities()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        System.out.println("======Pasa seguridad======");
        System.out.println(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
