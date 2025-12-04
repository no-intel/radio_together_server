package com.noint.radio_together.member.entity;

import com.noint.radio_together.member.enums.auth.AuthEnum;
import com.noint.radio_together.member.enums.Role;
import com.noint.radio_together.member.enums.State;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.noint.radio_together.member.enums.Role.USER;
import static com.noint.radio_together.member.enums.State.ACTIVE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "state"}))
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    private AuthEnum auth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Member(String email, String name, String thumbnail, AuthEnum auth) {
        this.email = email;
        this.name = name;
        this.thumbnail = thumbnail;
        this.auth = auth;
        this.state = ACTIVE;
        this.role = USER;
        this.createdAt = LocalDateTime.now();
    }


    public static Member of(String email,
                            String name,
                            String thumbnail,
                            AuthEnum auth) {

        return new Member(email, name, thumbnail, auth);

    }
}
