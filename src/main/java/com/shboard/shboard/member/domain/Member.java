package com.shboard.shboard.member.domain;

import com.shboard.shboard.global.BaseEntity;
import com.shboard.shboard.member.domain.vo.LoginId;
import com.shboard.shboard.member.domain.vo.Nickname;
import com.shboard.shboard.member.domain.vo.Password;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private LoginId loginId;

    @Embedded
    private Password password;

    @Embedded
    private Nickname nickname;

    @Builder
    private Member(final String loginId, final String password, final String nickname) {
        this.loginId = new LoginId(loginId);
        this.password = new Password(password);
        this.nickname = new Nickname(nickname);
    }
}
