package com.naaz.ioms.app.services.api;

import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.app.services.util.Constants;
import com.naaz.ioms.app.services.util.FileInfo;
import com.naaz.ioms.app.services.util.Utility;
import com.naaz.ioms.data.access.dao.FilesDao;
import com.naaz.ioms.data.access.entities.Files;
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
@Api("Files")
@Slf4j
public class FilesApi {
    final FilesDao filesDao;

    public FilesApi(FilesDao filesDao) {
        this.filesDao = filesDao;
    }

    /**
     * api to fetch a particular File by its id.
     *
     * @param id
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular File with its id")
    @Path(Constants.API_V1_VERSION + "/file/{id}")
    public Files getFileById(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            return filesDao.find(id);
        } catch (Exception e) {
            log.error("failed to fetch a Files for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular File by its id.
     *
     * @param id
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation("Fetch a particular File with its id")
    @Path(Constants.API_V1_VERSION + "/file/blob/{id}")
    public String getFileBlobById(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            return new String(filesDao.find(id).getFileData());
        } catch (Exception e) {
            log.error("failed to fetch a Files for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular Files by its id.
     *
     * @param id
     * @return
     * @throws IomsDbAccessException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular Files with its id")
    @Path(Constants.API_V1_VERSION + "/file/delete/{id}")
    public Response deleteFiles(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            filesDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to delete Files for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing Files.
     *
     * @param fileInfo
     * @return
     * @throws IomsDbAccessException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing Files")
    @Path(Constants.API_V1_VERSION + "/file/update")
    public Response updateFile(@Valid @NotNull final FileInfo fileInfo)
            throws IomsDbAccessException {
        try {
            final Files newFile =  filesDao.create(Utility.createFilesFromFileInfo(fileInfo));
            filesDao.update(newFile);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to update Files for id: {} due to exception.", fileInfo.getFileId(), e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new File.
     *
     * @param fileInfo
     * @return
     * @throws IomsDbAccessException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new File")
    @Path(Constants.API_V1_VERSION + "/file/create")
    public Files createFile(@Valid @NotNull final FileInfo fileInfo)
            throws IomsDbAccessException {
        try {

            final Files newFile =  filesDao.create(Utility.createFilesFromFileInfo(fileInfo));
            return newFile;
        } catch (Exception e) {
            log.error("failed to create Files for name:" + fileInfo.getFileName()
                    +" due to exception." , e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
