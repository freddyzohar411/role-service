package com.avensys.rts.roleservice.util;

import com.avensys.rts.roleservice.payloadresponse.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static ResponseEntity<Object>generateSuccessResponse(Object object,HttpStatus httpStatus,String message){
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setData(object);
        httpResponse.setCode(httpStatus.value());
        httpResponse.setMessage(message);
        return new ResponseEntity<>(httpResponse,httpStatus);

    }
    public static ResponseEntity<Object>generateErrorResponse(HttpStatus httpStatus,String message){
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setCode(httpStatus.value());
        httpResponse.setError(true);
        httpResponse.setMessage(message);
        return new ResponseEntity<>(httpResponse,httpStatus);

    }

}
