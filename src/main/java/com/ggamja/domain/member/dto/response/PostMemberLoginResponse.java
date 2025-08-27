package com.ggamja.domain.member.dto.response;

import com.ggamja.domain.member.entity.Member;

public record PostMemberLoginResponse(
        Long id,
        String nickname,
        String levelName,
        boolean bonusGiven,
        boolean levelChanged
) {
    public static PostMemberLoginResponse of(Member member, boolean bonusGiven, boolean levelChanged) {
        return new PostMemberLoginResponse(
                member.getId(),
                member.getNickname(),
                member.getLevel().getLevel(),
                bonusGiven,
                levelChanged
        );
    }
}