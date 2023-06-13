package com.lcwd.electronicstore.payloads;


import com.lcwd.electronicstore.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto extends BaseEntityDto {

    private String CategoryId;

   @NotBlank
   @Min(value = 3, message= "title must be of minimum 3 characters!!")
    private String title;

   @NotBlank(message= "Description is required!!")
    private String description;

    @NotBlank(message = "Cover image is required!!")
    private String coverImage;
}
