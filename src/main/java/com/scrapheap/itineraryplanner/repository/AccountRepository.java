package com.scrapheap.itineraryplanner.repository;

import com.scrapheap.itineraryplanner.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findByUsernameAndIsDeletedFalse(String username);

    public Account findByUsername(String username);

    public Account findByEmailAndIsDeletedFalse(String email);


}
