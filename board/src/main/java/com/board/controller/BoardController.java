package com.board.controller;

import com.board.dto.BoardFormDto;
import com.board.dto.BoardSearchDto;
import com.board.dto.CommentFormDto;
import com.board.entity.Board;
import com.board.entity.Comment;
import com.board.service.BoardService;
import com.board.service.CommentService;
import com.board.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping(value = "/") // localhost로 접속
    public String index(Principal principal, Model model) {
        if (principal != null)
            model.addAttribute("currUser", memberService.getMember(principal.getName()));
        return "index";
    }

    @GetMapping(value = "/write") // localhost/write
    public String write(Principal principal, Model model) {
        model.addAttribute("boardFormDto", new BoardFormDto());

        if (principal != null)
            model.addAttribute("currUser", memberService.getMember(principal.getName()));

        return "post/write";
    }

    @PostMapping(value = "/insert")
    public String insert(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) return "post/write";

        try {
            boardService.saveItem(boardFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage",
                    "게시물 등록 중 에러가 발생했습니다.");
            return "post/write";
        }

        return "redirect:/";
    }

    // 게시물 목록 페이지
    @GetMapping(value = {"/list", "/list/{page}"})
    public String boardManage(BoardSearchDto boardSearchDto,
                             @PathVariable("page") Optional<Integer> page,
                             Model model, Principal principal) {

        if (principal != null)
            model.addAttribute("currUser", memberService.getMember(principal.getName()));

        // PageRequest.of(페이지 번호, 한 페이지당 조회할 데이터 개수)
        // URL path에 페이지가 있으면 해당 페이지 번호를 조회하고, 페이지 번호가 없다면 0페이지(첫번째 페이지)를 조회
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<Board> items = boardService.getAdminItemPage(boardSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("boardSearchDto", boardSearchDto);

        // 상품 관리 페이지 하단에 보여줄 최대 페이지 번호
        model.addAttribute("maxPage", 5);

        return "post/list";
    }

    // 게시물 수정 페이지 보기
    @GetMapping(value = "/rewrite/{boardId}")
    public String boardDtl(@PathVariable("boardId") Long boardId, Model model, Principal principal) {

        if (principal != null)
            model.addAttribute("currUser", memberService.getMember(principal.getName()));

        try {
            Board board = boardService.getPostDtl(boardId);
            model.addAttribute("boardFormDto", board);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage",
                    "상품정보를 가져오는 도중 에러가 발생했습니다.");

            //에러 발생시 비어있는 객체를 넘겨준다.
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "post/rewrite"; // 등록화면으로 다시 이동
        }

        return "post/rewrite";
    }

    // 게시물 수정(update)
    @PostMapping(value = "/rewrite/{boardId}")
    public String boardUpdate(@Valid BoardFormDto boardFormDto, Model model, BindingResult bindingResult,
                             @PathVariable("boardId") Long boardId) {

        if(bindingResult.hasErrors()) return "post/rewrite"; //유효성 체크에서 걸리면

        Board board = boardService.getPostDtl(boardId);

        try {
            boardService.updateBoard(boardFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage",
                    "상품 수정 중 에러가 발생했습니다.");
            model.addAttribute("boardFormDto", board);
            return "post/rewrite";
        }

        return "redirect:/";

    }


    /* 댓글 */
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping(value = "/comment/insert")
    public @ResponseBody ResponseEntity<?> addComment(@RequestBody @Valid CommentFormDto commentFormDto, BindingResult bindingResult,
                                                      Principal principal) {
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();

        System.out.println(email);

        try {
            commentService.saveComment(commentFormDto, email);
            return ResponseEntity.ok().body("{\"message\": \"댓글이 등록되었습니다.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"댓글 등록 중 오류가 발생했습니다.\"}");
        }
    }

    // 게시물 보기 with comments
    @GetMapping(value = {"/view/{boardId}", "/view/{boardId}/{page}"})
    public String view(@PathVariable("boardId") Long boardId,
                       @PathVariable("page") Optional<Integer> page,
                       Model model, Principal principal) {

        if (principal != null)
            model.addAttribute("currUser", memberService.getMember(principal.getName()));

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<Comment> items = commentService.getComments(boardId, pageable);

        model.addAttribute("items", items);
        model.addAttribute("commentCount", commentService.countComment(boardId));

        // 상품 관리 페이지 하단에 보여줄 최대 페이지 번호
        model.addAttribute("maxPage", 3);

        Board board = boardService.getPostDtl(boardId);
        model.addAttribute("board", board);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", authentication.getName()); // 현재 인증된 사용자 이름 추가
        model.addAttribute("commentFormDto", new CommentFormDto());

        return "post/view";
    }

    // 게시물 삭제(delete)
    @DeleteMapping(value = "/delete/{boardId}")
    public @ResponseBody ResponseEntity deleteBoard(@PathVariable("boardId") Long boardId,
                                                    Principal principal) {
        // 1. 본인 확인
        if (!boardService.validateOrder(boardId, principal.getName())) {
            return new ResponseEntity<String>("삭제 권한이 없습니다.",
                    HttpStatus.FORBIDDEN);
        }

        // 2. 해당 게시물의 모든 댓글 삭제
        commentService.deleteComments(boardId);

        // 3. 게시물 삭제
        boardService.deleteBoard(boardId);

        return new ResponseEntity<Long>(boardId, HttpStatus.OK);
    }
}
