package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "vuelos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private UUID uuid;

    @Column
    private String origin;

    @Column
    private String destiny;

    @OneToMany(mappedBy = "vuelo")
    private Set<Reserva> reservas;

    @ManyToMany(mappedBy = "vuelos")
    private Set<Aereolinea> aereolineas;
}