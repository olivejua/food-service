package com.food.order.order.temp;

import com.food.common.error.ApplicationErrors;
import com.food.common.utils.Amount;
import com.food.order.error.OrderErrors;

public class NotEnoughOrderAmountException extends RuntimeException {
    private static final ApplicationErrors ERROR = OrderErrors.NOT_ENOUGH_ORDER_AMOUNT;

    public NotEnoughOrderAmountException(Amount amount) {
        super(ERROR.appendMessage("Amount=" + amount));
    }
}
