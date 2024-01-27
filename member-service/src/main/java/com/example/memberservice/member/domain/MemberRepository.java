package com.example.memberservice.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.loginId.loginId = :loginId AND m.password.password = :password")
    Optional<Member> findByLoginIdAndPassword(final @Param("loginId") String loginId, final @Param("password") String password);

    @Query("SELECT m FROM Member m WHERE m.loginId.loginId = :loginId")
    Optional<Member> findByLoginId(final @Param("loginId") String loginId);
}
