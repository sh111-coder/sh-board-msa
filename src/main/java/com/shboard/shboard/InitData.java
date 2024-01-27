package com.shboard.shboard;

import com.shboard.shboard.board.domain.Board;
import com.shboard.shboard.member.domain.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile({"local", "test"})
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitDataService initDataService;

    @PostConstruct
    public void init() {
        initDataService.init();
    }

    @Component
    static class InitDataService {
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            int totalPostCount = 10;

            final Member member = com.shboard.shboard.member.domain.Member.builder()
                    .loginId("sh111")
                    .password("password1!")
                    .nickname("성하")
                    .build();
            em.persist(member);

            for (int i = 1; i <= totalPostCount; i++) {
                final Board board = new Board(em.find(Member.class, 1L), "title" + i, "content" + i);
                em.persist(board);
            }
        }
    }

}
