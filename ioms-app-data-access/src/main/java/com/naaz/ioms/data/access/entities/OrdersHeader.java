package com.naaz.ioms.data.access.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naaz.ioms.data.access.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @nazimHussain
 *
 */

@Entity
@Table(name = Constants.ORDER_HEADER_TABLE_NAME)
@Getter
@Setter
@Slf4j
public class OrdersHeader extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 61L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_price")
    private Float orderPrice;

    @Column(name = "order_gst_amount")
    private Float orderGstAmount;

    @Column(name = "status")
    private String status;

    /**
     * ManyToOne relation with Users. Represents the Users to which this OrderHeader created for.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "customer_id")
    private Users customer;

    /**
     * ManyToOne relation with Users. Represents the Users to which this OrderHeader belong to.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "created_by")
    private Users createdBy;

    @Column(name = "approved_on")
    protected Timestamp approvedOn;

    @Column(name = "dispatched_on")
    protected Timestamp dispatchedOn;

    /**
     * OneToMany relation with OrderDetails. Represents a set of all non leaf json nodes present in this OrderHeader.
     *
     * Bi-directional relation.
     */
    @OneToMany(mappedBy = "ordersHeader", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<OrdersDetails> ordersDetails;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Long.toString(id).hashCode();
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
        if(that instanceof OrdersHeader)
        {
            final OrdersHeader temp = (OrdersHeader) that;
            return this.id == temp.id;
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
            return String.format("{ id: %s, obj: %s}", id, super.toString());
        }
    }
}
