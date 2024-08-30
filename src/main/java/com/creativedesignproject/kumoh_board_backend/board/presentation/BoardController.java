package com.creativedesignproject.kumoh_board_backend.board.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.CategoryPostDto;
import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.CommentDto;
import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.FavoriteListDto;
import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.PostDto;
import com.creativedesignproject.kumoh_board_backend.board.dto.request.PatchBoardRequestDto;
import com.creativedesignproject.kumoh_board_backend.board.dto.request.PostBoardRequestDto;
import com.creativedesignproject.kumoh_board_backend.board.dto.request.PostCommentRequestDto;
import com.creativedesignproject.kumoh_board_backend.board.dto.request.PostSubCommentRequestDto;
import com.creativedesignproject.kumoh_board_backend.board.dto.response.PutFavoriteResponseDto;
import com.creativedesignproject.kumoh_board_backend.board.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {
    private final PostService boardService;
    
    @GetMapping("/{category_id}/{post_number}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long category_id, @PathVariable Long post_number) {
        PostDto postDto = boardService.getPost(category_id, post_number);
        return ResponseEntity.status(HttpStatus.OK).body(postDto);
    }

    @PostMapping("/{category_id}/register")
    public ResponseEntity<Void> registerPost(@RequestBody @Valid PostBoardRequestDto dto, @PathVariable Long category_id, String userId) {
        boardService.registerPost(dto, category_id, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{category_id}/{post_number}")
    public ResponseEntity<Void> deletePost(@PathVariable Long category_id, @PathVariable Long post_number, String userId) {
        boardService.deletePost(category_id, post_number, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{category_id}/{post_number}")
    public ResponseEntity<Void> patchPost(@PathVariable Long category_id, @PathVariable Long post_number, String userId,@RequestBody @Valid PatchBoardRequestDto dto) {
        boardService.patchPost(category_id, post_number, userId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{category_id}/latest_list")
    public ResponseEntity<List<PostDto>> getLatestBoardList(@PathVariable Long category_id) {
        List<PostDto> latestBoardList = boardService.getLatestBoardList(category_id);
        return ResponseEntity.status(HttpStatus.OK).body(latestBoardList);
    }

    @GetMapping("/{category_id}/Top_3")
    public ResponseEntity<List<PostDto>> getTop3BoardList(@PathVariable Long category_id) {
        List<PostDto> top3BoardList = boardService.getTop3BoardList(category_id);
        return ResponseEntity.status(HttpStatus.OK).body(top3BoardList);
    }

    @GetMapping(value = {"/Search_list/{search_word}", "/Search_list/{search_word}/{relation_word}"})
    public ResponseEntity<List<PostDto>> getSearchBoardList(@PathVariable String search_word, @PathVariable(required=false) String relation_word) {
        List<PostDto> searchBoardList = boardService.getSearchBoardList(search_word, relation_word);
        return ResponseEntity.status(HttpStatus.OK).body(searchBoardList);
    }

    @GetMapping("/user-board-list/{userId}")
    public ResponseEntity<List<PostDto>> getUserBoardList(@PathVariable("userId") String userId) {
        List<PostDto> userBoardList = boardService.getUserBoardList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userBoardList);
    }

    @GetMapping("/main-list")
    public ResponseEntity<List<CategoryPostDto>> getCategoryOfBoardList() {
        List<CategoryPostDto> categoryOfBoardList = boardService.getCategoryOfBoardList();
        return ResponseEntity.status(HttpStatus.OK).body(categoryOfBoardList);
    }

    @PutMapping("/{category_id}/{post_number}/favorite")
    public ResponseEntity<PutFavoriteResponseDto> likeBoard(@PathVariable Long category_id, @PathVariable Long post_number, String userId) {
        PutFavoriteResponseDto putFavoriteResponseDto = boardService.likeBoard(category_id, post_number, userId);
        return ResponseEntity.status(HttpStatus.OK).body(putFavoriteResponseDto);
    }

    @PostMapping("/{category_id}/{post_number}/comment")
    public ResponseEntity<Void> addComment(@PathVariable Long category_id, @PathVariable Long post_number, String userId, @RequestBody @Valid PostCommentRequestDto dto) {
        boardService.addComment(category_id, post_number, userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/{category_id}/{post_number}/favorite-list")
    public ResponseEntity<List<FavoriteListDto>> getFavoriteList(
            @PathVariable Long category_id,
            @PathVariable Long post_number) {
        List<FavoriteListDto> favoriteList = boardService.getFavoriteList(category_id, post_number);
        return ResponseEntity.status(HttpStatus.OK).body(favoriteList);
    }

    @PostMapping("/{category_id}/{post_number}/subComment")
    public ResponseEntity<Void> addSubComment(@PathVariable Long category_id,
            @PathVariable Long post_number, String userId,
            @RequestBody @Valid PostSubCommentRequestDto dto) {
        boardService.addSubComment(category_id, post_number, userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{category_id}/{post_number}/comment-list")
    public ResponseEntity<List<CommentDto>> getCommentList(@PathVariable Long category_id, @PathVariable Long post_number) {
        List<CommentDto> commentList = boardService.getCommentList(category_id, post_number);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }
}