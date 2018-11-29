package com.ex.forblog.account;

import com.ex.forblog.account.AccountDto.AccountRegister;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    ModelMapper modelMapper;

    public Account register(AccountRegister accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        return accountRepository.save(account);
    }
}