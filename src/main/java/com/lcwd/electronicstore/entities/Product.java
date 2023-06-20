package com.lcwd.electronicstore.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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


}
