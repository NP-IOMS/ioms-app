package com.naaz.ioms.data.access.list_entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersHeaderDetails {
    private Long orderNumber;
    private Float orderPrice;
    private Float orderGstAmount;
    private String orderStatus;
    private String orderCustomerName;
    private String orderRaisedBy;
    protected String orderRaisedOn;
    protected String orderDispatchedOn;
    private String productName;
    private Float productPrice;
    private Long productGstRate;
    private Long productQuantity;
}
