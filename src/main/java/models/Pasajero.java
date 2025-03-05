package models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "pasajeros")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Pasajero {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "pasaporte_id", referencedColumnName = "id")
    private Pasaporte pasaporte;

    @OneToMany(mappedBy = "pasajero", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reserva> reservas;
}
