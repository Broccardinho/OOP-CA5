package org.example.dto;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;

public class JsonConverter {

    // Convert a single MonzaPerformanceDTO to JSON string
    public static String monzaPerformanceToJsonString(MonzaPerformanceDTO dto) {
        JSONObject obj = new JSONObject();
        obj.put("id", dto.getId());
        obj.put("name", dto.getName());
        obj.put("team", dto.getTeam());
        obj.put("fastestLapTime", dto.getFastestLapTime());
        obj.put("finalPosition", dto.getFinalPosition());
        obj.put("gridPosition", dto.getGridPosition());
        obj.put("pointsEarned", dto.getPointsEarned());
        obj.put("nationality", dto.getNationality());
        obj.put("imageLink", dto.getImageLink() != null ? dto.getImageLink() : "");
        return obj.toString();
    }

    // Convert a list of MonzaPerformanceDTO to JSON array string
    public static String monzaPerformanceListToJsonString(List<MonzaPerformanceDTO> dtoList) {
        JSONArray jsonArray = new JSONArray();
        for (MonzaPerformanceDTO dto : dtoList) {
            JSONObject obj = new JSONObject();
            obj.put("id", dto.getId());
            obj.put("name", dto.getName());
            obj.put("team", dto.getTeam());
            obj.put("fastestLapTime", dto.getFastestLapTime());
            obj.put("finalPosition", dto.getFinalPosition());
            obj.put("gridPosition", dto.getGridPosition());
            obj.put("pointsEarned", dto.getPointsEarned());
            obj.put("nationality", dto.getNationality());
            obj.put("imageLink", dto.getImageLink() != null ? dto.getImageLink() : "");
            jsonArray.put(obj);
        }
        return jsonArray.toString();
    }

    // Convert JSON string back to MonzaPerformanceDTO
    public static MonzaPerformanceDTO jsonStringToMonzaPerformance(String jsonString) {
        JSONObject obj = new JSONObject(jsonString);
        return new MonzaPerformanceDTO(
                obj.getInt("id"),
                obj.getString("name"),
                obj.getString("team"),
                obj.getDouble("fastestLapTime"),
                obj.getInt("finalPosition"),
                obj.getInt("gridPosition"),
                obj.getInt("pointsEarned"),
                obj.getString("nationality"),
                obj.optString("imageLink", "")
        );
    }

    // Convert JSON array string back to List<MonzaPerformanceDTO>
    public static List<MonzaPerformanceDTO> jsonStringToMonzaPerformanceList(String jsonArrayString) {
        JSONArray jsonArray = new JSONArray(jsonArrayString);
        List<MonzaPerformanceDTO> dtoList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            MonzaPerformanceDTO dto = new MonzaPerformanceDTO(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("team"),
                    obj.getDouble("fastestLapTime"),
                    obj.getInt("finalPosition"),
                    obj.getInt("gridPosition"),
                    obj.getInt("pointsEarned"),
                    obj.getString("nationality"),
                    obj.optString("imageLink", "")
            );
            dtoList.add(dto);
        }

        return dtoList;
    }
}