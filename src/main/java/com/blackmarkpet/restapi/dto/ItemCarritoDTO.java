package com.blackmarkpet.restapi.dto;

public class ItemCarritoDTO {

    private Long id;
    private ProductoDTO producto;
    private int cantidad;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public ProductoDTO getProducto() {
        return producto;
    }
    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}