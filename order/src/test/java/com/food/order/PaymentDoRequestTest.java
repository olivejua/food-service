package com.food.order;

import com.food.common.error.CommonErrors;
import com.food.common.error.exception.InvalidRequestParameterException;
import com.food.common.payment.business.external.model.PaymentDoRequest;
import com.food.common.payment.enumeration.PaymentMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentDoRequestTest {

    @Test
    void 결제금액과_결제수단은_필수값이며_없으면_실패한다() {
        InvalidRequestParameterException error1 = assertThrows(
                InvalidRequestParameterException.class,
                () -> new PaymentDoRequest.Item(PaymentMethod.CARD, null));
        assertEquals(
                CommonErrors.INVALID_REQUEST_PARAMETERS.getCode(),
                error1.getErrorCode());
        assertTrue(error1.getMessage().contains("결제금액이 비어있습니다."));

        InvalidRequestParameterException error2 = assertThrows(
                InvalidRequestParameterException.class,
                () -> new PaymentDoRequest.Item(null, 30_000));
        assertTrue(error2.getMessage().contains("결제수단이 비어있습니다."));

        assertDoesNotThrow(() -> new PaymentDoRequest.Item(PaymentMethod.CARD, 30_000));
    }

    @Test
    void 결제금액은_0을_포함한_양수여야한다() {
        InvalidRequestParameterException error = assertThrows(
                InvalidRequestParameterException.class,
                () ->  new PaymentDoRequest.Item(PaymentMethod.CARD, -1_000));
        assertEquals(CommonErrors.INVALID_REQUEST_PARAMETERS.getCode(), error.getErrorCode());
        assertTrue(error.getMessage().contains("결제금액은 양수여야 합니다."));
    }

    @Test
    void 주문ID가_존재해야_한다() {
        PaymentDoRequest.Item item1 = new PaymentDoRequest.Item(PaymentMethod.CARD, 30_000);
        PaymentDoRequest.Item item2 = new PaymentDoRequest.Item(PaymentMethod.ACCOUNT_TRANSFER, 10_000);

        InvalidRequestParameterException error3 = assertThrows(
                InvalidRequestParameterException.class,
                () -> new PaymentDoRequest(null, item1, item2));
        assertTrue(error3.getMessage().contains("주문정보가 비어있습니다."));
    }

    @Test
    void 결제수단은_하나_이상이어야_한다() {
        InvalidRequestParameterException error3 = assertThrows(
                InvalidRequestParameterException.class,
                () -> new PaymentDoRequest(1L));
        assertTrue(error3.getMessage().contains("결제정보가 비어있습니다."));
    }

    @Test
    void 주문수단은_중복될_수_없다() {
        PaymentDoRequest.Item item1 = new PaymentDoRequest.Item(PaymentMethod.CARD, 30_000);
        PaymentDoRequest.Item item2 = new PaymentDoRequest.Item(PaymentMethod.CARD, 10_000);
        PaymentDoRequest.Item item3 = new PaymentDoRequest.Item(PaymentMethod.POINT, 5_000);

        InvalidRequestParameterException error3 = assertThrows(
                InvalidRequestParameterException.class,
                () -> new PaymentDoRequest(1L, item1, item2, item3));
        assertTrue(error3.getMessage().contains("중복된 결제수단은 존재할 수 없습니다."));

        PaymentDoRequest.Item item4 = new PaymentDoRequest.Item(PaymentMethod.CARD, 30_000);
        PaymentDoRequest.Item item5 = new PaymentDoRequest.Item(PaymentMethod.POINT, 5_000);
        assertDoesNotThrow((() -> new PaymentDoRequest(1L, item4, item5)));
    }
}
