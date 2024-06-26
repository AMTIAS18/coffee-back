package cl.ucm.coffee.web.controller;

import cl.ucm.coffee.persitence.entity.UserEntity;
import cl.ucm.coffee.persitence.entity.UserRoleEntity;
import cl.ucm.coffee.persitence.repository.UserRepository;
import cl.ucm.coffee.persitence.repository.UserRoleRepository;
import cl.ucm.coffee.service.UserSecurityService;
import cl.ucm.coffee.service.dto.LoginDto;
import cl.ucm.coffee.service.dto.UserDto;
import cl.ucm.coffee.web.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserSecurityService userSecurityService;

//    @Autowired
//    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);

       // System.out.println(authentication.isAuthenticated());
       // System.out.println(authentication.getPrincipal());

        String jwt = this.jwtUtil.create(loginDto.getUsername());
        Map map = new HashMap<>();
        map.put("token",jwt);
        return ResponseEntity.ok(map);
        //return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserDto userDTO) {
        if (userRepository.existsById(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userEntity.setPassword(encodedPassword);

        userEntity.setEmail(userDTO.getEmail());
        userEntity.setLocked(userDTO.getLocked());
        userEntity.setDisabled(userDTO.getDisabled());

        userRepository.save(userEntity);

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUsername(userDTO.getUsername());
        userRoleEntity.setRole("CLIENT");
        userRoleEntity.setGrantedDate(LocalDateTime.now());
        userRoleEntity.setUser(userEntity);

        userRoleRepository.save(userRoleEntity);

        return ResponseEntity.ok("Usuario creado exitosamente con rol CLIENT");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDTO) {
        UserEntity userEntity = userRepository.findById(userDTO.getUsername()).orElse(null);
        if (userEntity == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        userEntity.setEmail(userDTO.getEmail());
        userEntity.setLocked(userDTO.getLocked());
        userEntity.setDisabled(userDTO.getDisabled());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            userEntity.setPassword(encodedPassword);
        }

        userRepository.save(userEntity);

        return ResponseEntity.ok("Usuario actualizado exitosamente");
    }

    @PostMapping("/block/{username}")
    public ResponseEntity<?> blockUser(@PathVariable String username) {
        userSecurityService.blockUser(username);
        return ResponseEntity.ok("Usuario bloqueado exitosamente");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        userSecurityService.invalidateToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok("Sesion cerrada exitosamente");
    }
}
