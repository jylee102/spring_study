package com.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private String name;
    private LocalDateTime regDate;
    private String content;

    @QueryProjection
    public CommentDto(String name, LocalDateTime regDate, String content) {
        this.name = name;
        this.regDate = regDate;
        this.content = content;
    }
}
