package models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "vuelos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
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

    @OneToMany
    @JoinColumn(name = "reserva_id", nullable = false)
    private Set<Reserva> reservas;

    @ManyToMany(mappedBy = "vuelos")
    private Set<Aereolinea> aereolineas;
}
