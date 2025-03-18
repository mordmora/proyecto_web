package com.example.demo.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pasaportes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pasaporte {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String number;

    @OneToOne(mappedBy = "pasaporte")
    private Pasajero pasajero;

}
