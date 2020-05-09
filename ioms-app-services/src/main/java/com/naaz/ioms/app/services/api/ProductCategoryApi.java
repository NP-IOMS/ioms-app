package com.naaz.ioms.app.services.api;


import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.app.services.util.Constants;
import com.naaz.ioms.data.access.dao.ProductCategoryDao;
import com.naaz.ioms.data.access.entities.ProductCategory;
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
@Api("ProductCategory")
@Slf4j
public class ProductCategoryApi {

    private ProductCategoryDao productCategoryDao;

    public ProductCategoryApi(ProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }

    /**
     * api to fetch all ProductCategory available.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all ProductCategory in db")
    @Path(Constants.API_V1_VERSION + "/product/category")
    public List<ProductCategory> getAllProductCategory() throws IomsDbAccessException {
        try{
            return productCategoryDao.findAll();
        } catch (Exception ex) {
            log.error("failed to fetch all ProductCategory due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch all ProductCategory available for a status.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all ProductCategory in db")
    @Path(Constants.API_V1_VERSION + "/product/categories")
    public List<ProductCategory> getAllProductCategory(@Valid @NotNull @HeaderParam("category-status") final Boolean status) throws IomsDbAccessException {
        try{
            return productCategoryDao.fetchAllProductCategoryForStatus(status);
        } catch (Exception ex) {
            log.error("failed to fetch all ProductCategory due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular productCategory by its id.
     *
     * @param id
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular productCategory with its id")
    @Path(Constants.API_V1_VERSION + "/product/category/{id}")
    public ProductCategory getProductCategoryById(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            return productCategoryDao.find(id);
        } catch (Exception e) {
            log.error("failed to fetch a productCategory for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular productCategory by its id.
     *
     * @param id
     * @return
     * @throws IomsDbAccessException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular productCategory with its id")
    @Path(Constants.API_V1_VERSION + "/product/category/delete/{id}")
    public Response deleteProductCategory(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            productCategoryDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to delete productCategory for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing productCategory.
     *
     * @param productCategory
     * @return
     * @throws IomsDbAccessException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing productCategory")
    @Path(Constants.API_V1_VERSION + "/product/category/update")
    public Response updateProductCategory(@Valid @NotNull final ProductCategory productCategory)
            throws IomsDbAccessException {
        try {
            productCategoryDao.update(productCategory);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to update productCategory for id: {} due to exception.", productCategory.getId(), e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new productCategory.
     *
     * @param productCategory
     * @return
     * @throws IomsDbAccessException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new productCategory")
    @Path(Constants.API_V1_VERSION + "/product/category/create")
    public ProductCategory createProduct(@Valid @NotNull final ProductCategory productCategory)
            throws IomsDbAccessException {
        try {
            final ProductCategory newProduct =  productCategoryDao.create(productCategory);
            return newProduct;
        } catch (Exception e) {
            log.error("failed to create productCategory for name:" + productCategory.getProductCategoryName()
                    +" due to exception." , e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

}
