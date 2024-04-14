package com.example.spring_semi_project.util;

import com.example.spring_semi_project.dto.Course;
import com.example.spring_semi_project.dto.CourseTime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public String convertToKoreanDayOfWeek(Course course) {
        StringBuilder convertedDays = new StringBuilder();

        Map<String, List<String>> courseMap = new HashMap<>();

        for (CourseTime courseTime : course.getCourseTime()) {
            String key = courseTime.getStartTime() + " - " + courseTime.getEndTime();

            if (!courseMap.containsKey(key)) {
                courseMap.put(key, new ArrayList<>());
            }
            courseMap.get(key).add(days.get(courseTime.getCourseDay().trim()));
        }

        for (Map.Entry<String, List<String>> entry : courseMap.entrySet()) {
            for (String str : entry.getValue()) {
                convertedDays.append(str).append("•");
            }
            convertedDays.replace(convertedDays.length() - 1, convertedDays.length(), " ");
            convertedDays.append(entry.getKey()).append("\n");
        }
        convertedDays.delete(convertedDays.length() - 1, convertedDays.length());

        return convertedDays.toString();
    }
}
