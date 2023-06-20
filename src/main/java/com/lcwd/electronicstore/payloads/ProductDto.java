package com.lcwd.electronicstore.payloads;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto extends BaseEntityDto{

    private String productid;

    @NotBlank(message = "title is required!!")
    @Size(min= 3, message = "title must be of minimum 3 characters")
    private String title;


    @NotBlank
    private String description;


    private int price;

    private int discountedPrice;


    private int quantity;


    private boolean live;


    private boolean stock;

    private String productImage;

}
