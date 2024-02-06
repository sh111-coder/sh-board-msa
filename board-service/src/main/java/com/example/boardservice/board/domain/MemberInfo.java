package com.example.boardservice.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo {

    @Id @GeneratedValue
    private Long id;

    private String nickname;

    public MemberInfo(final String nickname) {
        this.nickname = nickname;
    }
}
