package com.food.common.user.domain;

import com.food.common.common.domain.BaseTimeEntity;
import com.food.common.common.domain.Point;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Table(name = "tb_user")
@Entity
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String loginId;

    private String password;

    private String nickname;

    private Integer point;

    public User(final String loginId, final String password, final String nickname, final Point point) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.point = point.get();
    }

    public Point use(final Point point) {
        Point currentPoint = getPoint().subtract(point);
        this.point = currentPoint.get();

        return currentPoint;
    }

    public Point getPoint() {
        return new Point(point);
    }

    public Long getId() {
        return id;
    }
}
