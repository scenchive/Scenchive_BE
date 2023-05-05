package com.example.scenchive.comment.service;

import com.example.scenchive.board.repository.BoardRepository;
import com.example.scenchive.comment.Comment;
import com.example.scenchive.comment.dto.CommentSaveDto;
import com.example.scenchive.comment.repository.CommentRepository;
import com.example.scenchive.member.exception.MemberException;
import com.example.scenchive.member.exception.MemberExceptionType;
import com.example.scenchive.member.repository.MemberRepository;
import org.apache.catalina.security.SecurityUtil;

import java.security.Security;
import java.util.List;

public class CommentServicelmpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public void save(Long boardId, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmMember(memberRepository.findByName(SecurityUtil.getLoginMemberid()).orElseThrow(()
                -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        comment.confirmBoard(boardRepository.findById(boardId).orElseThrow(()
                -> new BoardException(BoardExceptionType.POST_NOT_FOUND)));

        commentRepository.save(comment);
    }

    @Override
    public void saveReply(Long boardId, Long parentId, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmMember(memberRepository.findByName(SecurityUtil.getLoginMemberid()).orElseThrow(()
                -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        comment.confirmBoard(boardRepository.findById(boardId).orElseThrow(()
                -> new BoardException(BoardExceptionType.BOARD_NOT_FOUND)));

        comment.confirmComment(commentRepository.findById(parentId).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT)));

        commentRepository.save(comment);
    }

    @Override
    public void remove(Long id) throws CommentException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));

        if(!comment.getMember().getMemberid().equals(SecurityUtil.getLoginUserid())) {
            throw new CommentException(CommentExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }

        comment.remove();
        List<Comment> removableCommentList = comment.findRemovableList();
        commentRepository.deleteAll(removableCommentList);
    }
}
