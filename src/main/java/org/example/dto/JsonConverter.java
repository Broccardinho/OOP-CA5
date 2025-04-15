package org.example.dto;
import org.example.dto.MonzaPerformanceDTO;
import org.example.Exceptions.DaoException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonConverter {

    // Convert a single racer to JSON Object
    public static JSONObject racerToJsonObject(MonzaPerformanceDTO racer) {
        JSONObject json = new JSONObject();
        json.put("id", racer.getId());
        json.put("name", racer.getName());
        json.put("team", racer.getTeam());
        json.put("fastestLapTime", racer.getFastestLapTime());
        json.put("finalPosition", racer.getFinalPosition());
        json.put("gridPosition", racer.getGridPosition());
        json.put("pointsEarned", racer.getPointsEarned());
        json.put("nationality", racer.getNationality());
        return json;
    }

    // Convert a list of racers to JSON Array
    public static JSONArray racersToJsonArray(List<MonzaPerformanceDTO> racers) {
        JSONArray jsonArray = new JSONArray();
        for (MonzaPerformanceDTO racer : racers) {
            jsonArray.put(racerToJsonObject(racer));
        }
        return jsonArray;
    }

    // Parse JSON response to single racer
    public static MonzaPerformanceDTO jsonToRacer(String jsonString) throws DaoException {
        try {
            JSONObject response = new JSONObject(jsonString);

            if (!response.has("status")) {
                throw new DaoException("Invalid response format: missing status field");
            }

            String status = response.getString("status");
            if (status.equals("error")) {
                throw new DaoException(response.getString("message"));
            }

            if (!response.has("data")) {
                throw new DaoException("Invalid response format: missing data field");
            }

            JSONObject data = response.getJSONObject("data");
            return new MonzaPerformanceDTO(
                    data.getInt("id"),
                    data.getString("name"),
                    data.getString("team"),
                    data.getDouble("fastestLapTime"),
                    data.getInt("finalPosition"),
                    data.getInt("gridPosition"),
                    data.getInt("pointsEarned"),
                    data.getString("nationality")
            );

        } catch (JSONException e) {
            throw new DaoException("JSON parsing error: " + e.getMessage());
        }
    }

    // Parse JSON response to list of racers
    public static List<MonzaPerformanceDTO> jsonToRacers(String jsonString) throws DaoException {
        try {
            JSONObject response = new JSONObject(jsonString);

            if (!response.has("status")) {
                throw new DaoException("Invalid response format: missing status field");
            }

            String status = response.getString("status");
            if (status.equals("error")) {
                throw new DaoException(response.getString("message"));
            }

            if (!response.has("data")) {
                throw new DaoException("Invalid response format: missing data field");
            }

            JSONArray dataArray = response.getJSONArray("data");
            List<MonzaPerformanceDTO> racers = new ArrayList<>();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject racerJson = dataArray.getJSONObject(i);
                racers.add(new MonzaPerformanceDTO(
                        racerJson.getInt("id"),
                        racerJson.getString("name"),
                        racerJson.getString("team"),
                        racerJson.getDouble("fastestLapTime"),
                        racerJson.getInt("finalPosition"),
                        racerJson.getInt("gridPosition"),
                        racerJson.getInt("pointsEarned"),
                        racerJson.getString("nationality")
                ));
            }

            return racers;

        } catch (JSONException e) {
            throw new DaoException("JSON parsing error: " + e.getMessage());
        }
    }

    // Helper method to create error response
    public static String createErrorResponse(String message) {
        JSONObject error = new JSONObject();
        error.put("status", "error");
        error.put("message", message);
        return error.toString();
    }

    // Helper method to create success response with single racer
    public static String createSuccessResponse(MonzaPerformanceDTO racer) {
        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("data", racerToJsonObject(racer));
        return response.toString();
    }

    // Helper method to create success response with racer list
    public static String createSuccessResponse(List<MonzaPerformanceDTO> racers) {
        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("data", racersToJsonArray(racers));
        return response.toString();
    }
}