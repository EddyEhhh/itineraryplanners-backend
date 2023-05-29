package com.scrapheap.account.repositories;

import com.scrapheap.account.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public List<Account> findByUsernameAndIsDeletedFalse(String username);

    public List<Account> findByEmailAndIsDeletedFalse(String email);

}
