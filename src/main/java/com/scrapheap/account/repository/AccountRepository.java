package com.scrapheap.account.repository;

import com.scrapheap.account.model.Account;
import com.scrapheap.account.model.Setting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findByUsernameAndIsDeletedFalse(String username);

    public Account findByUsername(String username);

    public List<Account> findByEmailAndIsDeletedFalse(String email);
}
