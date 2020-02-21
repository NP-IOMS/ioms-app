package com.naaz.ioms.data.access.entities;

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
@Table(name = Constants.INVENTORY_TABLE_NAME)
@Getter
@Setter
@Slf4j
public class Inventory extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory_name")
    private String inventoryName;

    @Column(name = "inventory_desc")
    private String inventoryDesc;

    @Column(name = "price")
    private Float price;

    @Column(name = "gst_rate")
    private Long gstRate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "available_stock")
    private Integer availableStock;

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
        if(that instanceof Inventory)
        {
            final Inventory temp = (Inventory) that;
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
