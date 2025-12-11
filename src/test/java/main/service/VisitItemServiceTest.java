package main.service;

import main.model.Trip;
import main.model.VisitItem;
import main.repository.TripRepository;
import main.repository.VisitItemRepository;
import main.web.dto.VisitItemRequest;
import main.web.dto.VisitItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VisitItemServiceTest {

    private VisitItemRepository visitItemRepository;
    private TripRepository tripRepository;
    private VisitItemService sut; // System Under Test

    @BeforeEach
    void setup() {
        visitItemRepository = mock(VisitItemRepository.class);
        tripRepository = mock(TripRepository.class);
        sut = new VisitItemService(visitItemRepository, tripRepository);
    }

    // ---------------------------------------------------------
    // getVisitItemsByTripId()
    // ---------------------------------------------------------
    @Test
    void getVisitItemsByTripId_success() {
        UUID tripId = UUID.randomUUID();
        Trip trip = new Trip();
        VisitItem item1 = new VisitItem();
        VisitItem item2 = new VisitItem();
        trip.setVisitItems((List<VisitItem>) Set.of(item1, item2));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        var result = sut.getVisitItemsByTripId(tripId);

        assertEquals(2, result.size());
        verify(tripRepository).findById(tripId);
    }

    @Test
    void getVisitItemsByTripId_tripNotFound_throws() {
        UUID tripId = UUID.randomUUID();
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sut.getVisitItemsByTripId(tripId));
    }

    // ---------------------------------------------------------
    // getVisitItem()
    // ---------------------------------------------------------
    @Test
    void getVisitItem_success() {
        UUID tripId = UUID.randomUUID();
        UUID visitItemId = UUID.randomUUID();
        VisitItem item = new VisitItem();

        when(visitItemRepository.findByIdAndTripId(visitItemId, tripId)).thenReturn(Optional.of(item));

        VisitItemResponse response = sut.getVisitItem(tripId, visitItemId);

        assertNotNull(response);
    }

    @Test
    void getVisitItem_notFound_throws() {
        UUID tripId = UUID.randomUUID();
        UUID visitItemId = UUID.randomUUID();

        when(visitItemRepository.findByIdAndTripId(visitItemId, tripId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sut.getVisitItem(tripId, visitItemId));
    }

    // ---------------------------------------------------------
    // createVisitItem()
    // ---------------------------------------------------------
    @Test
    void createVisitItem_success() {
        UUID tripId = UUID.randomUUID();
        Trip trip = new Trip();

        VisitItemRequest request = new VisitItemRequest();
        request.setTitle("title");
        request.setDescription("desc");
        request.setImageUrl("url");
        request.set_ownerId(UUID.fromString("owner"));

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        VisitItemResponse response = sut.createVisitItem(tripId, request);

        ArgumentCaptor<VisitItem> captor = ArgumentCaptor.forClass(VisitItem.class);
        verify(visitItemRepository).save(captor.capture());

        VisitItem saved = captor.getValue();
        assertEquals("title", saved.getTitle());
        assertEquals("desc", saved.getDescription());
        assertEquals("url", saved.getImageUrl());
        assertEquals("owner", saved.get_ownerId());
        assertEquals(trip, saved.getTrip());

        assertNotNull(response);
    }

    @Test
    void createVisitItem_tripNotFound_throws() {
        UUID tripId = UUID.randomUUID();
        VisitItemRequest request = new VisitItemRequest();
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sut.createVisitItem(tripId, request));
    }

    // ---------------------------------------------------------
    // editVisitItem()
    // ---------------------------------------------------------
    @Test
    void editVisitItem_success() {
        UUID tripId = UUID.randomUUID();
        UUID visitItemId = UUID.randomUUID();
        VisitItem item = new VisitItem();

        VisitItemRequest request = new VisitItemRequest();
        request.setTitle("new title");
        request.setDescription("new desc");

        when(visitItemRepository.findByIdAndTripId(visitItemId, tripId)).thenReturn(Optional.of(item));

        VisitItemResponse response = sut.editVisitItem(tripId, visitItemId, request);

        verify(visitItemRepository).save(item);
        assertEquals("new title", item.getTitle());
        assertEquals("new desc", item.getDescription());
        assertNotNull(response);
    }

    @Test
    void editVisitItem_notFound_throws() {
        UUID tripId = UUID.randomUUID();
        UUID visitItemId = UUID.randomUUID();
        VisitItemRequest request = new VisitItemRequest();

        when(visitItemRepository.findByIdAndTripId(visitItemId, tripId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sut.editVisitItem(tripId, visitItemId, request));
    }

    // ---------------------------------------------------------
    // deleteVisitItem()
    // ---------------------------------------------------------
    @Test
    void deleteVisitItem_success() {
        UUID visitItemId = UUID.randomUUID();
        VisitItem item = new VisitItem();
        item.setMembers(new HashSet<>());

        when(visitItemRepository.findById(visitItemId)).thenReturn(Optional.of(item));

        sut.deleteVisitItem(visitItemId);

        verify(visitItemRepository).delete(item);
    }

    @Test
    void deleteVisitItem_notFound_throws() {
        UUID visitItemId = UUID.randomUUID();
        when(visitItemRepository.findById(visitItemId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sut.deleteVisitItem(visitItemId));
    }

    // ---------------------------------------------------------
    // getVisitItemById()
    // ---------------------------------------------------------
    @Test
    void getVisitItemById_success() {
        UUID visitItemId = UUID.randomUUID();
        VisitItem item = new VisitItem();

        when(visitItemRepository.findById(visitItemId)).thenReturn(Optional.of(item));

        VisitItemResponse response = sut.getVisitItemById(visitItemId);

        assertNotNull(response);
    }

    @Test
    void getVisitItemById_notFound_throws() {
        UUID visitItemId = UUID.randomUUID();
        when(visitItemRepository.findById(visitItemId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sut.getVisitItemById(visitItemId));
    }

    // ---------------------------------------------------------
    // getAllVisitItems()
    // ---------------------------------------------------------
    @Test
    void getAllVisitItems_success() {
        VisitItem item1 = new VisitItem();
        VisitItem item2 = new VisitItem();

        when(visitItemRepository.findAll()).thenReturn(List.of(item1, item2));

        var result = sut.getAllVisitItems();

        assertEquals(2, result.size());
        verify(visitItemRepository).findAll();
    }
}
