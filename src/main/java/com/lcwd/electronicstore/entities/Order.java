package com.lcwd.electronicstore.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name= "orders")
public class Order extends BaseEntity {

    @Id
    private String OrderId;

    // PENDING, DISPATCHED, DELIVERED
    //enum
    private String orderStatus;

    // Not-PAID, PAID
    // enum
    // boolean- false=>NOT-PAID  || true=>PAID
    private String paymentStatus;

    private int orderAmount;

    @Column(length=1000)
    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderedDate;

    private Date deliveredDate;

    @ManyToOne(fetch= FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy= "order", fetch= FetchType.EAGER, cascade= CascadeType.ALL)
    private List<OrderItems> orderItems=new ArrayList<>();

}
