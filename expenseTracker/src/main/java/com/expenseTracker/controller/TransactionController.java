package com.expenseTracker.controller;

import com.expenseTracker.dto.TransactionFormDto;
import com.expenseTracker.entity.Transaction;
import com.expenseTracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // 내역 등록(insert)
    @PostMapping(value = "/transaction/insert")
    public @ResponseBody ResponseEntity insert(@Valid TransactionFormDto transactionFormDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            // 유효성 체크 후 에러 결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 에러 메시지를 가지고 온다.

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            transactionService.saveTransaction(transactionFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // 내역 확인
    @GetMapping(value = "/transaction/view/{transactionId}")
    public ResponseEntity<Transaction> view(@PathVariable("transactionId") Long transactionId) {
        Transaction transaction = transactionService.getDtl(transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
