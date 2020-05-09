package com.naaz.ioms.app.services.api;

import com.naaz.ioms.app.services.exception.IomsDbAccessException;
import com.naaz.ioms.app.services.util.Constants;
import com.naaz.ioms.data.access.dao.ProductDao;
import com.naaz.ioms.data.access.entities.Product;
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
@Api("Product")
@Slf4j
public class ProductApi {

    private ProductDao productDao;

    public ProductApi(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * api to fetch all product available.
     *
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch all product in db")
    @Path(Constants.API_V1_VERSION + "/product")
    public List<Product> getAllProduct() throws IomsDbAccessException {
        try{
            return productDao.findAll();
        } catch (Exception ex) {
            log.error("failed to fetch all product due to exception." , ex);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to fetch a particular product by its id.
     *
     * @param id
     * @return
     * @throws com.naaz.ioms.app.services.exception.IomsDbAccessException
     */
    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Fetch a particular product with its id")
    @Path(Constants.API_V1_VERSION + "/product/{id}")
    public Product getProductById(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            return productDao.find(id);
        } catch (Exception e) {
            log.error("failed to fetch a product for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to delete a particular product by its id.
     *
     * @param id
     * @return
     * @throws IomsDbAccessException
     */
    @DELETE
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Delete a particular product with its id")
    @Path(Constants.API_V1_VERSION + "/product/delete/{id}")
    public Response deleteProduct(@PathParam("id") final long id) throws IomsDbAccessException {
        try {
            productDao.delete(id);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to delete product for id: {} due to exception.", id, e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to update an existing product.
     *
     * @param product
     * @return
     * @throws IomsDbAccessException
     */
    @PUT
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update an existing product")
    @Path(Constants.API_V1_VERSION + "/product/update")
    public Response updateProduct(@Valid @NotNull final Product product)
            throws IomsDbAccessException {
        try {
                productDao.update(product);
            return Response.status(HttpStatus.SC_OK).build();
        } catch (Exception e) {
            log.error("failed to update product for id: {} due to exception.", product.getId(), e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }

    /**
     * api to create a new product.
     *
     * @param product
     * @return
     * @throws IomsDbAccessException
     */
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new product")
    @Path(Constants.API_V1_VERSION + "/product/create")
    public Product createProduct(@Valid @NotNull final Product product)
            throws IomsDbAccessException {
        try {
            final Product newProduct =  productDao.create(product);
            return newProduct;
        } catch (Exception e) {
            log.error("failed to create product for name:" + product.getProductName()
                    +" due to exception." , e);
            throw new IomsDbAccessException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "There was an error processing your " +
                    "request. It has been logged.");
        }
    }
}
