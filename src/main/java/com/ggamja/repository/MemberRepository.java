package com.ggamja.repository;

import com.ggamja.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일 중복 확인용
    Optional<Member> findByEmail(String email);

    // 닉네임 중복 확인용
    Optional<Member> findByNickname(String nickname);

    // 로그인 시 email + password 매칭 (비밀번호는 보통 인코딩 비교해서 서비스 단에서 검증)
    Optional<Member> findByEmailAndPassword(String email, String password);
}