package com.lcwd.electronicstore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name= "cart")
public class Cart extends BaseEntity{

    @Id
    private String cartId;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart", cascade= CascadeType.ALL, orphanRemoval = true)  // We can remove fetchType= Eager or we can use Set to avoid duplication of data
    private List<CartItem> items=new ArrayList<>();
}
// We have to apply orphanRemoval=true in @OneToMany annotaion to clear all cart items from cart