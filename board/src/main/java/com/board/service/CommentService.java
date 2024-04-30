package com.board.service;

import com.board.dto.CommentFormDto;
import com.board.entity.Board;
import com.board.entity.Comment;
import com.board.entity.Member;
import com.board.repository.BoardRepository;
import com.board.repository.CommentRepository;
import com.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 댓글 등록
    public Long saveComment(CommentFormDto commentFormDto, String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다: " + email);
        }

        Board board = boardRepository.findById(commentFormDto.getBoardId())
                .orElseThrow(EntityNotFoundException::new);

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setMember(member);
        comment.setContent(commentFormDto.getContent());

        return commentRepository.save(comment).getId();
    }

    @Transactional(readOnly = true)
    public Page<Comment> getComments(Long boardId, Pageable pageable) {
        return commentRepository.findByBoardId(boardId, pageable);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        commentRepository.delete(comment); // delete
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsAll(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    // 게시물의 댓글 전체 삭제
    public void deleteComments(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);

        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }
    }

    public int countComment(Long boardId) {
        return commentRepository.countComment(boardId);
    }
}
