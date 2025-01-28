package com.kakaoseventeen.dogwalking.notification.dto.response;

import com.kakaoseventeen.dogwalking._core.utils.CursorRequest;
import com.kakaoseventeen.dogwalking.notification.domain.Notification;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

/**
 * HomeResDTO(메인페이지(홈) 응답 DTO)
 *
 * @author 곽민주
 * @version 1.0
 */
@Getter
public class HomeResDTO {

    private CursorRequest nextCursorRequest;
    private List<NotificationDTO> notifications;
    private String image;

    public HomeResDTO(CursorRequest nextCursorRequest, List<NotificationDTO> notifications, String image) {
        this.nextCursorRequest = nextCursorRequest;
        this.notifications = notifications;
        this.image = image;
    }

    @Getter
    public static class NotificationDTO {
        private Long notificationId;
        private String title;
        private Double lat;
        private Double lng;
        private DogInfo dogInfo;
        private int dogBowl;

        @QueryProjection
        public NotificationDTO(Long notificationId, String title, Double lat, Double lng, String dogName, String dogImage, int dogAge, String dogSex, String dogBreed, int dogBowl) {
            this.notificationId = notificationId;
            this.title = title;
            this.lat = lat;
            this.lng = lng;
            this.dogInfo = new DogInfo(dogName, dogImage, dogAge, dogSex, dogBreed);
            this.dogBowl = dogBowl;
        }
    }

        /*
        public NotificationDTO(Notification post) {
            this(
                    post.getId(),
                    post.getTitle(),
                    post.getLat(),
                    post.getLng(),
                    new DogInfo(
                            post.getDog().getName(),
                            post.getDog().getImage(),
                            post.getDog().getAge(),
                            post.getDog().getSex(),
                            post.getDog().getBreed()
                    ),
                    post.getDog().getMember().getDogBowl()
            );
        }
    }

         */

    @Getter
    public static class DogInfo {
        private String name;
        private String image;
        private int age;
        private String sex;
        private String breed;

        public DogInfo(String name, String image, int age, String sex, String breed) {
            this.name = name;
            this.image = image;
            this.age = age;
            this.sex = sex;
            this.breed = breed;
        }
    }


}