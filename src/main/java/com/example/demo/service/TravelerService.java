package com.example.demo.service;

import com.example.demo.models.Pasaporte;
import com.example.demo.models.Pasajero;
import com.example.demo.repository.TravelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelerService {

    @Autowired
    private TravelerRepository travelerRepository;

    public Optional<Pasajero> getTravelerByPassport(Pasaporte pasaporte) {
        return travelerRepository.findByPasaporte(pasaporte);
    }

    public List<Pasajero> getTravelerByPassportId(Long passportId) {
        return travelerRepository.findByPasaporteId(passportId);
    }

    public List<Pasajero> getAllTravelersOrdered() {
        return travelerRepository.findAllByOrderByPasaporteIdAsc();
    }


    public List<Pasajero> getTravelersWithReservationsNotEmpty() {
        return travelerRepository.findByReservasNotEmpty();
    }


    public List<Pasajero> getTravelersWithReservationsEmpty() {
        return travelerRepository.findByReservasEmpty();
    }

    public long countTravelersWithReservations() {
        return travelerRepository.countByReservasNotEmpty();
    }

    public List<Pasajero> getTravelersByReservationCountGreaterThan(int count) {
        return travelerRepository.findByReservasCountGreaterThan(count);
    }

    public List<Pasajero> getTravelersByReservationEquals(int count) {
        return travelerRepository.findByReservasEquals(count);
    }

    public List<Pasajero> getTopNTravelersWithReservations(int n) {
        return travelerRepository.findTopNWithReservas(n);
    }

    public List<Pasajero> getTravelersByPassportNumberLike(String pattern) {
        return travelerRepository.findByPasaporteNumeroLike(pattern);
    }
}