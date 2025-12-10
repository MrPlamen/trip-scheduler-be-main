package main.web;

import main.service.VisitItemService;
import main.web.dto.VisitItemRequest;
import main.web.dto.VisitItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips/{tripId}/visit-items")
public class VisitItemController {

    private final VisitItemService visitItemService;

    public VisitItemController(VisitItemService visitItemService) {
        this.visitItemService = visitItemService;
    }

    @GetMapping
    public List<VisitItemResponse> getVisitItems(@PathVariable UUID tripId) {
        System.out.println("getVisitItems");

        return visitItemService.getVisitItemsByTripId(tripId);
    }

    @GetMapping("/{visitItemId}")
    public VisitItemResponse getVisitItem(
            @PathVariable UUID tripId,
            @PathVariable UUID visitItemId
    ) {

        System.out.println("getVisit 1");
        return visitItemService.getVisitItem(tripId, visitItemId);
    }

    @PostMapping
    public VisitItemResponse createVisitItem(
            @PathVariable UUID tripId,
            @RequestBody VisitItemRequest request
    ) {
        System.out.println("ownerId " + request.get_ownerId());
        return visitItemService.createVisitItem(tripId, request);
    }

    @GetMapping("/visit-items/{visitItemId}")
    public VisitItemResponse getVisitItemById(@PathVariable UUID visitItemId) {

        return visitItemService.getVisitItemById(visitItemId);
    }
}

