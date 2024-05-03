package com.expenseTracker.service;

import com.expenseTracker.constant.Type;
import com.expenseTracker.entity.Category;
import com.expenseTracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 로그인한 유저가 설정한 카테고리 불러오기
    public List<Category> getCategories(Long memberId, Type type) {
        return categoryRepository.findByMemberIdAndType(memberId, type);
    }
}
