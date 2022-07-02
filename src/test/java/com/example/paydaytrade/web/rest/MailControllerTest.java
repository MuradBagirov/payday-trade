package com.example.paydaytrade.web.rest;

import com.example.paydaytrade.controller.MailController;
import com.example.paydaytrade.security.AuthEntryPointJwt;
import com.example.paydaytrade.security.JwtUtils;
import com.example.paydaytrade.security.userDetails.UserDetailsServiceImpl;
import com.example.paydaytrade.service.impl.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WithMockUser
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(MailController.class)
class MailControllerTest {

    @MockBean
    private MailServiceImpl mailService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void mailController() {
        verify(mailService, times(0)).sendWithoutAttachment("xeyal.aqayev1998@gmail.com", "cavansir.asad@gmail.com", "testContent");
    }
}
