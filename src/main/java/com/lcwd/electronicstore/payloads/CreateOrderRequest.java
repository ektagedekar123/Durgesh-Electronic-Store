package com.lcwd.electronicstore.payloads;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateOrderRequest {

    private String userId;

    private String cartId;

    private String orderStatus="PENDING";

    private String paymentStatus="NOTPAID";

    private String billingAddress;

    private String billingPhone;

    private String billingName;


}
