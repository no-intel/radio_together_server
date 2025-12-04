package com.noint.radio_together.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
@ToString
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Member member;

    private String token;

    @Column(nullable = false, name = "expire_at")
    private LocalDateTime expireAt;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public RefreshToken(Member member, String token) {
        LocalDateTime now = LocalDateTime.now();
        this.member = member;
        this.token = token;
        this.expireAt = now.plusDays(7);
        this.createdAt = now;
    }

    public void update(String token) {
        this.token = token;
        this.updatedAt = LocalDateTime.now().plusDays(7);
    }
}
