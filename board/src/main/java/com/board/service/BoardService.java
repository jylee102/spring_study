package com.board.service;

import com.board.dto.BoardFormDto;
import com.board.dto.BoardSearchDto;
import com.board.entity.Board;
import com.board.entity.Member;
import com.board.repository.BoardRepository;
import com.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Long saveItem(BoardFormDto boardFormDto) throws Exception {
        Board board = boardFormDto.createBoard();

        board.setMember(memberRepository.findByEmail(getCurrentUserId()));
        boardRepository.save(board);

        return board.getId();
    }

    public String getCurrentUserId() {
        // 로그인한 사용자의 정보를 가지고 온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";

        if (authentication != null) {
            userId = authentication.getName(); // 로그인한 사용자의 id(email)을 가지고 온다.
        }

        return userId;
    }

    // 게시물 세부정보 가져오기
    @Transactional(readOnly = true)
    public Board getPostDtl(Long itemId) {
        return boardRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
    }

    // 게시물 수정하기
    public Long updateBoard(BoardFormDto boardFormDto) throws Exception {
        // 1. item 엔티티 수정
        // ★ update를 진행하기 전 반드시 select를 해온다.
        // -> 영속성 컨텍스트에 item 엔티티가 없다면 가져오기 위해
        Board board = boardRepository.findById(boardFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        Long memberId = board.getMember().getId();
        boardFormDto.setMemberId(memberId);

        // update 실행
        // ★ 한 번 select를 진행하면 엔티티가 영속성 컨텍스트에 저장되고
        // 변경감지 기능으로 인해 트랜잭션 커밋 시점에 엔티티와 DB에 저장된 값이
        // 다르다면 JPA에서 update 해준다.
        board.updateBoard(boardFormDto);

        return board.getId(); // 변경한 item의 id 리턴
    }

    @Transactional(readOnly = true)
    public Page<Board> getAdminItemPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardRepository.getAdminItemPage(boardSearchDto, pageable);
    }

    // 본인 확인(현재 로그인한 사용자와 주문데이터를 생성한 사용자가 같은지 검사)
    @Transactional(readOnly = true)
    public boolean validateOrder(Long boardId, String email) {
        // 로그인한 사용자 찾기
        Member curMember = memberRepository.findByEmail(email);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        Member savedMember = board.getMember(); // 게시글 작성자 찾기

        // 로그인한 사용자의 이메일과 게시글 작성자 비교
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    // 게시물 삭제
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        boardRepository.delete(board); // delete
    }
}