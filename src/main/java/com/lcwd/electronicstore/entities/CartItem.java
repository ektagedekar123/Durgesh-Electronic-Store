package com.lcwd.electronicstore.entities;

import lombok.*;

import javax.persistence.*;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
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

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name= "cart_id")
    private Cart cart;


}
// By default Fetch Type:-
// @OneToOne: Eager
// @OneToMany: Lazy
// @ManyToOne: Eager
// @ManyToMany: Lazy