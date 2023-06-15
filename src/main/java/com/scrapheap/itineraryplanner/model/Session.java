package com.scrapheap.itineraryplanner.model;

import jakarta.persistence.*;
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
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private LocalDateTime timestamp;

    private LocalDateTime expirationTimestamp;

    @OneToOne(mappedBy = "session",
            cascade=CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Account account;

}
