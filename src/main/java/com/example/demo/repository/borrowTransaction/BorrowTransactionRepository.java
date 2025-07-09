package com.example.demo.repository.borrowTransaction;

import com.example.demo.model.borrowTransaction.BorrowTransaction;
import com.example.demo.model.borrowTransaction.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BorrowTransactionRepository extends JpaRepository<BorrowTransaction, Long> {
    @Query("SELECT bt FROM BorrowTransaction bt WHERE bt.member.id = :memberId AND bt.book.id = :bookId AND bt.status = :status")
    Optional<BorrowTransaction> findByMemberIdAndBookIdAndStatus(@Param("memberId") Long memberId, @Param("bookId") Long bookId, @Param("status") Status status);

    @Query("SELECT COUNT(bt) FROM BorrowTransaction bt WHERE bt.member.id = :memberId AND bt.status = :status")
    long countByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status") Status status);
}
