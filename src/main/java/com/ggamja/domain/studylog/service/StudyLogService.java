package com.ggamja.domain.studylog.service;

import com.ggamja.domain.studylog.dto.response.GetStudyLogDetailResponse;
import com.ggamja.domain.studylog.dto.response.StudyLogDto;
import com.ggamja.domain.studylog.entity.StudyLog;
import com.ggamja.domain.studylog.repository.StudyLogRepository;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.AUTH_UNAUTHENTICATED;
import static com.ggamja.global.response.status.BaseExceptionResponseStatus.STUDY_LOG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;

    public Page<StudyLogDto> getStudyLogs(Member member, Pageable pageable) {
        return studyLogRepository.findByMember(member, pageable)
                .map(StudyLogDto::from);
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