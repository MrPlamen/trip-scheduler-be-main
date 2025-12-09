package main.web;

import main.service.VisitItemService;
import main.web.dto.VisitItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class VisitItemStandaloneController {

    private final VisitItemService visitItemService;

    public VisitItemStandaloneController(VisitItemService visitItemService) {
        this.visitItemService = visitItemService;
    }

    @GetMapping("/visit-items/{visitItemId}")
    public VisitItemResponse getVisitItemById(@PathVariable UUID visitItemId) {

        System.out.println("I got hit - getVisitItemById");
        return visitItemService.getVisitItemById(visitItemId);
    }
}

