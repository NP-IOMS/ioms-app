package com.naaz.ioms.app.services.api;

import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.data.access.dao.UsersDao;
import com.naaz.ioms.data.access.entities.Users;
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
import java.io.InputStream;
import java.util.List;

@Path("/")
@Api("Users")
@Slf4j
public class UsersApi {

    private final UsersDao usersDao;

    public UsersApi(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    /**
     * api to fetch all users available.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all users in db")
    @Path(Constants.API_V1_VERSION + "/users")
    public List<Users> getAllUsers() throws IomsDbAccessException {
        try{
            return usersDao.findAll();
        } catch (Exception ex) {
            log.error("failed to fetch all users due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular user by its id.
     *
     * @param id
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular user with its id")
    @Path(Constants.API_V1_VERSION + "/users/{id}")
    public Users getUserById(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            return usersDao.find(id);
        } catch (Exception e) {
            log.error("failed to fetch a user for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular user by its id.
     *
     * @param id
     * @return
     * @throws IomsDbAccessException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular user with its id")
    @Path(Constants.API_V1_VERSION + "/users/delete/{id}")
    public Response deleteUser(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            usersDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to delete user for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing user.
     *
     * @param user
     * @return
     * @throws IomsDbAccessException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing user")
    @Path(Constants.API_V1_VERSION + "/users/update/")
    public Response updateUser(@Valid @NotNull final Users user)
            throws IomsDbAccessException {
        try {
            usersDao.update(user);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to update user for id: {} due to exception.", user.getId(), e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new user.
     *
     * @param users
     * @return
     * @throws IomsDbAccessException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    @ApiOperation("Create a new user")
    @Path(Constants.API_V1_VERSION + "/users/create")
    public Users createUser(@Valid @NotNull final Users users)
            throws IomsDbAccessException {
        try {
            final Users user =  usersDao.create(users);
            return user;
        } catch (Exception e) {
            log.error("failed to create user for name:" + users.getUserName()
                    +" due to exception." , e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all users by their roleId.
     *
     * @param id
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular user with its id")
    @Path(Constants.API_V1_VERSION + "/users/role/{id}")
    public List<Users> getUserByUserRoleId(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            final List<Users> allSalesman = usersDao.findAllUserByRole(id);
            return allSalesman;
        } catch (Exception e) {
            log.error("failed to fetch a user for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
