package com.example.paydaytrade.web.rest;

import com.example.paydaytrade.config.TestControllerConfig;
import com.example.paydaytrade.controller.AuthController;
import com.example.paydaytrade.dto.LoginDto;
import com.example.paydaytrade.dto.RegisterDto;
import com.example.paydaytrade.dto.tokenDto.InteractionResponse;
import com.example.paydaytrade.model.User;
import com.example.paydaytrade.repository.RoleRepository;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.security.userDetails.UserDetailsServiceImpl;
import com.example.paydaytrade.service.impl.AuthServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.mockito.Mockito.when;

@WithMockUser
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthController.class)
@Import({TestControllerConfig.class})
public class AuthControllerTest {

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Mock
    private RoleRepository authRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private LoginDto loginCreateRequestDto;

    @Mock
    private RegisterDto registerRequestDto;

    @Mock
    private User user;


    private static final String DUMMY_STRING = "string";
    private static final String EMAIL = "test@test.com";
    private static final Long DUMMY_ID = 1L;
    private static final String ROLE_ADMIN = "ADMIN";

    @Before
    public void setup() {
        user = User.builder()
                .id(DUMMY_ID)
                .password(DUMMY_STRING)
                .email(EMAIL)
                .name(DUMMY_STRING)
                .build();

        registerRequestDto = RegisterDto.builder()
                .username(DUMMY_STRING)
                .password(DUMMY_STRING)
                .authority(List.of("ADMIN"))
                .email(EMAIL)
                .build();

        loginCreateRequestDto = LoginDto.builder()
                .email(DUMMY_STRING)
                .password(DUMMY_STRING)
                .build();
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void registerUser() throws MessagingException, UnsupportedEncodingException {
        ResponseEntity<InteractionResponse> ok = ResponseEntity.ok(new InteractionResponse("User registered successfully!"));
        AuthController authController = new AuthController(authService);
        authController.registerUser(registerRequestDto, httpServletRequest);
        //Arrange
        when(authService.register(registerRequestDto, httpServletRequest.getContextPath())).thenReturn(user);
        //Assert
    }

    @Test
    public void loginForm_ShouldIncludeNewUserInModel() throws Exception {
        AuthController authController = new AuthController(authService);
        authController.login(loginCreateRequestDto);

        when(authService.login(loginCreateRequestDto)).thenReturn(null);
    }
}
