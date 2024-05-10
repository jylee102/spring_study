package com.expenseTracker.controller;

import com.expenseTracker.constant.Type;
import com.expenseTracker.dto.TransactionFormDto;
import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Transaction;
import com.expenseTracker.service.AssetService;
import com.expenseTracker.service.CategoryService;
import com.expenseTracker.service.MemberService;
import com.expenseTracker.service.TransactionService;
import com.expenseTracker.util.TransactionSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

        // 필요한 데이터를 미리 로드
        loadInitialData(model, memberId, pageable);

        /* 모달 관련 */
        model.addAttribute("transactionFormDto", new TransactionFormDto());

        return "index";
    }

    @GetMapping(value = {"/loadItems", "/loadItems/{page}"})
    @ResponseBody
    public Page<Transaction> loadItems(HttpServletRequest request, Principal principal,
                                       @PathVariable("page") Optional<Integer> page) {
        Long memberId = memberService.getMember(principal.getName()).getId();// 현재 로그인한 유저
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        return transactionService.getPage(memberId, pageable);
    }

    private void loadInitialData(Model model, Long memberId, Pageable pageable) {
        try {
            Page<Transaction> items = transactionService.getPage(memberId, pageable);

            model.addAttribute("member", memberId);
            model.addAttribute("items", items);
            model.addAttribute("maxPage", 5);

            List<Asset> assets = assetService.getAssets(memberId);
            List<Category> incomeCategories = categoryService.getCategories(memberId, Type.INCOME);
            List<Category> expenseCategories = categoryService.getCategories(memberId, Type.EXPENSE);

            if (assets.isEmpty() || incomeCategories.isEmpty() || expenseCategories.isEmpty()) {
                model.addAttribute("errorMessage",
                        "설정/관리를 확인해주세요.\n(카테고리들은 각각 적어도 하나씩 존재해야 합니다.)");
            }
            model.addAttribute("assets", assets);
            model.addAttribute("incomeCategories", incomeCategories);
            model.addAttribute("expenseCategories", expenseCategories);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "데이터를 불러오는 것에 실패했습니다. 관리자에게 문의하세요.");
        }
    }

    @GetMapping(value = {"/view/calendar", "/view/calendar/{page}"})
    public String calendar(HttpServletRequest request,
                           Model model, Principal principal) {
        // 로그인 되어 있지 않다면 로그인 페이지로
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        // Gson 객체 생성 시 커스텀 어댑터 등록
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new TransactionSerializer())
                .create();

        Long memberId = memberService.getMember(principal.getName()).getId();// 현재 로그인한 유저

        List<Transaction> transactions = transactionService.getList(memberId);
        String json = gson.toJson(transactions); // List<Transaction>을 JSON 문자열로 변환

        model.addAttribute("data", json);

        return "calendar";
    }

    @GetMapping(value = "/settings/assets")
    public String assets(Model model, Principal principal) {
        try {
            Long memberId = memberService.getMember(principal.getName()).getId();
            List<Asset> assets = assetService.getAssets(memberId);

            model.addAttribute("assets", assets);
        } catch (Exception e) {
            return "/members/login/error";
        }
        return "settings/assets";
    }

    @GetMapping(value = "/settings/categories")
    public String categories(Model model, Principal principal) {
        try {
            Long memberId = memberService.getMember(principal.getName()).getId();
            List<Category> incomeCategories = categoryService.getCategories(memberId, Type.INCOME);
            List<Category> expenseCategories = categoryService.getCategories(memberId, Type.EXPENSE);
            model.addAttribute("incomeCategories", incomeCategories);
            model.addAttribute("expenseCategories", expenseCategories);
        } catch (Exception e) {
            return "/members/login/error";
        }
        return "settings/categories";
    }
}