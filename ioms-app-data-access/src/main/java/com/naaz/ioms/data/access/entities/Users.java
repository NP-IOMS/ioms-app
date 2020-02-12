package com.naaz.ioms.data.access.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;

import com.naaz.ioms.data.access.utils.Constants;

/**
 * @nazimHussain
 *
 */

@Entity
@Table(name = Constants.USERS_TABLE_NAME)
@Getter
@Setter
@Slf4j
public class Users extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_pass")
    private String userPass;

    @Column(name = "user_account_name")
    private String userAccountName;

    /**
     * ManyToOne relation with UserRole. Represents the UserRole to which this User belong to.
     *
     * Uni-directional relation.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_role_id")
    private UserRole userRole;

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
        if(that instanceof Users)
        {
            final Users temp = (Users) that;
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
