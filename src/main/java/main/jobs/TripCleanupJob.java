package main.jobs;

import main.repository.TripRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class TripCleanupJob {

    private final TripRepository tripRepository;

    public TripCleanupJob(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupOldTrips() {
        LocalDate cutoff = LocalDate.now().minusYears(1);
        tripRepository.deleteTripsOlderThan(cutoff);
    }
}

