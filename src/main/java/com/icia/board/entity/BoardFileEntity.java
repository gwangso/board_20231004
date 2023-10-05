package com.icia.board.entity;

import com.icia.board.util.UtilClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "board_file_table")
@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
public class BoardFileEntity extends UtilClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String originalFileName;

    @Column(length = 100)
    private String storedFileName;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private BoardEntity boardId;
}
