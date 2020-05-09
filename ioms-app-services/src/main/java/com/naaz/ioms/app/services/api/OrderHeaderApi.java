package com.naaz.ioms.app.services.api;

import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.app.services.util.Constants;
import com.naaz.ioms.data.access.dao.OrderHeaderDao;
import com.naaz.ioms.data.access.entities.OrdersHeader;
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
import java.util.List;

@Path("/")
@Api("OrderHeader")
@Slf4j
public class OrderHeaderApi {

    private final OrderHeaderDao orderHeaderDao;

    public OrderHeaderApi(OrderHeaderDao orderHeaderDao) {
        this.orderHeaderDao = orderHeaderDao;
    }

    /**
     * api to fetch all orders available.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all orders in db")
    @Path(Constants.API_V1_VERSION + "/order/all")
    public List<OrdersHeader> getAllOrders() throws IomsDbAccessException {
        try{
            return orderHeaderDao.findAll();
        } catch (Exception ex) {
            log.error("failed to fetch all orders due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all orders available against a status.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all orders for a status in db")
    @Path(Constants.API_V1_VERSION + "/orders/status")
    public List<OrdersHeader> getAllOrdersForStatus(@Valid @NotNull @HeaderParam("orderStatus") final String status) throws IomsDbAccessException {
        try{
            return orderHeaderDao.fetchOrdersForAStatus(status);
        } catch (Exception ex) {
            log.error("failed to fetch all orders of a status due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all orders available for a user.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all orders for a user in db")
    @Path(Constants.API_V1_VERSION + "/orders/user")
    public List<OrdersHeader> getAllOrdersForUser(@Valid @NotNull @HeaderParam("createdBy") final long createdBy) throws IomsDbAccessException {
        try{
            return orderHeaderDao.fetchOrdersForSalesman(createdBy);
        } catch (Exception ex) {
            log.error("failed to fetch all orders for a user due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing order.
     *
     * @param ordersHeader
     * @return
     * @throws IomsDbAccessException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing orderHeader")
    @Path(Constants.API_V1_VERSION + "/order/header/update")
    public Response updateOrder(@Valid @NotNull final OrdersHeader ordersHeader)
            throws IomsDbAccessException {
        try {
            orderHeaderDao.update(ordersHeader);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to update orderHeader for id: {} due to exception.", ordersHeader.getId(), e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new orderHeader.
     *
     * @param ordersHeader
     * @return
     * @throws IomsDbAccessException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new OrderHeader")
    @Path(Constants.API_V1_VERSION + "/order/header/create")
    public OrdersHeader createOrder(@Valid @NotNull final OrdersHeader ordersHeader)
            throws IomsDbAccessException {
        try {
            final OrdersHeader newOrdersHeader =  orderHeaderDao.create(ordersHeader);
            return newOrdersHeader;
        } catch (Exception e) {
            log.error("failed to create orderHeader for user :" + ordersHeader.getCreatedBy().getId()
                    +" due to exception." , e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
