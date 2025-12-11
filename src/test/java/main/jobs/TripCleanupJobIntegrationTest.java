package main.jobs;

import main.model.Trip;
import main.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TripCleanupJobIntegrationTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripCleanupJob cleanupJob;

    @Test
    void testCleanupOldTrips() {
        Trip oldTrip = Trip.builder()
                .title("Old Trip")
                .category("Test")
                .startDate(LocalDate.now().minusYears(2))
                .endDate(LocalDate.now().minusYears(2))
                .imageUrl("img")
                .summary("sum")
                .ownerEmail("x@x.bg")
                .build();

        Trip recentTrip = Trip.builder()
                .title("Recent Trip")
                .category("Test")
                .startDate(LocalDate.now().minusMonths(2))
                .endDate(LocalDate.now().minusMonths(2))
                .imageUrl("img")
                .summary("sum")
                .ownerEmail("x@x.bg")
                .build();

        tripRepository.save(oldTrip);
        tripRepository.save(recentTrip);

        cleanupJob.cleanupOldTrips();

        assertThat(tripRepository.findAll()).hasSize(1);
        assertThat(tripRepository.findAll().get(0).getTitle()).isEqualTo("Recent Trip");
    }
}
