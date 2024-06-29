package com.yukgaejang.inflearnclone.domain.board.application;

import com.yukgaejang.inflearnclone.domain.board.dao.BoardDao;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.domain.Tag;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardDetailDto;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardDto;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardListDto;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardDao boardDao;

    public List<BoardListDto> getAllPosts() {
        return boardDao.findAll().stream().map(this::convertToBoardListDto).collect(Collectors.toList());
    }

    public Optional<BoardDetailDto> getPostDTOById(Long id) {
        return boardDao.findById(id).map(this::convertToBoardDetailDto);
    }

    public Optional<Board> getPostById(Long id) {
        return boardDao.findById(id);
    }

    public Board createPost(Board board, User user, String title, String content, String category, Set<Tag> tags) {
        board.createPost(user, title, content, category, tags);
        return boardDao.save(board);
    }

    public Board updatePost(Board board, String title, String content, String category, Set<Tag> tags) {
        board.updatePost(title, content, category, tags);
        return boardDao.save(board);
    }

    public void deletePost(Long id) {
        boardDao.deleteById(id);
    }

    //Board -> BoardDto
    public BoardDto convertToDTO(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .likeCount(board.getLikeCount())
                .viewCount(board.getViewCount())
                .userNickname(board.getUser().getNickname())
                .tags(board.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .build();
    }

    //Board -> BoardListDto
    private BoardListDto convertToBoardListDto(Board board) {
        return BoardListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .category(board.getCategory())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .userNickname(board.getUser().getNickname())
                .build();
    }

    //Board -> BoardDetailDto
    public BoardDetailDto convertToBoardDetailDto(Board board) {
        return BoardDetailDto.builder()
                .id(board.getId())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .likeCount(board.getLikeCount())
                .viewCount(board.getViewCount())
                .userNickname(board.getUser().getNickname())
                .tags(board.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .build();
    }
}
