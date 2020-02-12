package com.naaz.ioms.app.services.bundle;


import com.naaz.ioms.app.services.config.IomsAppConfiguration;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class SwitchableSwaggerBundle extends SwaggerBundle<IomsAppConfiguration> {

    @Override
    protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(IomsAppConfiguration configuration) {
        return configuration.swaggerBundleConfiguration;
    }

    @Override
    public void run(IomsAppConfiguration configuration, Environment environment) throws Exception {
        if (configuration.isUseSwagger()) {
            super.run(configuration, environment);
        }
    }
}
