package com.example.paydaytrade.service;

import com.example.paydaytrade.dto.balance.ReqDeposit;
import com.example.paydaytrade.exception.NotFoundException;
import com.example.paydaytrade.model.Deposit;
import com.example.paydaytrade.model.User;
import com.example.paydaytrade.repository.DepositRepository;
import com.example.paydaytrade.repository.UserRepository;
import com.example.paydaytrade.service.impl.DepositServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class DepositServiceImplTest {
    private static final Long DUMMY_ID = 1L;
    private static final String DUMMY_STRING = "string";
    private static final LocalDate DUMMY_DATE = LocalDate.parse("2015-02-20");
    @InjectMocks
    private DepositServiceImpl depositService;
    @Mock
    private DepositRepository balanceRepository;
    @Mock
    private User user;
    @Mock
    private Deposit balance;
    @Mock
    private UserRepository userRepository;
    @Spy
    private ModelMapper modelMapper;
    private ReqDeposit balanceRequest;
    @BeforeEach
    void setUp() {
        balanceRequest = getBalanceRequest();
        balance=getBalance();
        user=getUser();
    }
    @Test
    public void givenRequestNotFoundThenException() {
        //Arrange
        when(userRepository.findById(1L));
        //Act & Assert
        assertThatThrownBy(() -> depositService.addDepositForUser(getBalanceRequest(), anyLong()))
                .isInstanceOf(UnfinishedStubbingException.class);
    }
    @Test
    void givenInValidIdWhenThenException() {
        //Act & Assert
        assertThatThrownBy(() -> depositService.addDepositForUser(getBalanceRequest(), anyLong())).isInstanceOf(NotFoundException.class);
    }
    @Test
    public void balanceIfCheck() {
        user.setBalance(balance);
        assertThat(getBalanceRequest().getAddBalance()).isNotNull();
        verify(userRepository, times(0)).save(user);
        BigDecimal addBalance = getBalanceRequest().getAddBalance();
        BigDecimal currentBalance = BigDecimal.ZERO;
        BigDecimal l = addBalance.add(currentBalance);
        Assertions.assertSame(l, 1L);
    }
    private ReqDeposit getBalanceRequest() {
        return ReqDeposit
                .builder()
                .addBalance(BigDecimal.ONE)
                .build();
    }
    private Deposit getBalance() {
        return Deposit.builder()
                .currentBalance(BigDecimal.ONE)
                .build();
    }
    private User getUser() {
        return User.builder()
                .id(1L)
                .balance(null)
                .build();
    }
}
