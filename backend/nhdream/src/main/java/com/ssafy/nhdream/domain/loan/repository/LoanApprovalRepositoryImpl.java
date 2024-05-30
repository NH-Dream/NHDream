package com.ssafy.nhdream.domain.loan.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.nhdream.domain.admin.dto.LoanReviewContentDto;
import com.ssafy.nhdream.domain.admin.dto.QLoanReviewContentDto;
import com.ssafy.nhdream.entity.user.ApprovalStatus;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.ssafy.nhdream.entity.loan.QLoanApproval.loanApproval;
import static com.ssafy.nhdream.entity.loan.QLoanOptions.loanOptions;
import static com.ssafy.nhdream.entity.user.QUser.user;

public class LoanApprovalRepositoryImpl implements LoanApprovalRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    public LoanApprovalRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public Page<LoanReviewContentDto> getLoanReviewList(String username, String reviewStatus, Pageable pageable) {

        List<LoanReviewContentDto> content = queryFactory
                .select(new QLoanReviewContentDto(
                        loanApproval.id,
                        loanApproval.user.name,
                        loanApproval.loanOptions.loanProduct.name,
                        loanApproval.createdAt,
                        loanApproval.approval,
                        loanOptions.amount,
                        loanOptions.rate
                ))
                .from(loanApproval)
                .where(
                        usernameEq(username),
                        reviewStatusEq(reviewStatus)
                )
                .orderBy(loanApproval.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(loanApproval.count())
                .from(loanApproval)
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
        return "ALL".equals(reviewStatus) ? null : loanApproval.approval.eq(ApprovalStatus.valueOf(reviewStatus));
    }
}
