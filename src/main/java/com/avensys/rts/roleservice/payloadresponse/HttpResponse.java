package com.avensys.rts.roleservice.payloadresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {
    private int code;
    private boolean error;
    private String message;
    private Object data;
    private Map<?,?>audit;
    private LocalDateTime timestamp = LocalDateTime.now();
}
