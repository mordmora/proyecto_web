package com.example.demo.service;

import com.example.demo.models.Pasaporte;
import com.example.demo.models.Pasajero;
import com.example.demo.repository.TravelerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelerServiceTest {

    @Mock
    private TravelerRepository travelerRepository;

    @InjectMocks
    private TravelerService travelerService;

    @Test
    void getTravelerByPassport() {
        Pasaporte pasaporte = new Pasaporte();
        pasaporte.setNumber("ABC123");
        Pasajero pasajero = new Pasajero();
        pasajero.setPasaporte(pasaporte);

        when(travelerRepository.findByPasaporte(pasaporte)).thenReturn(Optional.of(pasajero));

        Optional<Pasajero> result = travelerService.getTravelerByPassport(pasaporte);
        assertTrue(result.isPresent());
        assertEquals("ABC123", result.get().getPasaporte().getNumber());
        verify(travelerRepository).findByPasaporte(pasaporte);
    }

    @Test
    void getTravelerByPassportId() {
        Long passportId = 1L;
        Pasajero pasajero = new Pasajero();
        Pasaporte pasaporte = new Pasaporte();
        pasaporte.setId(passportId);
        pasajero.setPasaporte(pasaporte);

        when(travelerRepository.findByPasaporteId(passportId))
                .thenReturn(Collections.singletonList(pasajero));

        List<Pasajero> result = travelerService.getTravelerByPassportId(passportId);
        assertFalse(result.isEmpty());
        assertEquals(passportId, result.get(0).getPasaporte().getId());
        verify(travelerRepository).findByPasaporteId(passportId);
    }

    @Test
    void getAllTravelersOrdered() {
        Pasajero pasajero1 = new Pasajero();
        Pasajero pasajero2 = new Pasajero();
        when(travelerRepository.findAllByOrderByPasaporteIdAsc())
                .thenReturn(Arrays.asList(pasajero1, pasajero2));

        List<Pasajero> result = travelerService.getAllTravelersOrdered();
        assertEquals(2, result.size());
        verify(travelerRepository).findAllByOrderByPasaporteIdAsc();
    }

    @Test
    void getTravelersWithReservationsNotEmpty() {
        Pasajero pasajero = new Pasajero();
        when(travelerRepository.findByReservasNotEmpty())
                .thenReturn(Collections.singletonList(pasajero));

        List<Pasajero> result = travelerService.getTravelersWithReservationsNotEmpty();
        assertFalse(result.isEmpty());
        verify(travelerRepository).findByReservasNotEmpty();
    }

    @Test
    void getTravelersWithReservationsEmpty() {
        Pasajero pasajero = new Pasajero();
        when(travelerRepository.findByReservasEmpty())
                .thenReturn(Collections.singletonList(pasajero));

        List<Pasajero> result = travelerService.getTravelersWithReservationsEmpty();
        assertFalse(result.isEmpty());
        verify(travelerRepository).findByReservasEmpty();
    }

    @Test
    void countTravelersWithReservations() {
        when(travelerRepository.countByReservasNotEmpty()).thenReturn(5L);

        long count = travelerService.countTravelersWithReservations();
        assertEquals(5L, count);
        verify(travelerRepository).countByReservasNotEmpty();
    }

    @Test
    void getTravelersByReservationCountGreaterThan() {
        Pasajero pasajero = new Pasajero();
        when(travelerRepository.findByReservasCountGreaterThan(2))
                .thenReturn(Collections.singletonList(pasajero));

        List<Pasajero> result = travelerService.getTravelersByReservationCountGreaterThan(2);
        assertFalse(result.isEmpty());
        verify(travelerRepository).findByReservasCountGreaterThan(2);
    }

    @Test
    void getTravelersByReservationEquals() {
        Pasajero pasajero = new Pasajero();
        when(travelerRepository.findByReservasEquals(1))
                .thenReturn(Collections.singletonList(pasajero));

        List<Pasajero> result = travelerService.getTravelersByReservationEquals(1);
        assertEquals(1, result.size());
        verify(travelerRepository).findByReservasEquals(1);
    }

    @Test
    void getTopNTravelersWithReservations() {
        Pasajero pasajero1 = new Pasajero();
        Pasajero pasajero2 = new Pasajero();
        when(travelerRepository.findTopNWithReservas(2))
                .thenReturn(Arrays.asList(pasajero1, pasajero2));

        List<Pasajero> result = travelerService.getTopNTravelersWithReservations(2);
        assertEquals(2, result.size());
        verify(travelerRepository).findTopNWithReservas(2);
    }

    @Test
    void getTravelersByPassportNumberLike() {
        Pasajero pasajero = new Pasajero();
        Pasaporte pasaporte = new Pasaporte();
        pasaporte.setNumber("XYZ789");
        pasajero.setPasaporte(pasaporte);

        when(travelerRepository.findByPasaporteNumeroLike("%XYZ%"))
                .thenReturn(Collections.singletonList(pasajero));

        List<Pasajero> result = travelerService.getTravelersByPassportNumberLike("%XYZ%");
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getPasaporte().getNumber().contains("XYZ"));
        verify(travelerRepository).findByPasaporteNumeroLike("%XYZ%");
    }
}