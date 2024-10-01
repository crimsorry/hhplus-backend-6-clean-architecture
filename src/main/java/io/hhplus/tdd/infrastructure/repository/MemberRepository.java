package io.hhplus.tdd.infrastructure.repository;

import io.hhplus.tdd.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByMemberId(long memberId);

}
