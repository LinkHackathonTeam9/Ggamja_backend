package com.ggamja.domain.studylog.service;

import com.ggamja.domain.studylog.dto.GetStudyLogDetailResponse;
import com.ggamja.domain.studylog.dto.GetStudyLogListResponse;
import com.ggamja.domain.studylog.dto.GetStudyLogResponse;
import com.ggamja.domain.studylog.entity.StudyLog;
import com.ggamja.domain.studylog.repository.StudyLogRepository;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.AUTH_UNAUTHENTICATED;
import static com.ggamja.global.response.status.BaseExceptionResponseStatus.STUDY_LOG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;

    public GetStudyLogListResponse getStudyLogs(Member member) { // 목록 조회
        List<StudyLog> logs = studyLogRepository.findAllByMemberOrderByDateDesc(member);

        List<GetStudyLogResponse> responses = logs.stream()
                .map(log -> GetStudyLogResponse.builder()
                        .logId(log.getId())
                        .category(log.getCard().getCategory())
                        .title(log.getCard().getTitle())
                        .difficulty(log.getCard().getDifficulty())
                        .date(log.getDate())
                        .build()
                )
                .toList();

        return GetStudyLogListResponse.of(responses);
    }

    @Transactional(readOnly = true)
    public GetStudyLogDetailResponse getStudyLogDetail(Long logId, Member member) { // 상세 조회
        StudyLog log = studyLogRepository.findById(logId)
                .orElseThrow(() -> new CustomException(STUDY_LOG_NOT_FOUND));

        if (!log.getMember().getId().equals(member.getId())) {
            throw new CustomException(AUTH_UNAUTHENTICATED);
        }

        return GetStudyLogDetailResponse.builder()
                .logId(log.getId())
                .cardId(log.getCard().getId())
                .category(log.getCard().getCategory())
                .title(log.getCard().getTitle())
                .meaning(log.getCard().getMeaning())
                .difficulty(log.getCard().getDifficulty())
                .date(log.getDate())
                .build();
    }
}