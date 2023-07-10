package com.lcwd.electronicstore.payloads;


import com.lcwd.electronicstore.entities.BaseEntity;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto extends BaseEntityDto {

    private String CategoryId;

   @NotBlank
   @Size(min = 3, message= "title must be of minimum 3 characters!!")
    private String title;

   @NotBlank(message= "Description is required!!")
    private String description;

    @NotBlank(message = "Cover image is required!!")
    private String coverImage;
}
