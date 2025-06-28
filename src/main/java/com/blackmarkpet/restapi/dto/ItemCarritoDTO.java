package com.blackmarkpet.restapi.dto;

import com.blackmarkpet.restapi.model.ItemCarrito;

public class ItemCarritoDTO {
    
    private Long itemId;
    private Long productoId;
    private String nombreProducto;
    private double precioProducto;
    private String imagenProducto;
    private int cantidad;

    // Constructor que convierte una entidad ItemCarrito a este DTO
    public ItemCarritoDTO(ItemCarrito item) {
        this.itemId = item.getId();
        this.productoId = item.getProducto().getId();
        this.nombreProducto = item.getProducto().getNombre();
        this.precioProducto = item.getProducto().getPrecio();
        this.imagenProducto = item.getProducto().getImageUrl();
        this.cantidad = item.getCantidad();
    }

    // Getters y Setters (importantes para la serialización a JSON)
    
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}