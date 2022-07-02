package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.dto.LoginDto;
import com.example.paydaytrade.dto.RegisterDto;
import com.example.paydaytrade.enums.RoleEnum;
import com.example.paydaytrade.exception.EmailAlreadyExistException;
import com.example.paydaytrade.exception.EmailOrPasswordInvalid;
import com.example.paydaytrade.exception.EnableCheckException;
import com.example.paydaytrade.exception.RoleNotFoundException;
import com.example.paydaytrade.model.Role;
import com.example.paydaytrade.model.User;
import com.example.paydaytrade.repository.RoleRepository;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.resource.TokenResponse;
import com.example.paydaytrade.security.JwtUtils;
import com.example.paydaytrade.security.userDetails.UserDetailsImpl;
import com.example.paydaytrade.service.AuthService;
import com.example.paydaytrade.service.MailService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final MailService mailService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public User register(RegisterDto registerDto, String siteURL) throws MessagingException, UnsupportedEncodingException {

        User user = new User(registerDto.getUsername(),
                registerDto.getEmail(),
                passwordEncoder.encode(registerDto.getPassword()));
        Set<Role> roles = new HashSet<>();

        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {
            roleRepository.save(Role.builder().name(RoleEnum.ADMIN).build());
            roleRepository.save(Role.builder().name(RoleEnum.USER).build());
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new EmailAlreadyExistException();
        }
        if (registerDto.getAuthority().contains("ADMIN")) {
            Role admin = roleRepository.findByName(RoleEnum.ADMIN)
                    .orElseThrow(RoleNotFoundException::new);
            roles.add(admin);
        }
        else if (registerDto.getAuthority().contains("USER")) {
            Role userRole = roleRepository.findByName(RoleEnum.USER)
                    .orElseThrow(RoleNotFoundException::new);
            roles.add(userRole);

        }else {
            throw new RoleNotFoundException();
        }
        user.setRoles(roles);
        user.setPhoneNumber(registerDto.getPhoneNumber());
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        userRepository.save(user);
        mailService.sendVerificationEmail(user, siteURL);
        return user;
    }

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
        User byEmail = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(EmailOrPasswordInvalid::new);
        boolean matches = passwordEncoder.matches(loginDto.getPassword(), byEmail.getPassword());
        if (!byEmail.isEnabled()) {
            throw new EnableCheckException();
        }
        if (!matches) {
            throw new EmailOrPasswordInvalid();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new TokenResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                authorities));
    }
}
