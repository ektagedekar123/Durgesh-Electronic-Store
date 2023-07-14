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
@Table(name= "cart")
public class Cart extends BaseEntity{

    @Id
    private String cartId;

    private Date createdAt;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart", cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> items=new ArrayList<>();
}
