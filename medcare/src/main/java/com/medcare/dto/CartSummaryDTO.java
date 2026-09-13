package com.medcare.dto;
import java.math.BigDecimal;
import java.util.List;

public class CartSummaryDTO {
    private List<CartItemDTO> items; private BigDecimal subtotal;
    private BigDecimal gst; private BigDecimal total; private int itemCount;

    public CartSummaryDTO() {}
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private List<CartItemDTO> items; private BigDecimal subtotal;
        private BigDecimal gst; private BigDecimal total; private int itemCount;
        public Builder items(List<CartItemDTO> v) { this.items = v; return this; }
        public Builder subtotal(BigDecimal v) { this.subtotal = v; return this; }
        public Builder gst(BigDecimal v) { this.gst = v; return this; }
        public Builder total(BigDecimal v) { this.total = v; return this; }
        public Builder itemCount(int v) { this.itemCount = v; return this; }
        public CartSummaryDTO build() {
            CartSummaryDTO d = new CartSummaryDTO(); d.items = items; d.subtotal = subtotal;
            d.gst = gst; d.total = total; d.itemCount = itemCount; return d;
        }
    }
    public List<CartItemDTO> getItems() { return items; } public void setItems(List<CartItemDTO> v) { this.items = v; }
    public BigDecimal getSubtotal() { return subtotal; } public void setSubtotal(BigDecimal v) { this.subtotal = v; }
    public BigDecimal getGst() { return gst; } public void setGst(BigDecimal v) { this.gst = v; }
    public BigDecimal getTotal() { return total; } public void setTotal(BigDecimal v) { this.total = v; }
    public int getItemCount() { return itemCount; } public void setItemCount(int v) { this.itemCount = v; }
}
