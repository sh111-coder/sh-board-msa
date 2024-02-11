package com.example.memberservice.member.domain;

import com.example.memberservice.member.domain.vo.LoginId;
import com.example.memberservice.member.domain.vo.Nickname;
import com.example.memberservice.member.domain.vo.Password;
import com.example.shboardcommon.global.BaseEntity;
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

    private Long writePoint;

    @Builder
    private Member(final String loginId, final String password, final String nickname) {
        this.loginId = new LoginId(loginId);
        this.password = new Password(password);
        this.nickname = new Nickname(nickname);
        this.writePoint = 0L;
    }

    public void writeBoard() {
        this.writePoint += 500L;
    }
}
