package com.example.scenchive.domain.board.service;

import com.example.scenchive.domain.board.dto.BoardResponseDto;
import com.example.scenchive.domain.board.repository.Board;
import com.example.scenchive.domain.board.repository.BoardRepository;
import com.example.scenchive.domain.board.dto.BoardListResponseDto;
import com.example.scenchive.domain.board.dto.BoardSaveRequestDto;
import com.example.scenchive.domain.board.dto.BoardUpdateRequestDto;
import com.example.scenchive.domain.board.repository.boardType;
import com.example.scenchive.domain.comment.repository.CommentRepository;
import com.example.scenchive.domain.member.dto.BookmarkPerfumeDto;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
//
@Service
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    private final S3Uploader s3Uploader;

    @Autowired
    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository, MemberService memberService, S3Uploader s3Uploader) {
        this.boardRepository = boardRepository;
        this.memberRepository=memberRepository;
        this.memberService=memberService;
        this.s3Uploader=s3Uploader;
    }

    //게시물 등록 메소드 : 토큰과 BoardSaveRequestDto(게시물 제목, 내용, 게시판 카테고리) 넘겨주기
    @Transactional
    public Long save(MultipartFile image, BoardSaveRequestDto requestDto) throws IOException {
        Member member = memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get();
        String title = requestDto.getTitle();
        String body = requestDto.getBody();
        boardType boardtype = requestDto.getBoardtype();
        String imageUrl = null;

        if (!image.isEmpty()) {
            try{
                imageUrl = s3Uploader.upload(image, "board"); //board라는 이름의 디렉토리 생성 후 그 안에 파일 저장
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        Board board = Board.builder()
                .member(member)
                .title(title)
                .body(body)
                .boardtype(boardtype)
                .imageUrl(imageUrl)
                .build();

        boardRepository.save(board);
        return board.getId();
    }

    //게시물 수정 메소드
    @Transactional
    public Long update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        board.update(requestDto.getTitle(), requestDto.getBody(), requestDto.getBoardtype());
        return id;
    }

    //게시물 삭제 메소드
    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        boardRepository.delete(board);
    }

    //개별 게시물 조회 메소드
    @Transactional
    public BoardResponseDto findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        LocalDateTime localDateTime=board.getModified_at();
        String modifiedAt = localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

        BoardResponseDto boardResponseDto=new BoardResponseDto(board.getBoardtype().getBoardtype_name(), board.getTitle(), board.getBody(),
                board.getMember().getName(), board.getImageUrl(), modifiedAt);

        return boardResponseDto;
    }

    //게시판 전체 조회 메소드
    //findAllDesc()의 결과로 반환된 Board들을 BoardListResponseDto로 변환하고 List로 변환
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> findAllDesc(Pageable pageable) {
        List<BoardListResponseDto> boards= new ArrayList<>();
        boards=boardRepository.findAllDesc().stream()
                .map(board -> new BoardListResponseDto(board))
                .collect(Collectors.toList());

        List<BoardListResponseDto> pagingBoards=new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), boards.size());

        List<BoardListResponseDto> paginatedboards = new ArrayList<>(boards).subList(startIndex, endIndex);

        for(BoardListResponseDto board : paginatedboards){
            BoardListResponseDto boardListResponseDto=new BoardListResponseDto(board.getId(), board.getBoardtype_name(), board.getTitle());
            pagingBoards.add(boardListResponseDto);
        }

        return pagingBoards;
    }

    //카테고리별 게시판 조회 메소드
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> findByBoardtype(int boardtype_id, Pageable pageable){
        boardType boardtype=new boardType(boardtype_id);

        List<BoardListResponseDto> boards=new ArrayList<>();

        boards = boardRepository.findByBoardtype(boardtype).stream()
                .map(board->new BoardListResponseDto(board))
                .collect(Collectors.toList());

        List<BoardListResponseDto> pagingBoards=new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), boards.size());

        List<BoardListResponseDto> paginatedboards = new ArrayList<>(boards).subList(startIndex, endIndex);

        for(BoardListResponseDto board : paginatedboards){
            BoardListResponseDto boardListResponseDto=new BoardListResponseDto(board.getId(), board.getBoardtype_name(), board.getTitle());
            pagingBoards.add(boardListResponseDto);
        }

        return pagingBoards;
    }
}
