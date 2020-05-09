package com.naaz.ioms.data.access.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naaz.ioms.data.access.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @nazimHussain
 *
 */

@Entity
@Table(name = Constants.ORDER_DETAILS_TABLE_NAME)
@Getter
@Setter
@Slf4j
public class OrdersDetails implements Serializable {

    private static final long serialVersionUID = 66L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ManyToOne relation with OrderHeader. Represents the OrderHeader to which this OrderDetails belong to.
     *
     * Bi-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrdersHeader ordersHeader;

    /**
     * ManyToOne relation with Product. Represents the Product to which this OrderDetails belong to.
     *
     * Bi-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_price")
    private Float productPrice;

    @Column(name = "gst_rate")
    private Long gstRate;

    @Column(name = "product_quantity")
    private Long productQuantity;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Long.toString(ordersHeader.getId()).hashCode() + Long.toString(product.getId()).hashCode();
        return result;
    }

    @Override
    public boolean equals(Object that) {
        if(this == that)
        {
            return true;
        }
        if(that == null)
        {
            return false;
        }
        if(that instanceof OrdersDetails)
        {
            final OrdersDetails temp = (OrdersDetails) that;
            return (this.ordersHeader.getId() == temp.ordersHeader.getId() && this.product.getId() == temp.product.getId());
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("unable to compute toString due to exception: ", e);
            return String.format("{ orderId: %s, productId: %s, obj: %s}", ordersHeader.getId(), product.getId(), super.toString());
        }
    }
}
