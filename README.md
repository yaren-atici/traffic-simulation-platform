🚦 Traffic Simulation Platform

A real-time traffic simulation and route optimization platform built with Spring Boot, PostgreSQL, WebSocket, and JavaFX. Simulates multiple vehicles navigating a city graph concurrently, with routes that dynamically adapt to live traffic congestion.

Features
Dijkstra's shortest path algorithm for route calculation between intersections
Dynamic congestion-aware routing — road weights increase as more vehicles occupy them, so new vehicles naturally avoid crowded roads
Multi-threaded vehicle simulation — each vehicle moves independently on its own thread using a managed ExecutorService
Real-time updates via WebSocket (STOMP) — vehicle positions are broadcast live to all connected clients
REST API for managing intersections, roads, and simulations
PostgreSQL running in a Docker container for persistent storage
Two live clients:
A web dashboard (HTML/CSS/JS) with a live-updating vehicle table
A JavaFX desktop dashboard that renders intersections and moving vehicles on a canvas
Tech Stack
Java 21, Spring Boot 4
Spring Data JPA + PostgreSQL (Dockerized)
Spring WebSocket (STOMP over SockJS)
JavaFX (desktop client)
Maven
Architecture

Client (Web / JavaFX) | v REST API + WebSocket (Spring Boot) | v RouteService (Dijkstra + congestion weighting) | v SimulationService (multi-threaded vehicle engine) | v PostgreSQL (Docker)

API Endpoints
Method	Endpoint	Description
GET	/api/intersections	List all intersections
POST	/api/intersections	Create a new intersection
GET	/api/roads	List all roads
POST	/api/roads	Create a new road between two intersections
GET	/api/route?fromId=&toId=	Calculate the shortest path between two intersections
POST	/api/simulation/spawn?fromId=&toId=	Spawn a vehicle that will travel the calculated route
GET	/api/simulation/active	List all currently active vehicles
Running locally
Start PostgreSQL in Docker: docker run --name traffic-postgres -e POSTGRES_PASSWORD=traffic123 -e POSTGRES_DB=trafficsim -p 5432:5432 -d postgres:16
Run the Spring Boot application (TrafficsimApplication).
Open the web dashboard at http://localhost:8080
(Optional) Run the JavaFX desktop client: mvnw javafx:run This opens a separate window rendering the same live simulation on a canvas.
What this project demonstrates
Graph algorithms (Dijkstra) applied to a real-world-style routing problem
Safe concurrent programming (ConcurrentHashMap, synchronized, thread pools)
Real-time communication with WebSocket
REST API design with Spring Boot
Working with a containerized relational database (Docker + PostgreSQL)
Building multiple client interfaces (web + desktop) against the same backend
