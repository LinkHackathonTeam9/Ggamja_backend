package com.ggamja.domain.quizlog.service;

import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.quizlog.dto.response.GetQuizLogDetailResponse;
import com.ggamja.domain.quizlog.dto.response.QuizLogDto;
import com.ggamja.domain.quizlog.entity.QuizLog;
import com.ggamja.domain.quizlog.repository.QuizLogRepository;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizLogService {
    private final QuizLogRepository quizLogRepository;

    public Page<QuizLogDto> getQuizLogsList(Member member, Pageable pageable) {
        return quizLogRepository.findByMember(member, pageable)
                .map(QuizLogDto::from);
    }

    public GetQuizLogDetailResponse getQuizLogDetail(Member member, Long id) {
        QuizLog quizLog = quizLogRepository.findByIdAndMember(id, member)
                .orElseThrow(() -> new CustomException(QUIZLOG_NOT_FOUND));

        return GetQuizLogDetailResponse.of(quizLog);
    }
}
