package com.board.dto;

import com.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BoardFormDto {
    private Long id;

    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;

    private Long memberId;

    private static ModelMapper modelMapper = new ModelMapper();

    // dto -> entity
    public Board createBoard() {
        return modelMapper.map(this, Board.class);
    }

    // entity -> dto
    public static BoardFormDto of(Board board) {
        return modelMapper.map(board, BoardFormDto.class); // ItemFormDto 객체 리턴
    }
}
