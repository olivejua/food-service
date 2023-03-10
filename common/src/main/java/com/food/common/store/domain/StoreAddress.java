package com.food.common.store.domain;

import com.food.common.address.domain.Address;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.food.common.store.utils.StoreValidationFailureMessages.StoreAddress.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Table(name = "tb_store_address")
@Entity
public class StoreAddress {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "store_address_id")
    private Long id;

    @Comment("가게")
    @NotNull(message = STORE_CANNOT_BE_NULL)
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Comment("가게 소재지")
    @NotNull(message = ADDRESS_CANNOT_BE_NULL)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Comment("직접입력 상세주소")
    @Length(max = 150, message = ADDRESS_DETAIL_HAS_TO_BE_BETWEEN_LENGTH)
    private String addressDetail;

    public static StoreAddress create(Store store, Address address, String addressDetail) {
        StoreAddress storeAddress = new StoreAddress();
        storeAddress.store = store;
        storeAddress.address = address;
        storeAddress.addressDetail = addressDetail;

        return storeAddress;
    }
}
