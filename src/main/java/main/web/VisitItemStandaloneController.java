package main.web;

import main.service.VisitItemService;
import main.web.dto.VisitItemRequest;
import main.web.dto.VisitItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/visit-items/{visitItemId}")
public class VisitItemStandaloneController {

    private final VisitItemService visitItemService;

    public VisitItemStandaloneController(VisitItemService visitItemService) {
        this.visitItemService = visitItemService;
    }

    @GetMapping
    public VisitItemResponse getVisitItemById(@PathVariable UUID visitItemId) {

        System.out.println("I got hit - getVisitItemById");
        return visitItemService.getVisitItemById(visitItemId);
    }

    @PutMapping
    public VisitItemResponse editVisitItem(
            @PathVariable UUID visitItemId,
            @RequestBody VisitItemRequest request
    ) {
        UUID tripId = request.getTripId();

        return visitItemService.editVisitItem(tripId, visitItemId, request);
    }

    @DeleteMapping
    public void deleteVisitItem(
            @PathVariable UUID visitItemId
    ) {
        visitItemService.deleteVisitItem(visitItemId);
    }
}

