package com.expenseTracker.util;

import com.expenseTracker.constant.Type;
import org.springframework.stereotype.Component;

@Component
public class NumberFormatterUtil {

    public static String formatAmount(double amount) {
        return String.format("%,.0f원", amount);
    }
}