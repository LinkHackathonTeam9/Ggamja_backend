package com.ggamja.domain.studylog.repository;

import com.ggamja.domain.studylog.entity.StudyLog;
import com.ggamja.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {
    List<StudyLog> findAllByMemberOrderByDateDesc(Member member);
}