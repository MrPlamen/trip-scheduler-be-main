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
        return visitItemService.createVisitItem(tripId, request);
    }

    @PutMapping("/{visitItemId}")
    public VisitItemResponse editVisitItem(
            @PathVariable UUID tripId,
            @PathVariable UUID visitItemId,
            @RequestBody VisitItemRequest request
    ) {
        return visitItemService.editVisitItem(tripId, visitItemId, request);
    }

    @DeleteMapping("/{visitItemId}")
    public void deleteVisitItem(
            @PathVariable UUID tripId,
            @PathVariable UUID visitItemId
    ) {
        visitItemService.deleteVisitItem(tripId, visitItemId);
    }

    @GetMapping("/visit-items/{visitItemId}")
    public VisitItemResponse getVisitItemById(@PathVariable UUID visitItemId) {

        System.out.println("I got hit - getVisitItemById");
        return visitItemService.getVisitItemById(visitItemId);
    }
}

