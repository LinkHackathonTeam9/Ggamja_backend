package com.ggamja.domain.member.entity;

import com.ggamja.domain.level.entity.Level;
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
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int points;

    @Column(nullable = true, columnDefinition = "INT DEFAULT 0")
    private int weeklyAttendanceCount;   // 이번 주 출석 일수

    @Column(nullable = true)
    private LocalDate weekStartDate;    // 이번 주(월요일)의 시작 날짜

    @Column(nullable = true) // 회원가입했을 때 insert 가능하도록
    private LocalDateTime lastLogin;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public void addPoints(int amount) {
        this.points += amount;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateLastLogin(LocalDateTime loginTime) {
        this.lastLogin = loginTime;
    }

    public void resetWeeklyAttendance(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
        this.weeklyAttendanceCount = 1;
    }

    public void increaseAttendance() {
        this.weeklyAttendanceCount += 1;
    }

    public void updateLevel(Level newLevel) {
        this.level = newLevel;
    }
}