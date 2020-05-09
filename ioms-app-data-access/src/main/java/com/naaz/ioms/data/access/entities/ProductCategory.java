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
@Table(name = Constants.PRODUCT_CATEGORY_TABLE_NAME)
@Getter
@Setter
@Slf4j
public class ProductCategory extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_category_name")
    private String productCategoryName;

    @Column(name = "product_category_desc")
    private String productCategoryDesc;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "hsn_code")
    private String hsnCode;

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
        if(that instanceof ProductCategory)
        {
            final ProductCategory temp = (ProductCategory) that;
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
