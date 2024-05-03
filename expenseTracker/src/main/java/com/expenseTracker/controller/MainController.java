package com.expenseTracker.controller;

import com.expenseTracker.constant.Type;
import com.expenseTracker.dto.TransactionFormDto;
import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Member;
import com.expenseTracker.entity.Transaction;
import com.expenseTracker.service.AssetService;
import com.expenseTracker.service.CategoryService;
import com.expenseTracker.service.MemberService;
import com.expenseTracker.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final TransactionService transactionService;

    private final MemberService memberService;

    private final AssetService assetService;
    private final CategoryService categoryService;

    @GetMapping(value = {"/", "/{page}"})
    public String main(HttpServletRequest request,
                       @PathVariable("page") Optional<Integer> page,
                       Model model, Principal principal) {
        // 로그인 되어 있지 않다면 로그인 페이지로
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        /* 내역 리스트 관련 */
        Long memberId = memberService.getMember(principal.getName()).getId();// 현재 로그인한 유저
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Transaction> items = transactionService.getPage(memberId, pageable);

        model.addAttribute("member", memberId);

        model.addAttribute("items", items);
        model.addAttribute("maxPage", 5);

        /* 모달 관련 */
        model.addAttribute("transactionFormDto", new TransactionFormDto());

        List<Asset> assets = assetService.getAssets(memberId);
        List<Category> incomeCategories = categoryService.getCategories(memberId, Type.INCOME);
        List<Category> expenseCategories = categoryService.getCategories(memberId, Type.EXPENSE);

        if (assets.isEmpty() || incomeCategories.isEmpty() || expenseCategories.isEmpty())
            model.addAttribute("errorMessage",
                    "설정/관리를 확인해주세요.\n(카테고리들은 각각 적어도 하나씩 존재해야 합니다.)");
        model.addAttribute("assets", assets);
        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("expenseCategories", expenseCategories);

        return "index";
    }
}