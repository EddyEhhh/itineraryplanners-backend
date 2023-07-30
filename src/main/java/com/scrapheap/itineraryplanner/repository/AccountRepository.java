package com.scrapheap.itineraryplanner.repository;

import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.model.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findByUsernameAndIsDeletedFalse(String username);

    public Account findByUsername(String username);

    public Account findByEmailAndIsDeletedFalse(String email);

    public Account findByForgotPassword(ForgotPassword forgotPassword);




}
