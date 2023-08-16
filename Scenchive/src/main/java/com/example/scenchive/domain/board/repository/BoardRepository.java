package com.example.scenchive.domain.board.repository;

import com.example.scenchive.domain.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
//

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    //게시판 테이블에서 게시글 id로 내림차순 정렬해 가져오기
    @Query("SELECT b FROM Board b ORDER BY b.id DESC")
    List<Board> findAllDesc();

    List<Board> findByBoardtype(boardType boardtype);

    List<Board> findByMember(Member member);
}
