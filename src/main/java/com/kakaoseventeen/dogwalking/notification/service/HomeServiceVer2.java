package com.kakaoseventeen.dogwalking.notification.service;

import com.kakaoseventeen.dogwalking._core.security.CustomUserDetails;
import com.kakaoseventeen.dogwalking._core.utils.CursorRequest;
import com.kakaoseventeen.dogwalking.notification.domain.Notification;
import com.kakaoseventeen.dogwalking.notification.dto.response.HomeResDTO;
//import com.kakaoseventeen.dogwalking.notification.repository.NotificationRepositoryCustom;
import com.kakaoseventeen.dogwalking.notification.repository.NotificationRepositoryCustom;
import com.kakaoseventeen.dogwalking.notification.repository.NotificationSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HomeServiceVer2 {
    private final NotificationRepositoryCustom notificationRepositoryCustom;

    @Transactional(readOnly = true)
    public HomeResDTO home(CursorRequest cursorRequest, Double lat, Double lng, List<String> big, List<String> breed, String word, CustomUserDetails customUserDetails){

        String role = SecurityContextHolder.getContext().getAuthentication().getName();


            int pageSize = cursorRequest.hasSize() ? cursorRequest.getSize() : 20;

            Pageable pageable = PageRequest.of(0, pageSize);
            NotificationSearchCondition condition = new NotificationSearchCondition(pageable, big, breed, word, lat, lng, cursorRequest.getKey());

            List<HomeResDTO.NotificationDTO> notifications = notificationRepositoryCustom.searchWithNonLogin(condition);

            Double lastKey = getLastKey(notifications, lat, lng);

            return new HomeResDTO(cursorRequest.next(lastKey, pageSize), notifications, null);


    }

    private static Double getLastKey(List<HomeResDTO.NotificationDTO> notifications, Double curLat, Double curLng) {
        if (notifications.isEmpty()) {
            return CursorRequest.NONE_KEY;
        } else {
            Double lat = notifications.get(notifications.size() - 1).getLat();
            Double lng = notifications.get(notifications.size() - 1).getLng();

            final Double R = 6371.0;

            // 위도 및 경도를 라디안으로 변환
            Double latRad = Math.toRadians(lat);
            Double lngRad = Math.toRadians(lng);
            Double curLatRad = Math.toRadians(curLat);
            Double curLngRad = Math.toRadians(curLng);

            // 위도 및 경도의 차이 계산
            Double dLat = curLatRad - latRad;
            Double dLng = curLngRad - lngRad;

            // 하버사인 공식을 사용하여 거리 계산
            Double formula = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(latRad) * Math.cos(curLatRad) *
                            Math.sin(dLng / 2) * Math.sin(dLng / 2);

            Double result = 2 * Math.atan2(Math.sqrt(formula), Math.sqrt(1 - formula));

            Double finalResult =  R * result;
            return finalResult + 0.000000000001;
        }
    }



}
