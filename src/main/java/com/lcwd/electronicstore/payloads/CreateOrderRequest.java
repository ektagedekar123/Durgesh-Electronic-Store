package com.lcwd.electronicstore.payloads;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateOrderRequest {

    @NotBlank(message= "User Id is required!!")
    private String userId;

    @NotBlank(message= "Cart id is required!!")
    private String cartId;

    private String orderStatus="PENDING";

    private String paymentStatus="NOTPAID";

    @NotBlank(message= "Address is required!!")
    private String billingAddress;

    @NotEmpty
    private String billingPhone;

    @NotBlank
    private String billingName;


}
