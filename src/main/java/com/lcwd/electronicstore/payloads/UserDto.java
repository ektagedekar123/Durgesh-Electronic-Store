package com.lcwd.electronicstore.payloads;

import com.lcwd.electronicstore.validate.ImageNameValid;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto extends BaseEntityDto{

    private String userid;

    @Size(min = 3, max = 25, message = "Invalid name!!")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid email id")
//  @Pattern(regexp = "[a-zA-Z0-9][a-zA-Z0-9-.]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+", message = "Invalid email id")
//   @Email(message = "Invalid email id")
    private String email;

    @NotBlank(message = "Password is required!!")
    @Pattern(regexp = "[a-zA-z0-9][a-zA-Z0-9-[^a-zA-z0-9]]+", message = "Invalid Password")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid gender!!")
    private String gender;

    @NotBlank(message = "write something about yourself!!")
    private String about;

    @ImageNameValid
    private String imagename;


}
