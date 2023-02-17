package com.food.api.order;

import com.food.SuperIntegrationTest;
import com.food.common.order.domain.Order;
import com.food.common.order.enumeration.OrderStatus;
import com.food.common.order.repository.OrderRepository;
import com.food.common.payment.business.external.PayService;
import com.food.common.payment.business.external.model.PaymentDoRequest;
import com.food.common.payment.domain.Payment;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.common.store.domain.Store;
import com.food.common.store.domain.StoreOwner;
import com.food.common.user.business.external.model.RequestUser;
import com.food.common.user.domain.Point;
import com.food.common.user.domain.User;
import com.food.common.user.enumeration.AccountType;
import com.food.common.user.repository.PointRepository;
import com.food.mock.order.MockOrder;
import com.food.mock.user.MockPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PayApiTests extends SuperIntegrationTest {
    private final String DOCUMENT_AUTH = "payment/do-pay/";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PointRepository pointRepository;

    private Store mockStore;

    @Autowired
    private PayService payService;

    @BeforeEach
    void setup() {
        User storeOwnerUser = userFactory.user("i'm a storeowner");
        StoreOwner mockStoreOwner = storeOwnerFactory.storeOwner(storeOwnerUser);
        mockStore = storeFactory.store(mockStoreOwner);
    }

    @Test
    void shouldSavePayment() throws Exception {
        //given
        // 50,000의 주문 요청한다.
        Order mockOrder = MockOrder.builder()
                .customer(mockUser)
                .store(mockStore)
                .amount(50000)
                .status(OrderStatus.REQUEST)
                .build();
        orderRepository.save(mockOrder);

        //기본포인트는 1,000원 적립되어 있다.
        Point point = MockPoint.builder()
                .user(mockUser)
                .changedAmount(1000)
                .currentAmount(1000)
                .payment(null)
                .build();
        pointRepository.save(point);

        //결제 요청 Request: 포인트 1,000원 사용에 49,000원 카드 결제 요청한다.
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(),
                new PaymentDoRequest.Item(PaymentMethod.POINT, 1000),
                new PaymentDoRequest.Item(PaymentMethod.CARD, mockOrder.getAmount() - 1000));

        //when & then
        mvc.perform(post("/api/payments")
                        .header(ACCEPT, APPLICATION_JSON_VALUE)
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, createAuthentication())
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andDo(document(DOCUMENT_AUTH + "success",
                        requestHeaders(
                                headerWithName(ACCEPT).description("accept"),
                                headerWithName(CONTENT_TYPE).description("content type"),
                                headerWithName(AUTHORIZATION).description("access token")
                        ),
                        requestFields(
                                fieldWithPath("orderId").type(NUMBER).description("주문 고유번호"),
                                fieldWithPath("items[].method").type(STRING).description("결제 수단"),
                                fieldWithPath("items[].amount").type(NUMBER).description("결제 금액")
                        ),
                        responseFields(
                                fieldWithPath("success").type(BOOLEAN).description("정상 처리 여부")
                        )
                ));
    }

    @Test
    void shouldCancelPayment() throws Exception {
        //given
        // 기본포인트는 2000원 있다.
        Payment basepayment = paymentFactory.payment(orderFactory.order(mockUser, mockStore));
        pointFactory.point(mockUser, 2000, basepayment);

        // 5만원의 주문 요청한다.
        Order mockOrder = orderFactory.order(mockUser, mockStore, 50000, OrderStatus.REQUEST);

        // 포인트사용 1000원, 카드결제 49000원의 결제를 한다.
        PaymentDoRequest request = new PaymentDoRequest(mockOrder.getId(),
                new PaymentDoRequest.Item(PaymentMethod.POINT, 1000),
                new PaymentDoRequest.Item(PaymentMethod.CARD, mockOrder.getAmount() - 1000));

        Long paymentId = payService.pay(request, new RequestUser() {
            @Override
            public Long getUserId() {
                return mockUser.getId();
            }

            @Override
            public Long getAccountId() {
                return null;
            }

            @Override
            public AccountType getAccountType() {
                return null;
            }
        });

        mvc.perform(delete("/api/payments/{paymentId}", paymentId)
                        .header(ACCEPT, APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, createAuthentication()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andDo(document(DOCUMENT_AUTH + "success",
                        requestHeaders(
                                headerWithName(ACCEPT).description("accept"),
                                headerWithName(AUTHORIZATION).description("access token")
                        ),
                        pathParameters(
                                parameterWithName("paymentId").description("취소할 결제의 paymentId")
                        ),
                        responseFields(
                                fieldWithPath("success").type(BOOLEAN).description("정상 처리 여부")
                        )
                ));
    }
}
