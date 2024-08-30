package com.creativedesignproject.kumoh_board_backend.category.domain.repository;

import java.util.List;

import static com.creativedesignproject.kumoh_board_backend.category.domain.entity.QCategory.category;
import static com.creativedesignproject.kumoh_board_backend.board.domain.entity.QPost.post;

import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.CategoryPostDto;
import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.PostDto;
import com.creativedesignproject.kumoh_board_backend.board.domain.repository.query.UserDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements QuerydslCategoryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CategoryPostDto> findAllCategoryPostDtos() {
        return queryFactory
                .select(
                    Projections.constructor(CategoryPostDto.class,
                        category.name,
                        Projections.list(
                            Projections.constructor(PostDto.class,
                            post.title,
                            post.contents,
                            post.favorite_count,
                            post.comment_count,
                            post.view_count,
                            Projections.constructor(UserDto.class, post.user.nickname, post.user.profileImage),
                            Projections.constructor(CategoryPostDto.class, post.category.name, Expressions.constant(null)),
                            post.updatedDate,
                            Expressions.constant(null)
                        )
                    )
                ))
                .from(category)
                .leftJoin(post).on(category.id.eq(post.category.id))
                .fetch();
    }
}
