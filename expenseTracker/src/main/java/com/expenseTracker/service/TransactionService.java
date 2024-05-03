package com.expenseTracker.service;

import com.expenseTracker.dto.TransactionFormDto;
import com.expenseTracker.entity.Transaction;
import com.expenseTracker.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // 등록(insert)
    public Long saveTransaction(TransactionFormDto transactionFormDto) throws Exception {
        Transaction transaction = transactionFormDto.createTransaction(); // dto -> entity

        LocalDateTime dateTime = transactionFormDto.getDate().atTime(LocalTime.now());
        transaction.setDate(dateTime);

        transactionRepository.save(transaction); // insert

        return transaction.getId();
    }

    // 리스트
    @Transactional(readOnly = true)
    public Page<Transaction> getPage(Long memberId, Pageable pageable) {
        return transactionRepository.findByMemberIdAndDateMonth(memberId, pageable);
    }

    // 내역 정보 가져오기
    @Transactional(readOnly = true)
    public Transaction getDtl(Long itemId) {
        return transactionRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
    }
}
