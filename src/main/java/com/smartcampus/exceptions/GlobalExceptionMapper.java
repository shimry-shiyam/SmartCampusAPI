/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exceptions;

/**
 *
 * @author shimryshiyam
 */

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        // You could log the actual exception to the console here for debugging
        exception.printStackTrace(); 
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR) // 500
                       .entity("{\"error\":\"An unexpected internal server error occurred.\"}")
                       .type("application/json")
                       .build();
    }
}