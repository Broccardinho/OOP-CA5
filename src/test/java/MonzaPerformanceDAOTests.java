import org.example.dao.MonzaPerformanceDAO;
import org.example.dto.MonzaPerformanceDTO;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonzaPerformanceDAOTests {

    @Test
    public void testAddAndGetRacer() {
        MonzaPerformanceDAO dao = new MonzaPerformanceDAO();
        MonzaPerformanceDTO testRacer = new MonzaPerformanceDTO(
                0, "Test Racer", "Test Team", 80.0, 10, 10, 0, "TestNation");

        MonzaPerformanceDTO addedRacer = dao.addRacer(testRacer);
        assertNotNull(addedRacer);
        assertTrue(addedRacer.getId() > 0);

        MonzaPerformanceDTO retrieved = dao.getRacerById(addedRacer.getId());
        assertEquals("Test Racer", retrieved.getName());
    }

    @Test
    public void testUpdateRacer() {
        MonzaPerformanceDAO dao = new MonzaPerformanceDAO();
        MonzaPerformanceDTO testRacer = new MonzaPerformanceDTO(
                0, "Original", "Team", 89.0, 5, 5, 5, "Country");

        // Add first
        MonzaPerformanceDTO added = dao.addRacer(testRacer);

        // Update
        added.setName("Updated");
        boolean updateResult = dao.updateRacer(added.getId(), added);
        assertTrue(updateResult);

        // Verify
        MonzaPerformanceDTO updated = dao.getRacerById(added.getId());
        assertEquals("Updated", updated.getName());

        // Cleanup
        dao.deleteRacer(added.getId());
    }

    @Test
    public void testDeleteRacer() {
        MonzaPerformanceDAO dao = new MonzaPerformanceDAO();
        MonzaPerformanceDTO testRacer = new MonzaPerformanceDTO(
                0, "ToDelete", "Team", 91.0, 20, 20, 0, "Country");

        // Add first
        MonzaPerformanceDTO added = dao.addRacer(testRacer);

        // Delete and verify
        assertTrue(dao.deleteRacer(added.getId()));
        assertNull(dao.getRacerById(added.getId()));
    }

    @Test
    public void testGetAllContainsAddedRacer() {
        MonzaPerformanceDAO dao = new MonzaPerformanceDAO();
        MonzaPerformanceDTO testRacer = new MonzaPerformanceDTO(
                0, "TempRacer", "TempTeam", 92.0, 15, 15, 0, "Temp");

        // Add test racer
        MonzaPerformanceDTO added = dao.addRacer(testRacer);

        // Check if exists in all racers
        List<MonzaPerformanceDTO> allRacers = dao.getAllRacers();
        boolean found = false;
        for (MonzaPerformanceDTO r : allRacers) {
            if (r.getId() == added.getId()) {
                found = true;
                break;
            }
        }
        assertTrue(found);

        // Cleanup
        dao.deleteRacer(added.getId());
    }
}