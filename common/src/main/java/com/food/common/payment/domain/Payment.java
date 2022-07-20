package com.food.common.payment.domain;

import com.food.common.common.domain.BaseTimeEntity;
import com.food.common.user.domain.User;
import com.food.common.common.domain.Point;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Entity @Table(name = "tb_payment")
public class Payment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private Integer amount;

    @Enumerated(STRING)
    private Method method;

    @Enumerated(STRING)
    private Status status;

    @Embedded
    private PaymentPoints points;

    public Payment(@PositiveOrZero @NotNull final Integer amount,
                   @NotNull final Method method, @NotNull final Status status,
                   @NotNull final PaymentPoints points) {
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public enum Status {
        PAYMENT_REQUESTED,
        PAYMENT_COMPLETED,
        CANCELLATION_REQUESTED,
        CANCELLATION_COMPLETED
    }

    public enum Method {
        CARD,
        ACCOUNT_TRANSFER
    }

    @NoArgsConstructor(access = PROTECTED)
    @Embeddable
    public static class PaymentPoints {
        private Integer beforePoint;
        private Integer afterPoint;

        public static PaymentPoints use(final User user, final Point usingPoint) {
            PaymentPoints paymentPoints = new PaymentPoints();
            paymentPoints.beforePoint = user.getPoint().get();
            paymentPoints.afterPoint = user.use(usingPoint).get();

            return paymentPoints;
        }

        public static PaymentPoints notUse(final User user) {
            PaymentPoints paymentPoints = new PaymentPoints();
            paymentPoints.beforePoint = user.getPoint().get();
            paymentPoints.afterPoint = user.getPoint().get();

            return paymentPoints;
        }
    }
}
