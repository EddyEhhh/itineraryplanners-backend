package com.scrapheap.itineraryplanner.service;

import com.scrapheap.itineraryplanner.dto.SettingDTO;
import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.model.Setting;
import com.scrapheap.itineraryplanner.repository.AccountRepository;
import com.scrapheap.itineraryplanner.repository.SettingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private AccountRepository accountRepository;

    public SettingDTO getSettingDTO() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        Setting setting = account.getSetting();
        return convertToDTO(setting);
    }

    public String setLanguage(String language) {
        SettingDTO settingDTO = this.getSettingDTO();
        settingDTO.setLanguage(language);
        return "Language set successfully";
    }

    public String getLanguage() {
        SettingDTO settingDTO = this.getSettingDTO();
        return settingDTO.getLanguage();
    }

    private SettingDTO convertToDTO(Setting setting) {
        if (setting == null) {
            return null;
        }

        return SettingDTO.builder()
                .id(setting.getId())
                .language(setting.getLanguage())
                .build();
    }

    private Setting convertToEntity(SettingDTO settingDTO) {
        return Setting.builder()
                .id(settingDTO.getId())
                .language(settingDTO.getLanguage())
                .build();
    }
}
