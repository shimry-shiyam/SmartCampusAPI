Name:Shimry Shiyam
IIT ID: 20241277 / UOW ID: W2152978
Client Server Architecture Coursework


Smart Campus API

Overview of API Design
The Smart Campus API is a high-performance RESTful web service built using JAX-RS (Jakarta RESTful Web Services). It is designed to manage university infrastructure, specifically handling the registration and tracking of physical Rooms, IoT Sensors (such as Temperature and CO2 monitors), and their historical telemetry data. The API utilizes a custom-built, thread-safe in-memory data store (ConcurrentHashMap) to manage state without relying on external SQL databases, and features advanced exception mapping for robust error handling.

Building the Project and Launching the Server
This project was built using Apache NetBeans and is configured to run on an Apache Tomcat 10 server (Jakarta EE environment).
1.	Clone the Repository: Download or clone this repository to your local machine.
2.	Open the Project: Open Apache NetBeans, navigate to File > Open Project, and select the SmartCampusAPI folder.
3.	Configure the Server: Ensure you have Apache Tomcat 10 configured in NetBeans. Right-click the project, go to Properties > Run, and ensure your Tomcat 10 server is selected.
4.	Build the Application: Right-click the project name in the hierarchy and select Clean and Build to resolve all Maven dependencies.
5.	Launch the API: Click the Run (Play) button. NetBeans will deploy the .war file to Tomcat. The API will be accessible at http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1.


Sample API Interactions (cURL Commands)
Below are five sample curl commands demonstrating successful interactions with the core endpoints of the API.

1. View API Discovery Metadata (GET)
Bash
curl -X GET http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1

2. Create a New Room (POST)
Bash
curl -X POST http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 50
}'

3. Retrieve All Rooms (GET)
Bash
curl -X GET http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1/rooms

4. Register a Sensor to a Room (POST)
Bash
curl -X POST http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{
  "id": "TEMP-001",
  "type": "Temperature",
  "roomId": "LIB-301",
  "status": "ACTIVE"
}'

5. Log a Historical Sensor Reading (POST)
Bash
curl -X POST http://localhost:8080/SmartCampusAPI-1.0-SNAPSHOT/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{
  "value": 22.5
}'
