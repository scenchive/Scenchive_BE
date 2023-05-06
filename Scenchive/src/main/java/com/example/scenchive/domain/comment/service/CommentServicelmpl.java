package com.example.scenchive.domain.comment.service;

import com.example.scenchive.domain.board.exception.BoardException;
import com.example.scenchive.domain.board.exception.BoardExceptionType;
import com.example.scenchive.domain.board.repository.BoardRepository;
import com.example.scenchive.domain.comment.exception.CommentException;
import com.example.scenchive.domain.comment.exception.CommentExceptionType;
import com.example.scenchive.domain.comment.repository.Comment;
import com.example.scenchive.domain.comment.dto.CommentSaveDto;
import com.example.scenchive.domain.comment.repository.CommentRepository;
import com.example.scenchive.domain.member.exception.MemberException;
import com.example.scenchive.domain.member.exception.MemberExceptionType;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.global.util.security.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServicelmpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public void save(Long boardId, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmMember(memberRepository.findByName(SecurityUtil.getLoginUsername()).orElseThrow(()
                -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        comment.confirmBoard(boardRepository.findById(boardId).orElseThrow(()
                -> new BoardException(BoardExceptionType.BOARD_NOT_FOUND)));

        commentRepository.save(comment);
    }

    @Override
    public void saveReply(Long boardId, Long parentId, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmMember(memberRepository.findByName(SecurityUtil.getLoginUsername()).orElseThrow(()
                -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        comment.confirmBoard(boardRepository.findById(boardId).orElseThrow(()
                -> new BoardException(BoardExceptionType.BOARD_NOT_FOUND)));

        comment.confirmComment(commentRepository.findById(parentId).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT)));

        commentRepository.save(comment);
    }

    @Override
    public void remove(Long id) throws CommentException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));

        if(!comment.getMember().getName().equals(SecurityUtil.getLoginUsername())) {
            throw new CommentException(CommentExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }

        comment.remove();
        List<Comment> removableCommentList = comment.findRemovableList();
        commentRepository.deleteAll(removableCommentList);
    }
}
