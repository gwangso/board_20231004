package com.icia.board.repository;

import com.icia.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity,Long>{
    /*
        update board_table
        set board_hits=board_hits + 1
        where id=?

        jpql(java persistence query language)
    */
    @Modifying // insert, update, delete 를 사용할 때
    // 엔티티 기준으로 작성, 외부에서 받아서 사용할 파라미터는 클론(:)으로 구분
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id = :id")
    // nativeQuery방식, 꼭 뒤에 nativeQuery = true가 필요하다.
//    @Query(value = "update board_table set board_hits=board_hits where id=:id", nativeQuery = true)
    void increaseHits(@Param("id") Long id);

}