package com.lcwd.electronicstore.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToCartRequest extends BaseEntityDto{

   private String productId;

   private Integer quantity;
}
