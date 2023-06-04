package com.scrapheap.account.repository;

import com.scrapheap.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public List<Account> findByUsernameAndIsDeletedFalse(String username);

    public List<Account> findByEmailAndIsDeletedFalse(String email);


}
