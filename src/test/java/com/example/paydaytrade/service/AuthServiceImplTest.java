package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.LoginDto;
import com.example.paydaytrade.dto.RegisterDto;
import com.example.paydaytrade.enums.RoleEnum;
import com.example.paydaytrade.exception.EmailOrPasswordInvalid;
import com.example.paydaytrade.model.Role;
import com.example.paydaytrade.model.User;
import com.example.paydaytrade.repository.RoleRepository;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.security.JwtUtils;
import com.example.paydaytrade.service.impl.AuthServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.mail.MessagingException;
import javax.servlet.Filter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository authRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private MailService mailService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    AuthenticationManager authenticationManager;

    private Set<User> mocUser;

    private User user;

    private RegisterDto registerRequestDto;
    private LoginDto createRequestDto;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private MockMvc mvc;

    private static final String DUMMY_STRING = "test";
    private static final String EMAIL = "test@test.com";
    private static final Long DUMMY_ID = 1L;
    private static final String ROLE_ADMIN = "ADMIN";

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .phoneNumber(DUMMY_STRING)
                .id(DUMMY_ID)
                .password(DUMMY_STRING)
                .email(EMAIL)
                .name(DUMMY_STRING)
                .build();
        registerRequestDto = RegisterDto.builder()
                .phoneNumber(DUMMY_STRING)
                .password(DUMMY_STRING)
                .authority(List.of(ROLE_ADMIN))
                .email(DUMMY_STRING)
                .build();
        createRequestDto = LoginDto.builder()
                .email(EMAIL)
                .password(DUMMY_STRING)
                .build();

    }

    @Test
    public void registerUser() throws MessagingException, UnsupportedEncodingException {
        when(passwordEncoder.encode(any())).thenReturn(DUMMY_STRING);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(authRepository.findByName(any())).thenReturn(Optional.of(Role.builder()
                .name(RoleEnum.ADMIN)
                .build()));
        when(userRepository.save(any())).thenReturn(user);

        User user = authService.register(registerRequestDto, "test");

        assertThat(user.getEmail()).isEqualTo(DUMMY_STRING);
    }

    @Test
    public void login() throws Exception {

        assertThatThrownBy(() -> authService.login(createRequestDto)).isInstanceOf(EmailOrPasswordInvalid.class);
        mvc
                .perform(get("/auth/signIn").with(httpBasic("user", "password")))
                .andExpect(status().isNotFound())
                .andExpect(authenticated().withUsername("user"));
    }

    @Test
    void whenSignInNotFoundUserThrowException() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        LoginDto requestDto = LoginDto.builder()
                .email("wrong")
                .password("1sdsd234sds5678")
                .build();

        AssertionsForClassTypes.assertThatThrownBy(() -> authService.login(requestDto))
                .isInstanceOf(EmailOrPasswordInvalid.class);
    }

    @Configuration
    @EnableWebMvcSecurity
    @EnableWebMvc
    static class Config extends WebSecurityConfigurerAdapter {
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .withUser("user").password("password").roles("USER");
        }
    }
}
