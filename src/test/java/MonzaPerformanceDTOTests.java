import org.example.dto.MonzaPerformanceDTO;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonzaPerformanceDTOTests {
    @Test
    public void testConstructorAndGetters() {
        MonzaPerformanceDTO racer = new MonzaPerformanceDTO(
                1, "Lewis Hamilton", "Mercedes", 87.456, 1, 2, 25, "British");

        if(racer.getId() != 1){
            throw new AssertionError("ID getter failed");
        }
        if(!"Lewis Hamilton".equals(racer.getName())){
            throw new AssertionError("Name getter failed");
        }
        if(racer.getFastestLapTime() !=87.456){
            throw new AssertionError("Fastest LapTime getter failed");
        }
    }
@Test
public void testSetters() {
        MonzaPerformanceDTO racer = new MonzaPerformanceDTO(
                0, null, null, 0, 0, 0, 0,null);

        racer.setId(2);
        racer.setName("Max Verstappen");
        racer.setPointsEarned(18);

        if(racer.getId() != 2 || !"Max Verstappen".equals(racer.getName()) || racer.getPointsEarned() != 18){
            throw new AssertionError("Setter test failed");
        }
    }
@Test
public void testToString() {
        MonzaPerformanceDTO racer = new MonzaPerformanceDTO(
                3, "Charles Leclerc", "Ferrari", 87.789, 3, 3, 15, "Monegasque");

        String result = racer.toString();

        if(!result.contains("Charles Leclerc") || !result.contains("Ferrari") || !result.contains("Monegasque")){
            throw new AssertionError("String toString() failed");
        }
    }

//    @Test
//    public void failOnPurpose() {
//        MonzaPerformanceDTO racer = new MonzaPerformanceDTO(1, "x", "x", 0, 0, 0, 0, "x");
//        assertEquals(2, racer.getId()); // This should FAIL
//    }
}