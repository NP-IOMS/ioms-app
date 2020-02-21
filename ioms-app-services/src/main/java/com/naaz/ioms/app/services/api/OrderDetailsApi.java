package com.naaz.ioms.app.services.api;

import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.app.services.util.Constants;
import com.naaz.ioms.data.access.dao.OrderDetailsDao;
import com.naaz.ioms.data.access.entities.OrdersDetails;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Api("OrderDetails")
@Slf4j
public class OrderDetailsApi {

    private final OrderDetailsDao orderDetailsDao;

    public OrderDetailsApi(OrderDetailsDao orderDetailsDao) {
        this.orderDetailsDao = orderDetailsDao;
    }

    /**
     * api to update an existing order details.
     *
     * @param ordersDetails
     * @return
     * @throws IomsDbAccessException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an orderDetails")
    @Path(Constants.API_V1_VERSION + "/order/details/update")
    public Response updateOrderDetails( @Valid @NotNull final OrdersDetails ordersDetails)
            throws IomsDbAccessException {
        try {
            orderDetailsDao.update(ordersDetails);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        } catch (Exception e) {
            log.error("failed to update order details for order header id: {} due to exception.", ordersDetails.getOrdersHeader().getId(), e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new orderDetails.
     *
     * @param ordersDetails
     * @return
     * @throws IomsDbAccessException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new orderDetails")
    @Path(Constants.API_V1_VERSION + "/order/details/create")
    public OrdersDetails createOrderDetails(@Valid @NotNull final OrdersDetails ordersDetails)
            throws IomsDbAccessException {
        try {
            final OrdersDetails newOrdersDetails =  orderDetailsDao.create(ordersDetails);
            return newOrdersDetails;
        } catch (Exception e) {
            log.error("failed to create orderDetails for orderHeader id :" + ordersDetails.getOrdersHeader().getId()
                    +" due to exception." , e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
