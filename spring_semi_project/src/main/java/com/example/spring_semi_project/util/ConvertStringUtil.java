package com.example.spring_semi_project.util;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConvertStringUtil {
    private final Map<String, String> days;

    public ConvertStringUtil() {
        this.days = new HashMap<>();
        days.put("Mon", "월");
        days.put("Tue", "화");
        days.put("Wed", "수");
        days.put("Thu", "목");
        days.put("Fri", "금");
        days.put("Sat", "토");
        days.put("Sun", "일");
    }

    public String convertToKoreanDayOfWeek(String dayOfWeek) {
        StringBuilder convertedDays = new StringBuilder();
        String[] daysArray = dayOfWeek.split(",");

        for (String day : daysArray) {
            String trimmedDay = day.trim();
            if (days.containsKey(trimmedDay)) {
                convertedDays.append(days.get(trimmedDay)).append("•");
            }
        }

        // 마지막 "•" 제거
        if (!convertedDays.isEmpty()) {
            convertedDays.deleteCharAt(convertedDays.length() - 1);
        }

        return convertedDays.toString();
    }
}
