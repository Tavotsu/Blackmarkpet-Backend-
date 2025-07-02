package com.blackmarkpet.restapi.controller;

import com.blackmarkpet.restapi.model.Producto;
import com.blackmarkpet.restapi.model.Venta;
import com.blackmarkpet.restapi.repository.ProductoRepository;
import com.blackmarkpet.restapi.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://black-markpet-frontend.vercel.app/")
public class EstadisticasController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Endpoint POST para crear una nueva venta
    @PostMapping("/ventas")
    public ResponseEntity<Venta> crearVenta(@RequestBody Venta venta) {
        Venta ventaGuardada = ventaRepository.save(venta);
        return ResponseEntity.status(201).body(ventaGuardada);
    }

    // 1. Ventas por hora del día actual
    @GetMapping("/ventas-dia")
    public ResponseEntity<Map<String, Object>> getVentasPorHora() {
        LocalDate hoy = LocalDate.now();
        List<Object[]> resultados = ventaRepository.findVentasPorHora(hoy);

        List<Integer> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        Map<Integer, Integer> ventasPorHora = resultados.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).intValue(),
                        r -> ((Number) r[1]).intValue()
                ));

        for (int h = 0; h < 24; h++) {
            labels.add(h);
            data.add(ventasPorHora.getOrDefault(h, 0));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 2. Ganancias mensuales del año actual
    @GetMapping("/ganancias-mensuales")
    public ResponseEntity<Map<String, Object>> getGananciasMensuales() {
        int anio = Year.now().getValue();
        List<Object[]> resultados = ventaRepository.findGananciasMensuales(anio);

        Map<Integer, Double> gananciasPorMes = resultados.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).intValue(),
                        r -> ((Number) r[1]).doubleValue()
                ));

        List<Integer> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (int m = 1; m <= 12; m++) {
            labels.add(m);
            data.add(gananciasPorMes.getOrDefault(m, 0.0));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 3. Top 5 productos más vendidos
    @GetMapping("/top-productos")
    public ResponseEntity<Map<String, Object>> getTopProductos() {
        List<Object[]> resultados = ventaRepository.findTopProductos();

        List<Object[]> top5 = resultados.stream().limit(5).collect(Collectors.toList());

        List<Long> productoIds = top5.stream()
                .map(r -> ((Number) r[0]).longValue())
                .collect(Collectors.toList());

        List<Producto> productos = productoRepository.findAllById(productoIds);

        Map<Long, String> idNombreMap = productos.stream()
                .collect(Collectors.toMap(Producto::getId, Producto::getNombre));

        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        for (Object[] r : top5) {
            Long productoId = ((Number) r[0]).longValue();
            Integer cantidad = ((Number) r[1]).intValue();
            labels.add(idNombreMap.getOrDefault(productoId, "Desconocido"));
            data.add(cantidad);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 4. Productos con stock crítico (stock <= 5)
    @GetMapping("/productos-stock-critico")
    public ResponseEntity<Map<String, Object>> getProductosStockCritico() {
        List<Producto> productosCriticos = productoRepository.findAll().stream()
                .filter(p -> p.getStock() <= 5)
                .collect(Collectors.toList());

        List<String> labels = productosCriticos.stream()
                .map(Producto::getNombre)
                .collect(Collectors.toList());

        List<Integer> data = productosCriticos.stream()
                .map(Producto::getStock)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }
}
