package com.scrapheap.itineraryplanner.account.repository;

import com.scrapheap.itineraryplanner.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findByUsernameAndIsDeletedFalse(String username);

    public Account findByUsername(String username);

    public Account findByEmailAndIsDeletedFalse(String email);


}
