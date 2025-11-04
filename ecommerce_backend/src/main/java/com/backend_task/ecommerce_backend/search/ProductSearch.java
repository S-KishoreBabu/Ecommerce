package com.backend_task.ecommerce_backend.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "products")
public class ProductSearch {

    @Id
    @JsonIgnore // ✅ hides this field from JSON responses
    private Long id; // internal ES ID only

    private String name;
    private double mrp;
    private double discountedPrice;
    private int quantity;

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getMrp() { return mrp; }
    public void setMrp(double mrp) { this.mrp = mrp; }

    public double getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(double discountedPrice) { this.discountedPrice = discountedPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // ✅ Optional (useful for debugging)
    @Override
    public String toString() {
        return "ProductSearch{" +
                "name='" + name + '\'' +
                ", mrp=" + mrp +
                ", discountedPrice=" + discountedPrice +
                ", quantity=" + quantity +
                '}';
    }
}
