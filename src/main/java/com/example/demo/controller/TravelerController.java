package com.example.demo.controller;

import com.example.demo.models.Pasaporte;
import com.example.demo.models.Pasajero;
import com.example.demo.service.TravelerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/travelers")
public class TravelerController {

    @Autowired
    private TravelerService travelerService;

    // 1. Obtener todos los viajeros ordenados por ID de pasaporte
    @GetMapping
    public ResponseEntity<List<Pasajero>> getAllTravelers() {
        List<Pasajero> travelers = travelerService.getAllTravelersOrdered();
        return ResponseEntity.ok(travelers);
    }

    // 2. Obtener pasajero por el ID del pasaporte
    @GetMapping("/passport/{passportId}")
    public ResponseEntity<List<Pasajero>> getTravelerByPassportId(@PathVariable Long passportId) {
        List<Pasajero> travelers = travelerService.getTravelerByPassportId(passportId);
        if (travelers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(travelers);
    }

    // 3. Obtener viajeros cuyo número de pasaporte sea similar al patrón dado
    @GetMapping("/search")
    public ResponseEntity<List<Pasajero>> getTravelersByPassportNumberLike(@RequestParam("pattern") String pattern) {
        List<Pasajero> travelers = travelerService.getTravelersByPassportNumberLike(pattern);
        return ResponseEntity.ok(travelers);
    }

    // 4. Obtener viajeros con reservas no vacías
    @GetMapping("/reservations/not-empty")
    public ResponseEntity<List<Pasajero>> getTravelersWithReservationsNotEmpty() {
        List<Pasajero> travelers = travelerService.getTravelersWithReservationsNotEmpty();
        return ResponseEntity.ok(travelers);
    }

    // 5. Obtener viajeros con reservas vacías
    @GetMapping("/reservations/empty")
    public ResponseEntity<List<Pasajero>> getTravelersWithReservationsEmpty() {
        List<Pasajero> travelers = travelerService.getTravelersWithReservationsEmpty();
        return ResponseEntity.ok(travelers);
    }

    // 6. Contar la cantidad de viajeros con reservas no vacías
    @GetMapping("/reservations/count")
    public ResponseEntity<Long> countTravelersWithReservations() {
        long count = travelerService.countTravelersWithReservations();
        return ResponseEntity.ok(count);
    }

    // 7. Obtener viajeros con cantidad de reservas mayor a un valor
    @GetMapping("/reservations/greater-than")
    public ResponseEntity<List<Pasajero>> getTravelersByReservationCountGreaterThan(@RequestParam("count") int count) {
        List<Pasajero> travelers = travelerService.getTravelersByReservationCountGreaterThan(count);
        return ResponseEntity.ok(travelers);
    }

    // 8. Obtener viajeros que tengan exactamente la cantidad de reservas indicada
    @GetMapping("/reservations/equal")
    public ResponseEntity<List<Pasajero>> getTravelersByReservationEquals(@RequestParam("count") int count) {
        List<Pasajero> travelers = travelerService.getTravelersByReservationEquals(count);
        return ResponseEntity.ok(travelers);
    }

    // 9. Obtener los N primeros viajeros con reservas
    @GetMapping("/top")
    public ResponseEntity<List<Pasajero>> getTopNTravelersWithReservations(@RequestParam("n") int n) {
        List<Pasajero> travelers = travelerService.getTopNTravelersWithReservations(n);
        return ResponseEntity.ok(travelers);
    }

    // 10. Obtener pasajero por pasaporte (usando el objeto Pasaporte)
    @GetMapping("/by-passport")
    public ResponseEntity<Pasajero> getTravelerByPassport(@RequestParam("number") String number) {
        Pasaporte pasaporte = new Pasaporte();
        pasaporte.setNumber(number);
        Optional<Pasajero> traveler = travelerService.getTravelerByPassport(pasaporte);
        return traveler.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}