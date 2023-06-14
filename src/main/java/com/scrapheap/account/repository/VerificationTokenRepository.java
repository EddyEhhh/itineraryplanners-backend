package com.scrapheap.account.repository;

import com.scrapheap.account.model.Account;
import com.scrapheap.account.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    public VerificationToken findByToken(String token);

}
