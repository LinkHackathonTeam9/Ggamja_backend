package com.ggamja.domain.quizlog.service;

import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.quizlog.dto.response.GetQuizLogListResponse;
import com.ggamja.domain.quizlog.entity.QuizLog;
import com.ggamja.domain.quizlog.repository.QuizLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizLogService {
    private final QuizLogRepository quizLogRepository;

    public GetQuizLogListResponse getQuizLogsList(Member member, Pageable pageable) {
        Page<QuizLog> quizLogs = quizLogRepository.findByMember(member, pageable);

        List<GetQuizLogListResponse.QuizLogDto> content = quizLogs.getContent().stream()
                .map(GetQuizLogListResponse.QuizLogDto::from)
                .toList();

        return GetQuizLogListResponse.of(content, quizLogs.hasNext());
    }
}
