package com.ggamja.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    @Setter
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Setter
    private String password;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    @Setter
    private int points;

    @Column(nullable = true, columnDefinition = "INT DEFAULT 0")
    @Setter
    private int weeklyAttendanceCount;   // 이번 주 출석 일수

    @Column(nullable = true)
    @Setter
    private LocalDate weekStartDate;    // 이번 주(월요일)의 시작 날짜

    @Column(nullable = true) // 회원가입했을 때 insert 가능하도록
    @Setter
    private LocalDateTime lastLogin;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    public static Member create(String nickname, String email, String password, Level level) {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .level(level)
                .build();
    }
}