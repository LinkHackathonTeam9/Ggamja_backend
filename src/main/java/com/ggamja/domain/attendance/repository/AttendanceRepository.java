package com.ggamja.domain.attendance.repository;

import com.ggamja.domain.attendance.entity.Attendance;
import com.ggamja.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByMemberAndDate(Member member, LocalDate date);
    List<Attendance> findByMemberAndDateBetween(Member member, LocalDate start, LocalDate end);
}
