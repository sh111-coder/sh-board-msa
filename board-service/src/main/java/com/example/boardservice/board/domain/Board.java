package com.example.boardservice.board.domain;

import com.example.memberservice.member.domain.Member;
import com.example.shboardcommon.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Member writer;

    private String title;

    private String content;

    private Long viewCount;

    public Board(final Member writer, final String title,
                 final String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.viewCount = 1L;
    }

    public String getWriterNickname() {
        return writer.getNickname().getNickname();
    }
}
