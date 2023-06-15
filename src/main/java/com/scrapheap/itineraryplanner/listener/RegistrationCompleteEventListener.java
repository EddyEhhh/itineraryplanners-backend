package com.scrapheap.itineraryplanner.listener;

import com.scrapheap.itineraryplanner.event.RegistrationCompleteEvent;
import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.repository.VerificationTokenRepository;
import com.scrapheap.itineraryplanner.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    @Autowired
    private AccountService accountService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        Account account = event.getAccount();
        if(account.getVerificationToken() != null){
            verificationTokenRepository.delete(account.getVerificationToken());
        }
        String token = UUID.randomUUID().toString();
        accountService.createVerificationToken(account, token);
        String url = String.format("%s/api/v1/accounts/verify?token=%s",
                event.getApplicationUrl(), token);
        log.info(String.format("Verify your account: %s", url));
    }
}
