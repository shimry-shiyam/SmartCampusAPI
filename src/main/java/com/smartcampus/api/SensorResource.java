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
import com.smartcampus.models.Sensor;
import com.smartcampus.exceptions.LinkedResourceNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    // Connect to our database
    private Map<String, Sensor> sensors = DatabaseClass.getSensors();
    private Map<String, Room> rooms = DatabaseClass.getRooms();

    // 1. POST /: Register a new sensor with Room validation
    // 1. POST /: Register a new sensor with Room validation
    @POST
    public Response addSensor(Sensor sensor) {
        
        // Validation: Verify that the roomId specified actually exists in the system
        if (sensor.getRoomId() == null || !rooms.containsKey(sensor.getRoomId())) {
            // PHASE 5 UPDATE: Throwing our custom exception instead of a hardcoded 422
            throw new LinkedResourceNotFoundException("Linked Room ID does not exist.");
        }

        sensors.put(sensor.getId(), sensor);
        
        // --- THE MISSING LINK FIX ---
        // Fetch the parent room and physically add this sensor's ID to its list!
        Room parentRoom = rooms.get(sensor.getRoomId());
        parentRoom.getSensorIds().add(sensor.getId());
        // ----------------------------

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    // 2. GET /: Filtered Retrieval & Search
    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        List<Sensor> allSensors = new ArrayList<>(sensors.values());

        // If no type query parameter is provided, return the full list
        if (type == null || type.trim().isEmpty()) {
            return allSensors;
        }

        // If a type IS provided (e.g., ?type=CO2), filter the list to only include matches
        return allSensors.stream()
                         .filter(s -> type.equalsIgnoreCase(s.getType()))
                         .collect(Collectors.toList());
    }
    
    // 3. Sub-Resource Locator Pattern
    // Catch requests for /api/v1/sensors/{sensorId}/readings and delegate them
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        // We pass the sensorId to the constructor of the child class
        // so it knows exactly which sensor's history it is working with!
        return new SensorReadingResource(sensorId);
    }
}
