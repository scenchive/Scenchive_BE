package com.example.scenchive.domain.comment.service;

import com.example.scenchive.domain.comment.repository.Comment;
import com.example.scenchive.domain.comment.dto.CommentSaveDto;

import java.util.List;

//== Comment 엔티티에 대한 CRUD 작업을 수행하는 인터페이스 정의 ==//
//== 실제 데이터베이스 조작 수행 ==//
public interface CommentService {

    void save(Comment comment); // Comment 엔티티 저장
    void saveReply(Long boardId, Long parendId, CommentSaveDto commentSaveDto);

    Comment findById(Long id) throws Exception; // 해당 id의 Comment 엔티티 검색, 존재하지 않으면 예외 발생

    List<Comment> findAll(); // 저장된 모든 Comment 엔티티 검색, List 형태로 리턴

    void remove(Long id) throws Exception; // 해당 id의 Comment 엔티티 삭제, 존재하지 않으면 예외 발생
}
