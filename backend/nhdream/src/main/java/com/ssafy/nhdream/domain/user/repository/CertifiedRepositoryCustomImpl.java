package com.ssafy.nhdream.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.nhdream.domain.admin.dto.FarmerReviewContentDto;
import com.ssafy.nhdream.domain.admin.dto.QFarmerReviewContentDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.ssafy.nhdream.entity.user.QCertified.certified;
import static com.ssafy.nhdream.entity.user.QUser.user;

public class CertifiedRepositoryCustomImpl implements CertifiedRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CertifiedRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<FarmerReviewContentDto> getFarmerReviewList(String username, Pageable pageable) {

        List<FarmerReviewContentDto> content = queryFactory
                .select(new QFarmerReviewContentDto(
                        certified.id,
                        certified.user.name,
                        certified.licenseNum,
                        certified.createdAt,
                        certified.approval
                ))
                .from(certified)
                .where(
                        usernameEq(username)
                )
                .orderBy(certified.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery =  queryFactory
                .select(certified.count())
                .from(certified)
                .where(
                        usernameEq(username)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? user.name.eq(username) : null;
    }
}
