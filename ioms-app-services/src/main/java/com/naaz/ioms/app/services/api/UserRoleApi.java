package com.naaz.ioms.app.services.api;

import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.data.access.dao.UserRoleDao;
import com.naaz.ioms.data.access.entities.UserRole;
import com.naaz.ioms.app.services.util.Constants;
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
@Api("UserRole")
@Slf4j
public class UserRoleApi {

    private UserRoleDao userRoleDao;

    public UserRoleApi(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    /**
     * api to fetch all userRole available.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all user-role in db")
    @Path(Constants.API_V1_VERSION + "/user-role")
    public List<UserRole> getAllUserRole() throws IomsDbAccessException {
        try{
            return userRoleDao.findAll();
        } catch (Exception ex) {
            log.error("failed to fetch all user-role due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular user-role by its id.
     *
     * @param id
     * @return
     * @throws IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular user-role with its id")
    @Path(Constants.API_V1_VERSION + "/user-role/{id}")
    public UserRole getUserRoleById(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            return userRoleDao.find(id);
        } catch (Exception e) {
            log.error("failed to fetch a user-role for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular user-role by its id.
     *
     * @param id
     * @return
     * @throws IomsDbAccessException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular user-role with its id")
    @Path(Constants.API_V1_VERSION + "/user-role/delete/{id}")
    public Response deleteUserRole(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            userRoleDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to delete user-role for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing user-role.
     *
     * @param id
     * @param userRole
     * @return
     * @throws IomsDbAccessException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing user-role")
    @Path(Constants.API_V1_VERSION + "/user-role/update/{id}")
    public Response updateUserRole(@PathParam("id") final long id, @Valid @NotNull final UserRole userRole)
            throws IomsDbAccessException {
        try {
            userRoleDao.update(userRole);
            return Response.status(HttpStatus.SC_NO_CONTENT).build();
        } catch (Exception e) {
            log.error("failed to update user-role for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new user-role.
     *
     * @param userRole
     * @return
     * @throws IomsDbAccessException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new user-role")
    @Path(Constants.API_V1_VERSION + "/user-role/create")
    public UserRole createUserRole(@Valid @NotNull final UserRole userRole)
            throws IomsDbAccessException {
        try {
            final UserRole newUserRole =  userRoleDao.create(userRole);
            return newUserRole;
        } catch (Exception e) {
            log.error("failed to create user-role for name:" + userRole.getUserRoleName()
                    +" due to exception." , e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
