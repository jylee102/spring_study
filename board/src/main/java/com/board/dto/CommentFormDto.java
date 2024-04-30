package com.board.dto;

import com.board.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class CommentFormDto {
    private Long boardId;

    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;

    private static ModelMapper modelMapper = new ModelMapper();

    public Comment createComment() {
        return modelMapper.map(this, Comment.class);
    }
}
