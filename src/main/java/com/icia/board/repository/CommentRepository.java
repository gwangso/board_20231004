package com.icia.board.repository;

import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByBoardEntity(BoardEntity boardEntity);
}
