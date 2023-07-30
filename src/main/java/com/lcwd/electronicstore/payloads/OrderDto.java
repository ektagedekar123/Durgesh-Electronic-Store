package com.lcwd.electronicstore.payloads;

import com.lcwd.electronicstore.entities.OrderItems;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDto extends BaseEntityDto{

    private String OrderId;

    @NotBlank
    private String orderStatus="PENDING";

    @NotBlank
    private String paymentStatus="NOTPAID";


    private int orderAmount;

    @NotBlank(message="billing address must not be blank")
    private String billingAddress;

    @NotBlank
    private String billingPhone;

    @NotBlank
    private String billingName;

    private Date orderedDate; //=new Date();

    private Date deliveredDate;

    private List<OrderItemsDto> orderItems=new ArrayList<>();
}
