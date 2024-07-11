package com.creativedesignproject.kumoh_board_backend.board.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.creativedesignproject.kumoh_board_backend.board.domain.Comment;
import com.creativedesignproject.kumoh_board_backend.board.domain.Favorite;
import com.creativedesignproject.kumoh_board_backend.board.domain.Image;
import com.creativedesignproject.kumoh_board_backend.board.domain.Post;
import com.creativedesignproject.kumoh_board_backend.board.domain.SubComment;
import com.creativedesignproject.kumoh_board_backend.board.dto.request.PatchBoardRequestDto;
import com.creativedesignproject.kumoh_board_backend.board.dto.request.PostBoardRequestDto;
import com.creativedesignproject.kumoh_board_backend.board.dto.request.PostCommentRequestDto;
import com.creativedesignproject.kumoh_board_backend.board.dto.request.PostSubCommentRequestDto;
import com.creativedesignproject.kumoh_board_backend.board.dto.response.PutFavoriteResponseDto;
import com.creativedesignproject.kumoh_board_backend.board.repository.CommentRepository;
import com.creativedesignproject.kumoh_board_backend.board.repository.FavoriteRepository;
import com.creativedesignproject.kumoh_board_backend.board.repository.PostRepository;
import com.creativedesignproject.kumoh_board_backend.board.repository.SubCommentRepository;
import com.creativedesignproject.kumoh_board_backend.board.repository.query.CategoryPostDto;
import com.creativedesignproject.kumoh_board_backend.board.repository.query.CommentDto;
import com.creativedesignproject.kumoh_board_backend.board.repository.query.FavoriteListDto;
import com.creativedesignproject.kumoh_board_backend.board.repository.query.ImageDto;
import com.creativedesignproject.kumoh_board_backend.board.repository.query.PostDto;
import com.creativedesignproject.kumoh_board_backend.board.repository.query.SubCommentDto;
import com.creativedesignproject.kumoh_board_backend.board.repository.query.UserDto;
import com.creativedesignproject.kumoh_board_backend.board.service.PostService;
import com.creativedesignproject.kumoh_board_backend.category.domain.Category;
import com.creativedesignproject.kumoh_board_backend.category.repository.CategoryRepository;
import com.creativedesignproject.kumoh_board_backend.common.exception.BadRequestException;
import com.creativedesignproject.kumoh_board_backend.common.exception.ErrorCode;
import com.creativedesignproject.kumoh_board_backend.auth.domain.User;
import com.creativedesignproject.kumoh_board_backend.auth.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public PostDto getPost(Long category_id, Long post_number) {
        boolean isExistedCategory = categoryRepository.existsByCategoryId(category_id);
        if(!isExistedCategory) throw new BadRequestException(ErrorCode.NOT_EXISTED_CATEGORY);

        boolean isExistedBoard = postRepository.existsByCategoryIdAndPostNumber(category_id, post_number);
        if(!isExistedBoard) throw new BadRequestException(ErrorCode.NOT_EXISTED_POST);
        
        Post post = postRepository.selectBoardWithImage(category_id, post_number);
        post.increaseViewCount(); // 따로 저장안해도 저장 됨 -> 영속성 컨텍스트 때문
        
        PostDto postDto = PostDto.builder()
                .categoryPostDto(buildCategoryPostDto(post))
                .userDto(buildUserDto(post))
                .title(post.getTitle())
                .contents(post.getContents())
                .favoriteCount(post.getFavorite_count())
                .commentCount(post.getComment_count())
                .viewCount(post.getView_count())
                .imageDto(buildImageDtoList(post))
                .build();
        return postDto;
    }

    @Transactional
    @Override
    public void registerPost(PostBoardRequestDto dto, Long category_id, String userId) {
        User user = userRepository.findByUserId(userId);
        if(user == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_USER);

        Category category = categoryRepository.findById(category_id);
        if(category == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_CATEGORY);

        Post post = Post.builder()
                .title(dto.getTitle())
                .contents(dto.getContent())
                .user(user)
                .category(category)
                .images(buildImageList(dto.getBoardImageList()))
                .build();
        postRepository.save(post);
    }

    @Transactional
    @Override
    public void deletePost(Long category_id, Long post_number,String userId) {
        boolean isExistedUser = userRepository.existsByUserId(userId);
        if(!isExistedUser) throw new BadRequestException(ErrorCode.NOT_EXISTED_USER);

        boolean isExistedPost = postRepository.existsByCategoryIdAndPostNumber(category_id, post_number);
        if(!isExistedPost) throw new BadRequestException(ErrorCode.NOT_EXISTED_POST);

        boolean isOwner = postRepository.isOwner(category_id, post_number, userId);
        if(!isOwner) throw new BadRequestException(ErrorCode.NO_PERMISSION);

        postRepository.deleteByPostNumberAndCategoryId(post_number, category_id);
    }

    @Transactional
    @Override
    public void patchPost(Long category_id, Long post_number, String userId, PatchBoardRequestDto dto) {
        User user = userRepository.findByUserId(userId);
        if(user == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_USER);

        Category category = categoryRepository.findById(category_id);
        if(category == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_CATEGORY);

        Post post = postRepository.findByCategoryIdAndPostNumber(category_id, post_number);
        if(post == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_POST);

        boolean isOwner = postRepository.isOwner(category_id, post_number, userId);
        if (!isOwner) throw new BadRequestException(ErrorCode.NO_PERMISSION);

        //변경 감지로 자동 저장
        post.setTitle(dto.getTitle());
        post.setContents(dto.getContent());
        post.setImages(buildImageList(dto.getBoardImageList()));
    }

    @Override
    public List<PostDto> getLatestBoardList(Long category_id) {
        List<PostDto> postList = postRepository.selectLatestBoardList(category_id);
        return postList;
    }

    @Override
    public List<PostDto> getTop3BoardList(Long category_id) {
        LocalDateTime beforeWeek = LocalDateTime.now().minusDays(7);
        List<PostDto> postList = postRepository.selectTop3BoardList(beforeWeek, category_id);
        return postList;
    }

    @Override
    public List<PostDto> getSearchBoardList(String search_word, String relation_word) {
        List<PostDto> postList = postRepository.selectSearchBoardList(search_word, relation_word);
        return postList;
    }

    @Override
    public List<PostDto> getUserBoardList(String userId) {
        boolean isExistedUser = userRepository.existsByUserId(userId);
        if(!isExistedUser) throw new BadRequestException(ErrorCode.NOT_EXISTED_USER);

        List<PostDto> postList = postRepository.selectUserBoardList(userId);
        return postList;
    }

    @Override
    public List<CategoryPostDto> getCategoryOfBoardList() {
        List<CategoryPostDto> postList = categoryRepository.findAllCategoryPostDtos();
        return postList;
    }

    @Transactional
    @Override
    public PutFavoriteResponseDto likeBoard(Long category_id, Long post_number, String userId) {
        User user = userRepository.findByUserId(userId);
        if(user == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_USER);

        Post post = postRepository.findById(post_number);
        if(post == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_POST);

        boolean favoriteExists = favoriteRepository.findByBoardNumberAndUserId(user.getId(), post_number);
        if (!favoriteExists) {
            Favorite favoriteEntity = Favorite.builder()
                .user(user)
                .post(post)
                .build();
            favoriteRepository.save(favoriteEntity);
            post.setFavorite_count(post.getFavorite_count() + 1);
        } else {
            favoriteRepository.delete(user.getId(), post_number);
            if (post.getFavorite_count() > 0) {
                post.setFavorite_count(post.getFavorite_count() - 1);
            }
        }

        return new PutFavoriteResponseDto(post.getFavorite_count());
    }

    @Transactional
    @Override
    public void addComment(Long category_id, Long post_number, String userId, PostCommentRequestDto dto) {
        User user = userRepository.findByUserId(userId);
        if(user == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_USER);

        Post post = postRepository.findById(post_number);
        if(post == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_POST);

        Comment comment = Comment.builder()
                .contents(dto.getContent())
                .user(user)
                .post(post)
                .build();
        
        commentRepository.save(comment);
        post.increaseCommentCount();
    }

    @Override
    public List<CommentDto> getCommentList(Long category_id, Long post_number) {
        List<Comment> comments = commentRepository.selectCommentsByPostNumber(post_number);
        List<CommentDto> commentList = new ArrayList<>();
        
        for (Comment comment : comments) {
            CommentDto commentDto = CommentDto.builder()
                .contents(comment.getContents())
                .userDto(UserDto.builder()
                    .nickname(comment.getUser().getNickname())
                    .profile_image(comment.getUser().getProfile_image())
                    .build())
                .subComments(buildSubCommentDtoList(comment.getSubcomments()))
                .build();
            
            commentList.add(commentDto);
        }
        return commentList;
    }

    private List<SubCommentDto> buildSubCommentDtoList(List<SubComment> subComments) {
        return subComments.stream()
            .map(subComment -> SubCommentDto.builder()
                .content(subComment.getContent())
                .userDto(UserDto.builder()
                    .nickname(subComment.getParentComment().getUser().getNickname())
                    .profile_image(subComment.getParentComment().getUser().getProfile_image())
                    .build())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<FavoriteListDto> getFavoriteList(Long category_id, Long post_number) {
        boolean existedBoard = postRepository.existsByPostNumberAndCategoryId(post_number, category_id);
        if(!existedBoard) throw new BadRequestException(ErrorCode.NOT_EXISTED_POST);

        List<FavoriteListDto> favoriteList = favoriteRepository.findByPostId(post_number);

        return favoriteList;
    }

    @Transactional
    @Override
    public void addSubComment(Long category_id, Long post_number, String userId, PostSubCommentRequestDto dto) {
        boolean isExisted = userRepository.existsByUserId(userId);
        if (!isExisted) throw new BadRequestException(ErrorCode.NOT_EXISTED_USER);
        
        User user = userRepository.findByUserId(userId);
        if(user == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_USER);

        Post post = postRepository.findById(post_number);
        if(post == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_POST);

        Comment parentComment = commentRepository.findById(dto.getParent_comment_id());
        if(parentComment == null) throw new BadRequestException(ErrorCode.NOT_EXISTED_COMMENT);

        SubComment subCommentEntity = SubComment.builder()
            .content(dto.getContent())
            .parentComment(parentComment)
            .build();

        subCommentRepository.save(subCommentEntity);
    }

    private CategoryPostDto buildCategoryPostDto(Post post) {
        return CategoryPostDto.builder()
            .categoryName(post.getCategory().getName())
            .build();
    }

    private UserDto buildUserDto(Post post) {
        return UserDto.builder()
                .nickname(post.getUser().getNickname())
                .profile_image(post.getUser().getProfile_image())
                .build();
    }
    
    private List<ImageDto> buildImageDtoList(Post post) {
        return post.getImages().stream()
                .map(image -> ImageDto.builder()
                        .url(image.getUrl())
                        .build())
                .collect(Collectors.toList());
    }

    private List<Image> buildImageList(List<String> boardImageList) {
        return boardImageList.stream()
                .map(url -> Image.builder()
                        .url(url)
                        .build())
                .collect(Collectors.toList());
    }
}
