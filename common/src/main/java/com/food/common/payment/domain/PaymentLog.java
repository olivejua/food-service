package com.food.common.payment.domain;

import com.food.common.basetime.BaseTimeEntity;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.common.user.domain.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import static com.food.common.payment.utils.PaymentValidationFailureMessages.PaymentLog.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Validated
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "tb_payment_log")
@Entity
public class PaymentLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "payment_log_id")
    private Long id;

    @Comment("결제")
    @NotNull(message = PAYMENT_CANNOT_BE_NULL)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Comment("결제 수단")
    @NotNull(message = METHOD_CANNOT_BE_NULL)
    @Enumerated(STRING)
    private PaymentMethod method;

    @Comment("결제 금액")
    @PositiveOrZero(message = AMOUNT_HAS_TO_BE_POSITIVE)
    @NotNull(message = AMOUNT_CANNOT_BE_NULL)
    private Integer amount;

    @Comment("사용 포인트")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    public static PaymentLog create(Long id, Payment payment, PaymentMethod method, Integer amount, Point point) {
        PaymentLog paymentLog = new PaymentLog();
        paymentLog.id = id;
        paymentLog.payment = payment;
        paymentLog.method = method;
        paymentLog.amount = amount;
        paymentLog.point = point;

        return paymentLog;
    }

    public static PaymentLog create(Payment payment, PaymentMethod method, Integer amount) {
        return create(null, payment, method, amount, null);
    }

    public void update(@NotNull Point point) {
        this.point = point;
    }

    public Long getPaymentId() {
        return payment.getId();
    }

    public Long getPointId() {
        return point.getId();
    }
}
