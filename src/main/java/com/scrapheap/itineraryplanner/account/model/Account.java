package com.scrapheap.itineraryplanner.account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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

    private String imageUrl;

    private LocalDateTime created;

    private String role;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(
            referencedColumnName = "id")
    private LoginAttempt loginAttempt;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(
            referencedColumnName = "id")
    private Setting setting;

    @OneToOne(cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(
            referencedColumnName = "id")
    private ForgotPassword forgotPassword;

    @OneToOne(mappedBy = "account")
    private VerificationToken verificationToken;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "session", referencedColumnName = "id")
    private Session session;

    private boolean isVerified;

    private boolean isDeleted;

    private LocalDateTime deletedAt;



}
