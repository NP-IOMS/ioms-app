package com.naaz.ioms.app.services.api;

import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.app.services.util.Constants;
import com.naaz.ioms.data.access.dao.ReportsDao;
import com.naaz.ioms.data.access.entities.OrdersHeader;
import com.naaz.ioms.data.access.list_entities.OrdersHeaderDetails;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/")
@Api("Reports")
@Slf4j
public class ReportsApi {

    private final ReportsDao reportsDao;

    public ReportsApi(ReportsDao reportsDao) {
        this.reportsDao = reportsDao;
    }

    /**
     * api to fetch all orders in db for a duration.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all orders in db for a duration")
    @Path(Constants.API_V1_VERSION + "/reports/order")
    public List<OrdersHeaderDetails> getAllOrders(
            @Valid @NotNull @HeaderParam("orderStatus") final String status,
            @Valid @NotNull @HeaderParam("fromDate") final String startDate,
            @Valid @NotNull @HeaderParam("endDate") final String endDate
    ) throws IomsDbAccessException {
        try{
//            Fri Feb 21 2020 01:19:57 GMT+0530 E MMM dd yyyy HH:mm:ss
            final SimpleDateFormat inputDateFormatter = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss");
            final SimpleDateFormat outputDateFormatter = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");

            final List<OrdersHeaderDetails> orderHeaderDetailsList = new ArrayList<OrdersHeaderDetails>();
            final List<OrdersHeader> allOrders = reportsDao.getOrderForADurationAndStatus(inputDateFormatter.parse(startDate),
                    inputDateFormatter.parse(endDate), status);
            allOrders.forEach(orderHeader -> {
                orderHeader.getOrdersDetails().forEach(ordersDetails -> {
                    final OrdersHeaderDetails ordersHeaderDetails = new OrdersHeaderDetails();
                    ordersHeaderDetails.setOrderNumber(orderHeader.getId());
                    ordersHeaderDetails.setOrderPrice(orderHeader.getOrderPrice());
                    ordersHeaderDetails.setOrderGstAmount(orderHeader.getOrderGstAmount());
                    ordersHeaderDetails.setOrderStatus(orderHeader.getStatus());
                    ordersHeaderDetails.setOrderCustomerName(orderHeader.getCustomer().getUserAccountName());
                    ordersHeaderDetails.setOrderRaisedBy(orderHeader.getCreatedBy().getUserAccountName());
                    ordersHeaderDetails.setOrderRaisedOn(outputDateFormatter.format(new Date(orderHeader.getCreatedOn().getTime())));
                    ordersHeaderDetails.setOrderDispatchedOn(outputDateFormatter.format(new Date(orderHeader.getDispatchedOn().getTime())));
                    ordersHeaderDetails.setProductName(ordersDetails.getProduct().getProductName());
                    ordersHeaderDetails.setProductPrice(ordersDetails.getProduct().getPrice());
                    ordersHeaderDetails.setProductGstRate(ordersDetails.getProduct().getGstRate());
                    ordersHeaderDetails.setProductQuantity(ordersDetails.getProductQuantity());
                    orderHeaderDetailsList.add(ordersHeaderDetails);
                });
            });
            return orderHeaderDetailsList;
        } catch (Exception ex) {
            log.error("failed to fetch all orders report for a duration due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
