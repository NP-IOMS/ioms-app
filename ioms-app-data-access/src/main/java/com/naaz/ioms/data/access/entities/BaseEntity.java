package com.naaz.ioms.data.access.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author nazimHussain
 *
 */
@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "created_on")
    protected Timestamp createdOn;

    @Column(name = "modified_on")
    protected Timestamp modifiedOn;
}
