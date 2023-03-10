package com.food.common.address.domain;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.food.common.address.utils.AddressFailureMessages.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Table(name = "tb_address")
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Comment("우편번호")
    @NotBlank(message = POST_CODE_CANNOT_BE_BLANK)
    @Length(max = 30, message = POST_CODE_IS_OUT_OF_LENGTH_OF_STRING)
    private String postCode;

    @Comment("시/도명")
    @NotBlank(message = SIDO_CANNOT_BE_BLANK)
    @Length(max = 30, message = SIDO_IS_OUT_OF_LENGTH_OF_STRING)
    private String sido;

    @Comment("시/군/구명")
    @NotBlank(message = SIGUNGU_CANNOT_BE_BLANK)
    @Length(max = 30, message = SIGUNGU_IS_OUT_OF_LENGTH_OF_STRING)
    private String sigungu;

    @Comment("도로명/지번 유형")
    @NotNull(message = TYPE_CANNOT_BE_NULL)
    @Enumerated(STRING)
    private Address.Type type;

    @Comment("도로명/지번 주소")
    @NotBlank(message = TYPE_ADDRESS_CANNOT_BE_BLANK)
    @Length(max = 100, message = TYPE_ADDRESS_IS_OUT_OF_LENGTH_OF_STRING)
    private String typeAddress;

    @Comment("본번")
    @NotNull(message = MAIN_NO_CANNOT_BE_NULL)
    private Short mainNo;

    @Comment("부번")
    @NotNull(message = SUB_NO_CANNOT_BE_NULL)
    private Short subNo;

    @Comment("참고 주소")
    @Length(max = 30, message = REFERENCE_ADDRESS_IS_OUT_OF_LENGTH_OF_STRING)
    private String referenceAddress;

    public static Address create(String postCode, String sido, String sigungu,
                                 Type type, String typeAddress, Short mainNo, Short subNo,
                                 String referenceAddress) {
        Address address = new Address();
        address.postCode = postCode;
        address.sido = sido;
        address.sigungu = sigungu;
        address.type = type;
        address.typeAddress = typeAddress;
        address.mainNo = mainNo;
        address.subNo = subNo;
        address.referenceAddress = referenceAddress;

        return address;
    }

    public Long getId() {
        return id;
    }

    public enum Type {
        ROAD("도로명"),
        JIBUN("지번")
        ;

        private final String description;

        Type(String description) {
            this.description = description;
        }
    }
}
