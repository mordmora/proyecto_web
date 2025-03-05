package repository;

import models.Aereolinea;
import models.Vuelo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirlineRepository extends JpaRepository<Aereolinea, Long> {
    List<Aereolinea> findByName(String name);

    List<Aereolinea> findByNameContainingIgnoreCase(String nameFragment);

    List<Aereolinea> findAllByOrderByNameAsc();

    @Query("SELECT a FROM Aereolinea a WHERE SIZE(a.vuelos) = :size")
    List<Aereolinea> findByVuelosSize(int size);

    List<Aereolinea> findByVuelosNotEmpty();

    List<Aereolinea> findByNameStartsWith(String prefix);

    List<Aereolinea> findByVuelosEmpty();

    @Query("SELECT a FROM Aereolinea a ORDER BY a.name ASC")
    List<Aereolinea> findTopNByOrderByNameAsc(Pageable pageable);

    @Query("SELECT a FROM Aereolinea a WHERE a.name = :name AND SIZE(a.vuelos) > :size")
    List<Aereolinea> findByNameAndVuelosGreaterThan(String name, int size);

    @Query("SELECT DISTINCT a FROM Aereolinea a JOIN a.vuelos v WHERE v.destiny LIKE %:destino%")
    List<Aereolinea> findByVuelosDestiny(String destiny);
} 
