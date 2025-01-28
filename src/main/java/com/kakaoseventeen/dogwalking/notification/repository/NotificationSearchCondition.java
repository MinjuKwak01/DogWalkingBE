package com.kakaoseventeen.dogwalking.notification.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@AllArgsConstructor
@Getter
public class NotificationSearchCondition {
    /*
     *   DB에서 공고글을 찾는 조건
     */
    private Pageable pageable;
    private List<String> big;
    private List<String> breed;
    private String word;
    private Double latitude;
    private Double longitude;
    private Double key;
}