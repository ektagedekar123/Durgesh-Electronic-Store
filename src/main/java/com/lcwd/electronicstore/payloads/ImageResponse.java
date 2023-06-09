package com.lcwd.electronicstore.payloads;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {

    private String imagename;

    private String message;

    private boolean success;

    private HttpStatus status;
}
