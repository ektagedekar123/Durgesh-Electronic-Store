package com.lcwd.electronicstore.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntityDto {

    @NotBlank
    public String createdBy;

    private LocalDateTime createdOn;

    @NotBlank
    private String lastModifiedBy;

    @NotEmpty
    private String isActive;
}
