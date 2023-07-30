package com.lcwd.electronicstore.payloads;

import com.lcwd.electronicstore.entities.OrderItems;
import lombok.*;

import javax.validation.constraints.NotBlank;
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

    private String orderStatus="PENDING";

    private String paymentStatus="NOTPAID";

    private int orderAmount;


    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderedDate; //=new Date();

    private Date deliveredDate;

    private List<OrderItemsDto> orderItems=new ArrayList<>();
}
