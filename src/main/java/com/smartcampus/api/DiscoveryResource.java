/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api;

/**
 *
 * @author shimryshiyam
 */

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetadata() {
        // Returns basic API metadata and hypermedia links as requested in Part 1
        String metadata = "{"
                + "\"version\": \"1.0\","
                + "\"admin_contact\": \"admin@smartcampus.edu\","
                + "\"endpoints\": {"
                + "\"rooms\": \"/api/v1/rooms\","
                + "\"sensors\": \"/api/v1/sensors\""
                + "}"
                + "}";
        return Response.ok(metadata).build();
    }
}