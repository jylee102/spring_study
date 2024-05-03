package com.expenseTracker.service;

import com.expenseTracker.constant.Type;
import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Member;
import com.expenseTracker.repository.AssetRepository;
import com.expenseTracker.repository.CategoryRepository;
import com.expenseTracker.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // 하나의 메소드가 트랜잭션으로 묶인다.(DB Exception 혹은 다른 Exception 발생시 롤백)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;

    // 회원가입
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        saveDefaults(member);

        return member;
    }

    private void saveDefaults(Member member) {
        String[] incomeCategories = {"급여", "용돈", "금융소득"};
        String[] expenseCategories = {"식비", "교통/차량", "패션/미용", "문화생활", "경조사비"};
        String[] assetNames = {"현금", "은행", "카드", "저축"};

        // 수입 카테고리 생성 및 저장
        for (String categoryName : incomeCategories) {
            Category category = new Category(categoryName, Type.INCOME, member);
            categoryRepository.save(category);
        }

        // 지출 카테고리 생성 및 저장
        for (String categoryName : expenseCategories) {
            Category category = new Category(categoryName, Type.EXPENSE, member);
            categoryRepository.save(category);
        }

        // 자산 생성 및 저장
        for (String assetName : assetNames) {
            Asset asset = new Asset(assetName, member);
            assetRepository.save(asset);
        }
    }

    // 회원 중복체크
    private void validateDuplicateMember(Member member) {
        Member findMember = getMember(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // 이메일로 회원 찾기
    public Member getMember(String email) {
        return memberRepository.findByEmail(email);
    }
}
