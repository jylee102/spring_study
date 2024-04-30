package com.board.repository;

import com.board.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByBoardId(Long boardId, Pageable pageable);

    List<Comment> findByBoardId(Long boardId);

    // 댓글 개수
    @Query("select count(c) from Comment c where c.board.id = :boardId")
    int countComment(@Param("boardId") Long boardId);
}
