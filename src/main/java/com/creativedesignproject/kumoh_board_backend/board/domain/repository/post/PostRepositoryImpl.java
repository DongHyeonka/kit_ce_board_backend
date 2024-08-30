package com.creativedesignproject.kumoh_board_backend.board.domain.repository.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.creativedesignproject.kumoh_board_backend.board.domain.entity.QPost.post;
import static com.creativedesignproject.kumoh_board_backend.board.domain.entity.QImage.image;
import static com.creativedesignproject.kumoh_board_backend.auth.domain.entity.QUser.user;
import static com.creativedesignproject.kumoh_board_backend.category.domain.entity.QCategory.category;

import com.creativedesignproject.kumoh_board_backend.board.domain.entity.Post;
import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.CategoryPostDto;
import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.ImageDto;
import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.PostDto;
import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.UserDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements QuerydslPostRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> selectBoardWithImage(Long categoryId, Long postId) {
        return Optional.ofNullable(
            queryFactory
            .selectFrom(post)
            .leftJoin(post.images).fetchJoin()
            .where(post.category.id.eq(categoryId)
                .and(post.id.eq(postId)))
            .fetchOne()
        );
    }

    @Override
    public boolean isOwner(Long categoryId, Long postId, String userId) {
        return queryFactory
            .selectOne()
            .from(post)
            .where(post.category.id.eq(categoryId)
                .and(post.id.eq(postId))
                .and(post.user.userId.eq(userId)))
            .fetchFirst() != null;
    }

    @Override
    public List<PostDto> selectLatestBoardList(Long category_id, LocalDateTime sevenDaysAgoDate) {
        return queryFactory
            .select(Projections.constructor(PostDto.class,
                    post.title,
                    post.contents,
                    post.favorite_count,
                    post.comment_count,
                    post.view_count,
                    Projections.constructor(UserDto.class, post.user.nickname, post.user.profileImage),
                    Projections.constructor(CategoryPostDto.class, post.category.name, Expressions.constant(null)),
                    post.updatedDate,
                    Projections.list(
                        Projections.constructor(ImageDto.class, image.url)
                    )
            ))
            .from(post)
            .join(post.user, user).fetchJoin()
            .join(post.category, category).fetchJoin()
            .leftJoin(post.images, image).fetchJoin() // 이미지가 있는 경우에만 즉시 로딩
            .where(post.category.id.eq(category_id)
                .and(post.updatedDate.goe(sevenDaysAgoDate)))
            .orderBy(post.updatedDate.desc())
            .fetch();
    }

    @Override
    public List<PostDto> selectTop3BoardList(LocalDateTime sevenDaysAgoDate, Long category_id) {
        return queryFactory
                .select(Projections.constructor(PostDto.class,
                post.title,
                post.contents,
                post.favorite_count,
                post.comment_count,
                post.view_count,
                Projections.constructor(UserDto.class, post.user.nickname, post.user.profileImage),
                Projections.constructor(CategoryPostDto.class, post.category.name, Expressions.constant(null)),
                post.updatedDate,
                Projections.constructor(ImageDto.class, image.url)
            ))
            .from(post)
            .join(post.user, user).fetchJoin()
            .join(post.category, category).fetchJoin()
            .leftJoin(post.images, image).fetchJoin()
            .where(post.category.id.eq(category_id)
                .and(post.updatedDate.goe(sevenDaysAgoDate)))
            .orderBy(post.favorite_count.desc())
            .fetch();
    }

    @Override
    public List<PostDto> selectSearchBoardList(String searchWord, String relationWord) {
        return queryFactory
            .select(Projections.constructor(PostDto.class,
                post.title,
                post.contents,
                post.favorite_count,
                post.comment_count,
                post.view_count,
                Projections.constructor(UserDto.class, post.user.nickname, post.user.profileImage),
                Projections.constructor(CategoryPostDto.class, post.category.name, Expressions.constant(null)),
                post.updatedDate,
                Projections.constructor(ImageDto.class, image.url)
            ))
            .from(post)
            .join(post.user, user).fetchJoin()
            .join(post.category, category).fetchJoin()
            .leftJoin(post.images, image).fetchJoin()
            .where(
                post.title.containsIgnoreCase(searchWord)
                    .or(post.contents.containsIgnoreCase(searchWord))
                    .or(post.user.nickname.containsIgnoreCase(searchWord))
                    .and(post.title.containsIgnoreCase(relationWord)
                        .or(post.contents.containsIgnoreCase(relationWord))
                        .or(post.user.nickname.containsIgnoreCase(relationWord))
                    )
            )
            .orderBy(post.updatedDate.desc())
            .fetch();
    }

    @Override
    public List<PostDto> selectUserBoardList(String userId) {
        return queryFactory
            .select(Projections.constructor(PostDto.class,
                post.title,
                post.contents,
                post.favorite_count,
                post.comment_count,
                post.view_count,
                Projections.constructor(UserDto.class, post.user.nickname, post.user.profileImage),
                Projections.constructor(CategoryPostDto.class, post.category.name, Expressions.constant(null)),
                post.updatedDate,
                Projections.constructor(ImageDto.class, image.url)
            ))
            .from(post)
            .join(post.user, user).fetchJoin()
            .join(post.category, category).fetchJoin()
            .leftJoin(post.images, image).fetchJoin()
            .where(post.user.userId.eq(userId))
            .orderBy(post.updatedDate.desc())
            .fetch();
    }
}
