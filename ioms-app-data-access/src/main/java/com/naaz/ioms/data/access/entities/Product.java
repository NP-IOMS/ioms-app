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
@Table(name = Constants.PRODUCT_TABLE_NAME)
@Getter
@Setter
@Slf4j
public class Product extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_desc")
    private String productDesc;

    @Column(name = "price")
    private Float price;

    @Column(name = "gst_rate")
    private Long gstRate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "available_stock")
    private Integer availableStock;

    @Column(name = "hsn_code")
    private String hsnCode;

    /**
     * ManyToOne relation with ProductCategory. Represents the ProductCategory to which this Product belong to.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    /**
     * OneToOne relation with Files. Represents the file for Product.
     *
     * Uni-directional relation.
     */
    @OneToOne
    @JoinColumn(name = "file_id")
    private Files file;

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
        if(that instanceof Product)
        {
            final Product temp = (Product) that;
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
