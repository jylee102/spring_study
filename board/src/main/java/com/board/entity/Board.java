package com.board.entity;

import com.board.dto.BoardFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board extends BaseTimeEntity {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "longtext")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateBoard(BoardFormDto boardFormDto) {
        this.id = boardFormDto.getId();
        this.title = boardFormDto.getTitle();
        this.content = boardFormDto.getContent();
        this.member = boardFormDto.createBoard().getMember();
    }
}
