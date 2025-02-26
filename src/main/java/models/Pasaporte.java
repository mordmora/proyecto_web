package models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pasaportes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Pasaporte {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String number;

    @OneToOne(mappedBy = "pasaporte")
    private Pasajero pasajero;

}
