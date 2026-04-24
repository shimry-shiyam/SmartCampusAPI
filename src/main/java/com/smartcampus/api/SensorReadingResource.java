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
import com.smartcampus.exceptions.SensorUnavailableException;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;
    private Map<String, Sensor> sensors = DatabaseClass.getSensors();
    private Map<String, List<SensorReading>> sensorReadings = DatabaseClass.getSensorReadings();

    // The constructor receives the ID from the parent SensorResource
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // 1. GET /: Fetch history for this specific sensor
    @GET
    public Response getReadings() {
        // Ensure the parent sensor actually exists
        if (!sensors.containsKey(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Sensor not found\"}")
                           .build();
        }
        
        List<SensorReading> readings = sensorReadings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(readings).build();
    }

    // 2. POST /: Append new reading and update parent sensor
    // 2. POST /: Append new reading and update parent sensor
    @POST
    public Response addReading(SensorReading reading) {
        Sensor parentSensor = sensors.get(sensorId);
        
        if (parentSensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Sensor not found\"}")
                           .build();
        }

        // Phase 5: Check if sensor is physically disconnected
        if ("MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is in maintenance mode and cannot accept readings.");
        }
        // -------------------------

        // Auto-generate UUID and Timestamp if the client didn't provide them
        if (reading.getId() == null || reading.getId().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        // --- CORE REQUIREMENT: Update the parent sensor's state ---
        parentSensor.setCurrentValue(reading.getValue());

        // Safely add the reading to our thread-safe in-memory database
        sensorReadings.putIfAbsent(sensorId, new CopyOnWriteArrayList<>());
        sensorReadings.get(sensorId).add(reading);

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}