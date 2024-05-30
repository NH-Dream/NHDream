package com.ssafy.nhdream.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.nhdream.domain.admin.dto.QTrainingReviewContentDto;
import com.ssafy.nhdream.domain.admin.dto.TrainingReviewContentDto;
import com.ssafy.nhdream.entity.user.ApprovalStatus;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.ssafy.nhdream.entity.loan.QLoanApproval.loanApproval;
import static com.ssafy.nhdream.entity.user.QEducation.education;
import static com.ssafy.nhdream.entity.user.QUser.user;

public class EducationRepositoryCustomImpl implements EducationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public EducationRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<TrainingReviewContentDto> getTrainingReviewList(String username, String reviewStatus, Pageable pageable) {

        List<TrainingReviewContentDto> content = queryFactory
                .select(new QTrainingReviewContentDto(
                        education.id,
                        education.user.name,
                        education.name,
                        education.createdAt,
                        education.approval
                ))
                .from(education)
                .where(
                        usernameEq(username),
                        reviewStatusEq(reviewStatus)
                )
                .orderBy(education.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(education.count())
                .from(education)
                .where(
                        usernameEq(username),
                        reviewStatusEq(reviewStatus)
                );


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? user.name.eq(username) : null;
    }

    private BooleanExpression reviewStatusEq(String reviewStatus){
        return "ALL".equals(reviewStatus) ? null : education.approval.eq(ApprovalStatus.valueOf(reviewStatus));
    }

}
