package com.icia.board.entity;

import com.icia.board.util.UtilClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "board_file_table")
@Entity
public class BoardFileEntity extends UtilClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String originalFileName;

    @Column(length = 100)
    private String storedFileName;

    // BoardFile : Board = 다 대 일
    @ManyToOne(fetch = FetchType.LAZY) // fetch
    @JoinColumn(name = "board_id") // DB에 생성될 참조 컬럼의 이름
    private BoardEntity boardEntity; //부모 엔티티 타입으로 정의

    public static BoardFileEntity toSaveBoardFile(String originalFileName, String storedFileName, BoardEntity savedEntity) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(savedEntity);
        return boardFileEntity;
    }
}