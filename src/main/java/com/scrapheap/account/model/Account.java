package com.scrapheap.account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String displayName;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String imageUrl = null;

    private LocalDateTime created;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name = "loginAttempt",
            referencedColumnName = "id")
    private LoginAttempt loginAttempt;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name = "setting",
            referencedColumnName = "id")
    private Setting setting;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name = "forgotPassword",
            referencedColumnName = "id")
    private ForgotPassword forgotPassword;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "session", referencedColumnName = "id")
    private Session session;



    private boolean isDeleted = false;

    private LocalDateTime deletedAt;



}
