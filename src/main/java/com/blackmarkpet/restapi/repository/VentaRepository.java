package com.blackmarkpet.restapi.repository;

import com.blackmarkpet.restapi.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Ventas por hora para un día específico
    @Query("SELECT EXTRACT(HOUR FROM v.fecha) as hora, SUM(v.cantidad) as totalVentas " +
            "FROM Venta v WHERE CAST(v.fecha AS date) = :fecha GROUP BY EXTRACT(HOUR FROM v.fecha) ORDER BY hora")
    List<Object[]> findVentasPorHora(@Param("fecha") LocalDate fecha);

    // Ganancias mensuales para un año específico
    @Query("SELECT EXTRACT(MONTH FROM v.fecha) as mes, SUM(v.total) as totalGanancias " +
            "FROM Venta v WHERE EXTRACT(YEAR FROM v.fecha) = :anio GROUP BY EXTRACT(MONTH FROM v.fecha) ORDER BY mes")
    List<Object[]> findGananciasMensuales(@Param("anio") int anio);

    // Top productos más vendidos (por cantidad total) - limit 5
    @Query("SELECT v.productoId, SUM(v.cantidad) as totalCantidad " +
            "FROM Venta v GROUP BY v.productoId ORDER BY totalCantidad DESC")
    List<Object[]> findTopProductos();

}
