package models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aereolineas")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
@Builder
public class Aereolinea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "vuelos_de_aereolineas",
            joinColumns = @JoinColumn(name = "aereolinea_id", nullable = false, referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vuelo_id", nullable = true, referencedColumnName = "id")
    )
    private List<Vuelo> vuelos = new ArrayList<Vuelo>();

    public void agregarVuelo(Vuelo vuelo) {
        this.vuelos.add(vuelo);
    }

}
