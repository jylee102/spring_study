package com.example.spring_semi_project.controller;

import com.example.spring_semi_project.dto.Course;
import com.example.spring_semi_project.service.CourseService;
import com.example.spring_semi_project.util.PagingUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    CourseService courseService;

    @Autowired
    PagingUtil pagingUtil;

    @GetMapping(value = "/") // localhost로 접속
    public String index(HttpSession session) {
        if (session.getAttribute("univ") == null) return "member/login";
        else {
            return "index";
        }
    }

    // localhost/list
    // 같은 경로로 get 방식과 post 방식을 동시에 받을 수 있다.
    @RequestMapping(value = "/list",
            method = {RequestMethod.GET, RequestMethod.POST})
    public String list(HttpSession session, HttpServletRequest request, Model model) {
        try {
            String pageNum = request.getParameter("pageNum");
            pagingUtil.setCurrentPage(1); // 페이지 번호 항상 1로 우선 초기화

            // 현재 페이지의 값을 바꿔준다.
            if (pageNum != null) pagingUtil.setCurrentPage(Integer.parseInt(pageNum));

            String univ = (String) session.getAttribute("univ");
            String searchValue1 = request.getParameter("searchValue1"); // 검색 키워드
            String searchValue2 = request.getParameter("searchValue2"); // 검색어

            if (searchValue1 == null) {
                searchValue1 = "";
            }
            if (searchValue2 == null) {
                searchValue2 = "";
            }

            Map map = new HashMap();
            map.put("univ", univ);
            map.put("searchValue1", searchValue1);
            map.put("searchValue2", searchValue2);

            // 1. 전체 게시물의 개수를 가져온다.(페이징 처리시 필요)
            int dataCount = courseService.getDataCount(map);

            // 2. 페이징 처리를 한다.(준비 단계)
            // numPerPage: 페이지단 보여줄 게시물 목록의 개수
            pagingUtil.resetPaging(dataCount, 5);

            map.put("start", pagingUtil.getStart());
            map.put("end", pagingUtil.getEnd());

            // 3. 페이징 처리할 리스트를 가지고 온다.
            List<Course> lists = courseService.getCourseList(map);

            // 4. 검색어에 대한 쿼리 스트링을 만든다.
            String param = "";
            String listUrl = "/list";

            // 검색어가 있다면
            if (searchValue1 != null && !searchValue1.equals("")) {
                param = "searchValue1=" + searchValue1;
            }

            if (searchValue2 != null && !searchValue2.equals("")) {
                param += "&searchValue2=" + URLEncoder.encode(searchValue2, "UTF-8");
            }

            // 검색어가 있다면
            if (!param.equals("")) {
                listUrl += "?" + param;
            }

            // 5. 페이징 버튼을 만들어준다.
            // ◀이전 1 2 3 4 5 다음▶
            String pageIndexList = pagingUtil.pageIndexList(listUrl);

            model.addAttribute("lists", lists); // DB에서 가져온 전체 게시물 리스트
            model.addAttribute("pageIndexList", pageIndexList); // 페이징 버튼
            model.addAttribute("dataCount", dataCount); // 게시물의 전체 개수
            model.addAttribute("searchValue1", searchValue1); // 검색키워드
            model.addAttribute("searchValue2", searchValue2); // 검색어

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "student/enroll";
    }
}
