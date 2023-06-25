package com.scrapheap.itineraryplanner.listener;

import com.scrapheap.itineraryplanner.repository.VerificationTokenRepository;
import com.scrapheap.itineraryplanner.service.MailService;
import com.scrapheap.itineraryplanner.util.StringUtil;
import com.scrapheap.itineraryplanner.event.RegistrationCompleteEvent;
import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    @Autowired
    private AccountService accountService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private StringUtil stringUtil;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        Account account = event.getAccount();
        if(account.getVerificationToken() != null){
            verificationTokenRepository.delete(account.getVerificationToken());
        }
        String token = UUID.randomUUID().toString();
        accountService.createVerificationToken(account, token);
        String url = String.format("%s/api/v1/auth/verify?token=%s",
                event.getApplicationUrl(), token);

        String mailSubject = "Welcome to TheItineraryPlanners";
        String mailMessage = stringUtil.getStringFromFile("/template/verification.html");

        Map<String, Object> params = new HashMap<>();
        params.put("verification", url);
        mailMessage = StringSubstitutor.replace(mailMessage, params, "${", "}");
        mailService.sendEmail(account.getEmail(), mailSubject, mailMessage);
        log.info(String.format("Verify account link temp: %s", url));
    }

}
