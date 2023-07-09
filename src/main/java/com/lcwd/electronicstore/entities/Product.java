package com.lcwd.electronicstore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product extends BaseEntity{

    @Id
    private String productid;

    private String title;

    @Column(length= 10000)
    private String description;

    private int price;

    private int discountedPrice;

    private int quantity;

    private boolean live;

    private boolean stock;

    private String productImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "category_id")
    private Category category;


}
