package com.lcwd.electronicstore.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name= "cart")
public class Cart {

    @Id
    private String cartId;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart", cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> list=new ArrayList<>();
}
