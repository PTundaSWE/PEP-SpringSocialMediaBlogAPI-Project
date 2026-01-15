package org.paul.springbootlearning.repository;

import org.paul.springbootlearning.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByUsernameAndPassword(String username, String password);
}
