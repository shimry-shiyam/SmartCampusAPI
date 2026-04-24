/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author shimryshiyam
 */
package com.smartcampus.database;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatabaseClass {
    
    // ConcurrentHashMap ensures thread safety when multiple clients hit the API at once
    private static Map<String, Room> rooms = new ConcurrentHashMap<>();
    private static Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    
    // Maps a Sensor ID to a thread-safe list of its readings
    private static Map<String, List<SensorReading>> sensorReadings = new ConcurrentHashMap<>();

    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static Map<String, Sensor> getSensors() {
        return sensors;
    }

    public static Map<String, List<SensorReading>> getSensorReadings() {
        return sensorReadings;
    }
}