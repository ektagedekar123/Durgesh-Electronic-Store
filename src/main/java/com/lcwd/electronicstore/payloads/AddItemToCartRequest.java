package com.lcwd.electronicstore.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToCartRequest extends BaseEntityDto{

   private String productId;

   @NotBlank
   private Integer quantity;
}
