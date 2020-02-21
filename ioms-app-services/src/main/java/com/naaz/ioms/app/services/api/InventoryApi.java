package com.naaz.ioms.app.services.api;

import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.app.services.util.Constants;
import com.naaz.ioms.data.access.dao.InventoryDao;
import com.naaz.ioms.data.access.entities.Inventory;
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
@Api("Inventory")
@Slf4j
public class InventoryApi {

    private InventoryDao inventoryDao;

    public InventoryApi(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    /**
     * api to fetch all inventory available.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all inventory in db")
    @Path(Constants.API_V1_VERSION + "/inventory")
    public List<Inventory> getAllInventory() throws IomsDbAccessException {
        try{
            return inventoryDao.findAll();
        } catch (Exception ex) {
            log.error("failed to fetch all inventory due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular inventory by its id.
     *
     * @param id
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular inventory with its id")
    @Path(Constants.API_V1_VERSION + "/inventory/{id}")
    public Inventory getInventoryById(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            return inventoryDao.find(id);
        } catch (Exception e) {
            log.error("failed to fetch a inventory for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular inventory by its id.
     *
     * @param id
     * @return
     * @throws IomsDbAccessException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular inventory with its id")
    @Path(Constants.API_V1_VERSION + "/inventory/delete/{id}")
    public Response deleteInventory(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            inventoryDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to delete inventory for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing inventory.
     *
     * @param inventory
     * @return
     * @throws IomsDbAccessException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing inventory")
    @Path(Constants.API_V1_VERSION + "/inventory/update")
    public Response updateInventory(@Valid @NotNull final Inventory inventory)
            throws IomsDbAccessException {
        try {
            inventoryDao.update(inventory);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        } catch (Exception e) {
            log.error("failed to update inventory for id: {} due to exception.", inventory.getId(), e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new inventory.
     *
     * @param inventory
     * @return
     * @throws IomsDbAccessException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new inventory")
    @Path(Constants.API_V1_VERSION + "/inventory/create")
    public Inventory createInventory(@Valid @NotNull final Inventory inventory)
            throws IomsDbAccessException {
        try {
            final Inventory newInventory =  inventoryDao.create(inventory);
            return newInventory;
        } catch (Exception e) {
            log.error("failed to create inventory for name:" + inventory.getInventoryName()
                    +" due to exception." , e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
