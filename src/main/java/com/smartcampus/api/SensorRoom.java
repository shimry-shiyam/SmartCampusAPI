/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api;

/**
 *
 * @author shimryshiyam
 */

import com.smartcampus.database.DatabaseClass;
import com.smartcampus.models.Room;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON) // Tells JAX-RS to automatically convert returned objects to JSON
@Consumes(MediaType.APPLICATION_JSON) // Tells JAX-RS to expect incoming JSON payloads
public class SensorRoom {

    // Connect to our thread-safe, in-memory database
    private Map<String, Room> rooms = DatabaseClass.getRooms();

    // 1. GET /: Provide a comprehensive list of all rooms
    @GET
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    // 2. POST /: Enable the creation of new rooms
    @POST
    public Response addRoom(Room room) {
        // Basic validation
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Room ID is required\"}")
                           .build();
        }
        
        rooms.put(room.getId(), room);
        
        // Return 201 Created status along with the created room
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // 3. GET /{roomId}: Fetch detailed metadata for a specific room
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404 if not found
        }
        return Response.ok(room).build(); // 200 OK
    }

    // 4. DELETE /{roomId}: Room Deletion & Safety Logic
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Business Logic Constraint: Cannot delete if it has active sensors
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            // For now, we return a standard 409 Conflict response.
            // In Phase 5, we will upgrade this to use the custom RoomNotEmptyException!
            return Response.status(Response.Status.CONFLICT)
                           .entity("{\"error\":\"Room is currently occupied by active hardware.\"}")
                           .build();
        }

        rooms.remove(roomId);
        // Return 204 No Content to indicate successful deletion with no body
        return Response.noContent().build(); 
    }
}