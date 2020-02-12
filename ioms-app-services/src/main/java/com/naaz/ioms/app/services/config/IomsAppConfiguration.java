package com.naaz.ioms.app.services.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
public class IomsAppConfiguration extends Configuration {

    @JsonProperty
    private boolean useSwagger = true;

    @Valid
    @NotNull
    @JsonProperty("database")
    private final DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @NotNull
    @JsonProperty("httpClientConfiguration")
    private final JerseyClientConfiguration httpClientConfiguration = new JerseyClientConfiguration();

}
