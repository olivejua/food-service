package com.food.common.store.domain;

import com.food.common.user.domain.StoreOwner;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Table(name = "tb_store")
@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Comment("상호명")
    @NotBlank
    private String name;

    @Comment("가게 사장님")
    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_owner_id")
    private StoreOwner owner;

    @Comment("최소 주문 금액")
    @PositiveOrZero
    private Integer minOrderAmount;

    @Comment("운영 상태")
    @NotNull
    @Enumerated(STRING)
    private OpenStatus status;

    public static Store create(String name, StoreOwner owner, Integer minOrderAmount, OpenStatus status) {
        Store store = new Store();
        store.name = name;
        store.owner = owner;
        store.minOrderAmount = minOrderAmount;
        store.status = status;

        return store;
    }

    public enum OpenStatus {
        OPEN("운영 중"),
        CLOSED("운영 종료")
        ;

        private final String description;

        OpenStatus(String description) {
            this.description = description;
        }
    }

}
