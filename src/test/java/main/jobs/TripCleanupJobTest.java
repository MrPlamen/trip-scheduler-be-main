package main.jobs;

import main.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

class TripCleanupJobTest {

    @Test
    void testCleanupInvokesRepositoryCorrectly() {
        TripRepository repo = Mockito.mock(TripRepository.class);
        TripCleanupJob job = new TripCleanupJob(repo);

        job.cleanupOldTrips();

        LocalDate expectedCutoff = LocalDate.now().minusYears(1);
        Mockito.verify(repo).deleteTripsOlderThan(expectedCutoff);
    }
}

