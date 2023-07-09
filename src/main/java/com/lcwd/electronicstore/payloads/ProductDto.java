package com.lcwd.electronicstore.payloads;



import com.lcwd.electronicstore.entities.Category;
import lombok.*;

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
@Builder
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

    private CategoryDto category;

}
