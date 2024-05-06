package com.expenseTracker.repository;

import com.expenseTracker.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE MONTH(t.date) = MONTH(sysdate) AND t.member.id = :memberId order by t.date desc")
    Page<Transaction> findByMemberIdAndDateMonth(@Param("memberId") Long memberId, Pageable pageable);

    List<Transaction> findByMemberId(Long memberId);
}
