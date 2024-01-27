package com.shboard.shboard.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.loginId.loginId = :loginId AND m.password.password = :password")
    Optional<Member> findByLoginIdAndPassword(final String loginId, final String password);

    @Query("SELECT m FROM Member m WHERE m.loginId.loginId = :loginId")
    Optional<Member> findByLoginId(final String loginId);
}
