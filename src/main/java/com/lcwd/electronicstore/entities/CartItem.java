package com.lcwd.electronicstore.entities;

import javax.persistence.*;

@Entity
@Table(name= "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer cartItemId;

    private Integer quantity;

    private Integer totalPrice;

    @OneToOne
    @JoinColumn(name= "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name= "cart_id")
    private Cart cart;


}
