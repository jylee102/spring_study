package com.example.spring_semi_project.controller;

import com.example.spring_semi_project.dto.Course;
import com.example.spring_semi_project.dto.CourseTime;
import com.example.spring_semi_project.dto.Enroll;
import com.example.spring_semi_project.service.CourseService;
import com.example.spring_semi_project.service.MemberService;
import com.example.spring_semi_project.util.ConvertStringUtil;
import com.example.spring_semi_project.util.PagingUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class CourseController {
    @Autowired
    CourseService courseService;

    @Autowired
    MemberService memberService;

    @Autowired
    PagingUtil pagingUtil;

    @Autowired
    ConvertStringUtil convertStringUtil;

    @RequestMapping(value = "/",
            method = {RequestMethod.GET, RequestMethod.POST})
    public String index(HttpSession session, HttpServletRequest request, Model model) {
        try {
            String univName = (String) session.getAttribute("univName");
            String id = (String) session.getAttribute("member_id");

            if (univName == null || id == null) {
                return "redirect:/login"; // 세션 만료시 로그인 페이지로 이동
            }

            String pageNum = request.getParameter("pageNum");
            pagingUtil.setCurrentPage(1); // 페이지 번호 항상 1로 우선 초기화

            // 현재 페이지의 값을 바꿔준다.
            if (pageNum != null) pagingUtil.setCurrentPage(Integer.parseInt(pageNum));

            String searchValue1 = request.getParameter("searchValue1"); // 검색 키워드
            String searchValue2 = request.getParameter("searchValue2"); // 검색어

            if (searchValue1 == null) {
                searchValue1 = "";
            }
            if (searchValue2 == null) {
                searchValue2 = "";
            }

            Map map = new HashMap();
            map.put("univName", univName);
            map.put("searchValue1", searchValue1);
            map.put("searchValue2", searchValue2);
            map.put("id", id);

            String role = (String) session.getAttribute("role");
            switch (role) {
                case "Staff" -> model.addAttribute("member", memberService.loginStaff(map));
                case "Professor" -> model.addAttribute("member", memberService.loginProfessor(map));
                case "Student" -> model.addAttribute("member", memberService.loginStudent(map));
            }

            // 1. 전체 게시물의 개수를 가져온다.(페이징 처리시 필요)
            int dataCount = courseService.getMyDataCount(map);

            // 2. 페이징 처리를 한다.(준비 단계)
            // numPerPage: 페이지단 보여줄 게시물 목록의 개수
            pagingUtil.resetPaging(dataCount, 5);

            map.put("start", pagingUtil.getStart());
            map.put("end", pagingUtil.getEnd());

            // 3. 페이징 처리할 리스트를 가지고 온다.
            List<Course> lists = courseService.getMyCourse(map);

            // 4. 검색어에 대한 쿼리 스트링을 만든다.
            String param = "";
            String listUrl = "/index";

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

            for (Course c : lists) {
                map.put("courseCode", c.getCourseCode());
                c.getCourseDayStr().add(convertStringUtil.convertToKoreanDayOfWeek(courseService.getCourseInfo(map)));
            }

            System.out.println(model.getAttribute("member"));

            model.addAttribute("lists", lists); // DB에서 가져온 전체 게시물 리스트
            model.addAttribute("pageIndexList", pageIndexList); // 페이징 버튼
            model.addAttribute("dataCount", dataCount); // 게시물의 전체 개수
            model.addAttribute("searchValue1", searchValue1); // 검색키워드
            model.addAttribute("searchValue2", searchValue2); // 검색어

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "/index";
    }

    @RequestMapping(value = "/list",
            method = {RequestMethod.GET, RequestMethod.POST})
    public String list(HttpSession session, HttpServletRequest request, Model model) {
        try {
            String univName = (String) session.getAttribute("univName");
            String id = (String) session.getAttribute("member_id");

            if (univName == null || id == null) {
                return "redirect:/login"; // 세션 만료시 로그인 페이지로 이동
            }

            String pageNum = request.getParameter("pageNum");
            pagingUtil.setCurrentPage(1); // 페이지 번호 항상 1로 우선 초기화

            // 현재 페이지의 값을 바꿔준다.
            if (pageNum != null) pagingUtil.setCurrentPage(Integer.parseInt(pageNum));

            String searchValue1 = request.getParameter("searchValue1"); // 검색 키워드
            String searchValue2 = request.getParameter("searchValue2"); // 검색어

            if (searchValue1 == null) {
                searchValue1 = "";
            }
            if (searchValue2 == null) {
                searchValue2 = "";
            }

            Map map = new HashMap();
            map.put("univName", univName);
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

            for (Course c : lists) {
                map.put("courseCode", c.getCourseCode());
                c.getCourseDayStr().add(convertStringUtil.convertToKoreanDayOfWeek(courseService.getCourseInfo(map)));
            }

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

    // 시간 중복 확인
    public boolean isCourseTimeOverlapped(Course course, String userId) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("univName", course.getUnivName());
            map.put("id", userId);

            for (CourseTime courseTime : course.getCourseTime()) {
                map.put("courseDay", courseTime.getCourseDay());
                map.put("startTime", courseTime.getStartTime());
                map.put("endTime", courseTime.getEndTime());

                int overlappingCount = courseService.countOverlappingCourses(map);

                if (overlappingCount > 0) {
                    return true; // 중복 수업 발견
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false; // 중복 수업 없음
    }

    @PostMapping(value = "/enroll/{majorCode}/{courseCode}")
    public ResponseEntity<String> enrollCourse(@PathVariable("majorCode") String majorCode,
                                               @PathVariable("courseCode") String courseCode,
                                               HttpSession session) {
        try {
            Object univName = session.getAttribute("univName");
            Object id = session.getAttribute("member_id");

            if (univName == null || id == null) {
                return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
            }

            Enroll enroll = new Enroll();
            enroll.setUnivName((String) univName);
            enroll.setId((String) id);
            enroll.setMajorCode(majorCode);
            enroll.setCourseCode(courseCode);

            if (courseService.isEnrolled(enroll)) {
                return new ResponseEntity<>("이미 신청된 강좌입니다.", HttpStatus.BAD_REQUEST);
            }

            Map map = new HashMap();
            map.put("univName", enroll.getUnivName());
            map.put("courseCode", enroll.getCourseCode());

            if (isCourseTimeOverlapped(courseService.getCourseInfo(map), (String) id)) {
                return new ResponseEntity<>("해당 강좌의 시간과 중복되는 강좌가 있습니다.", HttpStatus.BAD_REQUEST);
            }
            courseService.enroll(enroll);
            courseService.updateRestSeat(enroll);

            return new ResponseEntity<>("강좌 수강이 성공적으로 등록되었습니다.", HttpStatus.OK);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>("이미 신청된 강좌입니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("강좌 수강 등록에 실패했습니다. 관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{courseCode}")
    public @ResponseBody ResponseEntity deleteCourse(@PathVariable("courseCode") String courseCode,
                                                     HttpSession session) {
        try {
            Object univName = session.getAttribute("univName");
            Object id = session.getAttribute("member_id");

            if (univName == null || id == null) {
                return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
            }

            Map map = new HashMap();
            map.put("univName", univName);
            map.put("id", id);
            map.put("courseCode", courseCode);

            courseService.deleteCourse(map);
            courseService.restoreRestSeat(map);

            return new ResponseEntity<String>("성공적으로 삭제되었습니다.", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("삭제에 실패하였습니다. 관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/timeTable")
    public String timeTable(HttpSession session, HttpServletRequest request, Model model) {
        try {
            String univName = (String) session.getAttribute("univName");
            String id = (String) session.getAttribute("member_id");

            if (univName == null || id == null) {
                return "redirect:/login"; // 세션 만료시 로그인 페이지로 이동
            }

            Map map = new HashMap();
            map.put("univName", univName);
            map.put("id", id);
            map.put("start", 1);
            map.put("end", 100);

            List<Course> lists = courseService.getMyCourse(map);

            for (Course c : lists) {
                map.put("courseCode", c.getCourseCode());
                c.setCourseTime(courseService.getCourseInfo(map).getCourseTime());
            }

            String[] daysOfWeek = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
            model.addAttribute("courses", lists);
            model.addAttribute("daysOfWeek", daysOfWeek);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "student/timeTable";
    }
}
