package com.yaren.trafficsim.desktop;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DesktopApp extends Application {

    private static final String API_BASE = "http://localhost:8080";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private List<JsonNode> intersections = new ArrayList<>();
    private final Map<String, JsonNode> vehicles = new ConcurrentHashMap<>();

    private double minLat, maxLat, minLon, maxLon;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(900, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        root.setStyle("-fx-background-color: #1a1033;");

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Traffic Simulation - Desktop Dashboard");
        stage.setScene(scene);
        stage.show();

        refreshIntersections();

        AnimationTimer timer = new AnimationTimer() {
            private long lastPoll = 0;

            @Override
            public void handle(long now) {
                if (now - lastPoll > 800_000_000L) {
                    refreshVehicles();
                    lastPoll = now;
                }
                draw(gc);
            }
        };
        timer.start();
    }

    private void refreshIntersections() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/api/intersections"))
                    .GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            JsonNode array = mapper.readTree(res.body());

            intersections = new ArrayList<>();
            array.forEach(intersections::add);

            minLat = Double.MAX_VALUE;
            maxLat = -Double.MAX_VALUE;
            minLon = Double.MAX_VALUE;
            maxLon = -Double.MAX_VALUE;

            for (JsonNode i : intersections) {
                double lat = i.get("latitude").asDouble();
                double lon = i.get("longitude").asDouble();
                minLat = Math.min(minLat, lat);
                maxLat = Math.max(maxLat, lat);
                minLon = Math.min(minLon, lon);
                maxLon = Math.max(maxLon, lon);
            }
        } catch (Exception e) {
            System.out.println("Could not load intersections: " + e.getMessage());
        }
    }

    private void refreshVehicles() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/api/simulation/active"))
                    .GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            JsonNode obj = mapper.readTree(res.body());

            vehicles.clear();
            obj.fields().forEachRemaining(entry -> vehicles.put(entry.getKey(), entry.getValue()));
        } catch (Exception e) {
            System.out.println("Could not load vehicles: " + e.getMessage());
        }
    }

    private double[] project(double lat, double lon, double width, double height) {
        double padding = 80;
        double x = padding + (lon - minLon) / (maxLon - minLon + 0.0001) * (width - 2 * padding);
        double y = height - padding - (lat - minLat) / (maxLat - minLat + 0.0001) * (height - 2 * padding);
        return new double[]{x, y};
    }

    private void draw(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        gc.setFill(Color.web("#1a1033"));
        gc.fillRect(0, 0, width, height);

        if (intersections.isEmpty()) {
            gc.setFill(Color.WHITE);
            gc.fillText("Loading intersections...", width / 2 - 60, height / 2);
            return;
        }

        gc.setFont(Font.font(14));

        for (JsonNode i : intersections) {
            double[] p = project(i.get("latitude").asDouble(), i.get("longitude").asDouble(), width, height);
            gc.setFill(Color.web("#a05eff"));
            gc.fillOval(p[0] - 8, p[1] - 8, 16, 16);
            gc.setFill(Color.WHITE);
            gc.fillText(i.get("name").asText(), p[0] + 12, p[1] + 4);
        }

        for (JsonNode v : vehicles.values()) {
            JsonNode pos = v.get("currentPosition");
            if (pos == null) continue;
            double[] p = project(pos.get("latitude").asDouble(), pos.get("longitude").asDouble(), width, height);
            gc.setFill(Color.web("#ff6ec7"));
            gc.fillOval(p[0] - 6, p[1] - 6, 12, 12);
            gc.setStroke(Color.web("#ff6ec7"));
            gc.strokeText(v.get("id").asText(), p[0] - 10, p[1] - 12);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}