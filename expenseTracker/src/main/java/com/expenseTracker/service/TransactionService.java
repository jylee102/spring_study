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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    // 리스트 페이지
    @Transactional(readOnly = true)
    public Page<Transaction> getPage(Long memberId, Pageable pageable) {
        return transactionRepository.findByMemberIdAndDateMonth(memberId, pageable);
    }

    // 리스트(달력)
    @Transactional(readOnly = true)
    public List<Transaction> getList(Long memberId) {
        return transactionRepository.findByMemberId(memberId);
    }

    // 내역 정보 가져오기
    @Transactional(readOnly = true)
    public Transaction getDtl(Long itemId) {

        return transactionRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
    }

    // 수정(edit)
    public Long updateTransaction(TransactionFormDto transactionFormDto) throws Exception {

        Transaction transaction = transactionRepository.findById(transactionFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        transaction.update(transactionFormDto);

        return transaction.getId();
    }

    // 삭제(delete)
    public void deleteTransaction(Long transactionId) throws Exception {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(EntityNotFoundException::new);

        transactionRepository.delete(transaction);
    }
}
