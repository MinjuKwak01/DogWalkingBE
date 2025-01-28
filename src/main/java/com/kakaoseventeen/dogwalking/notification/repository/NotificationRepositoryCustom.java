package com.kakaoseventeen.dogwalking.notification.repository;

import com.kakaoseventeen.dogwalking.notification.dto.response.HomeResDTO;
import com.kakaoseventeen.dogwalking.notification.dto.response.QHomeResDTO_NotificationDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kakaoseventeen.dogwalking.notification.domain.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<HomeResDTO.NotificationDTO> searchWithNonLogin(NotificationSearchCondition condition) {

        var distanceExpression = Expressions.numberTemplate(Double.class,
                "6371.0 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({4})) * sin(radians({5})))",
                condition.getLatitude(), notification.lat, notification.lng, condition.getLongitude(), condition.getLatitude(), notification.lat);

        return queryFactory
                .select(new QHomeResDTO_NotificationDTO(
                        notification.id.as("notificationId"),
                        notification.title,
                        notification.lat,
                        notification.lng,
                        notification.dog.name.as("dogName"),
                        notification.dog.image.as("dogImage"),
                        notification.dog.age.as("dogAge"),
                        notification.dog.sex.as("dogSex"),
                        notification.dog.breed.as("dogBreed"),
                        notification.dog.member.dogBowl.as("dogBowl")
                ))
                .from(notification)
                .join(notification.dog.member)
                .where(
                        buildWhereClause(condition, distanceExpression)
                )
                .orderBy(distanceExpression.asc())
                .limit(condition.getPageable().getPageSize())
                .fetch();
    }


    private BooleanBuilder buildWhereClause(NotificationSearchCondition condition, NumberExpression<Double> distanceExpression) {
        BooleanBuilder whereClause = new BooleanBuilder();

        whereClause.and(titleContains(condition.getWord()));
        whereClause.and(keyContains(distanceExpression, condition.getKey()));

        BooleanBuilder orClause = new BooleanBuilder();
        if (condition.getBig() != null) {
            orClause.or(sizeIn(condition.getBig()));
        }

        orClause.or(breedIn(condition.getBreed()));

        if (orClause.hasValue()) {
            whereClause.and(orClause);
        }

        return whereClause;
    }

    // 검색어가 파라미터에 포함되어있거나 검색어가 없을 경우
    private BooleanExpression titleContains(String tit) {
        return tit != null ? notification.title.containsIgnoreCase(tit) : null;
    }

    //강아지 크기에 따라 필터링하고 싶을 경우
    private BooleanExpression sizeIn(List<String> size) {
        return size != null ? notification.dog.size.in(size) : null;
    }

    //강아지 견종에 따라 필터링하고 싶을 경우
    private BooleanExpression breedIn(List<String> breed) {
        return breed != null ? notification.dog.breed.in(breed) : null;
    }

    //키값이 파라미터에 포함되어있거나 없을 경우
    private BooleanExpression keyContains(NumberExpression<Double> distanceExpression, Double key) {
        return key != null ? distanceExpression.gt(key) : null;
    }



}
