package main.web;

import main.model.VisitItem;
import main.service.VisitItemService;
import main.web.dto.VisitItemRequest;
import main.web.dto.VisitItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/visit-items")
public class VisitItemStandaloneController {

    private final VisitItemService visitItemService;

    public VisitItemStandaloneController(VisitItemService visitItemService) {
        this.visitItemService = visitItemService;
    }

    @GetMapping("/{visitItemId}")
    public VisitItemResponse getVisitItemById(@PathVariable UUID visitItemId) {

        System.out.println("I got hit - getVisitItemById");
        return visitItemService.getVisitItemById(visitItemId);
    }

    @GetMapping
    public List<VisitItemResponse> getAllVisitItems() {

        return visitItemService.getAllVisitItems();
    }

    @PutMapping("/{visitItemId}")
    public VisitItemResponse editVisitItem(
            @PathVariable UUID visitItemId,
            @RequestBody VisitItemRequest request
    ) {
        UUID tripId = request.getTripId();

        return visitItemService.editVisitItem(tripId, visitItemId, request);
    }

    @DeleteMapping("/{visitItemId}")
    public void deleteVisitItem(
            @PathVariable UUID visitItemId
    ) {
        visitItemService.deleteVisitItem(visitItemId);
    }
}

