package com.shopmax.exception;

public class OutOfStockException extends RuntimeException {
    
    // 재고 부족 예외
    public OutOfStockException(String message) {
        super(message);
    }
}
