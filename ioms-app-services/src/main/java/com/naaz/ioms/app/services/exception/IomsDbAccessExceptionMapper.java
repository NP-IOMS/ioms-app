package com.naaz.ioms.app.services.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class IomsDbAccessExceptionMapper implements ExceptionMapper<IomsDbAccessException> {
    public Response toResponse(IomsDbAccessException e) {
        return Response.status(e.getStatus()).entity(e).build();
    }
}
