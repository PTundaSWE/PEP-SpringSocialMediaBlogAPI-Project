package org.paul.springbootlearning.service;

import org.paul.springbootlearning.entity.Account;
import org.paul.springbootlearning.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * User Story 1: Register
     * - username not blank
     * - password length >= 4
     * - username unique
     *
     * Success: returns saved Account (with accountId), HTTP 200 handled by controller
     * Duplicate username: 409
     * Other validation failure: 400
     */
    public Account register(Account account) {
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String username = account.getUsername();
        String password = account.getPassword();

        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (password == null || password.length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        boolean usernameTaken = accountRepository.findByUsername(username).isPresent();
        if (usernameTaken) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return accountRepository.save(account);
    }

    /**
     * User Story 2: Login
     * - username + password must match an existing account
     *
     * Success: returns Account (with accountId)
     * Failure: 401
     */
    public Account login(Account account) {
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String username = account.getUsername();
        String password = account.getPassword();

        if (username == null || password == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return accountRepository
                .findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }
}
