package com.naaz.ioms.app.services.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.app.services.util.Constants;
import com.naaz.ioms.data.access.dao.UsersDao;
import com.naaz.ioms.data.access.entities.Users;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Api("Login")
@Slf4j
public class LoginApi {
    private final UsersDao usersDao;

    public LoginApi(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    /**
     * api to check user credibility.
     *
     * @return
     * @throws IomsDbAccessException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Check if user is valid")
    @Path(Constants.API_V1_VERSION + "/login")
    public List<Users> checkValidUser(@Valid @NotNull final Users user) throws IomsDbAccessException {
        try{
            log.info("Authenticating user : {}", new ObjectMapper().writeValueAsString(user));
            return usersDao.findActiveUser(user);
        } catch (Exception ex) {
            log.error("failed to fetch active user due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
