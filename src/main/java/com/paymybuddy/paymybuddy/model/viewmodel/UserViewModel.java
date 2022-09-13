package com.paymybuddy.paymybuddy.model.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserViewModel {
    private String     email;
    private String     firstname;
    private String     lastname;
    private BigDecimal balance;
}
