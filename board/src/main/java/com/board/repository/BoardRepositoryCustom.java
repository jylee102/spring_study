package com.board.repository;

import com.board.dto.BoardSearchDto;
import com.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> getAdminItemPage(BoardSearchDto boardSearchDto, Pageable pageable);
}
