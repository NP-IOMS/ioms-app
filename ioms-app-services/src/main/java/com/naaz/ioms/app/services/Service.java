package com.naaz.ioms.app.services;

import com.naaz.ioms.app.services.api.EchoApi;
import com.naaz.ioms.app.services.api.LoginApi;
import com.naaz.ioms.app.services.api.UserRoleApi;
import com.naaz.ioms.app.services.api.UsersApi;
import com.naaz.ioms.app.services.bundle.SwitchableSwaggerBundle;
import com.naaz.ioms.app.services.config.IomsAppConfiguration;
import com.naaz.ioms.app.services.exception.IomsDbAccessExceptionMapper;
import com.naaz.ioms.data.access.dao.UserRoleDao;
import com.naaz.ioms.data.access.dao.UsersDao;
import com.naaz.ioms.data.access.entities.UserRole;
import com.naaz.ioms.data.access.entities.Users;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.ServerProperties;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.client.Client;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Service extends Application<IomsAppConfiguration> {

    public static void main(String[] args) throws Exception {
        new Service().run(args);
    }

    private final HibernateBundle<IomsAppConfiguration> hibernate =
            new HibernateBundle<IomsAppConfiguration>(Users.class, UserRole.class) {
                public DataSourceFactory getDataSourceFactory(IomsAppConfiguration alertProcessorConfiguration) {
                    return alertProcessorConfiguration.getDataSourceFactory();
                }
            };

    private final MigrationsBundle<IomsAppConfiguration> migrations = new MigrationsBundle<IomsAppConfiguration>() {
        public DataSourceFactory getDataSourceFactory(IomsAppConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<IomsAppConfiguration> bootstrap) {
        bootstrap.addBundle(new SwitchableSwaggerBundle());
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(migrations);
    }

    public void run(IomsAppConfiguration iomsAppConfiguration, Environment environment) throws Exception {
        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // DO NOT pass a preflight request to down-stream auth filters
        // unauthenticated preflight requests should be permitted by spec
        cors.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());

        final Client jerseyClient = new JerseyClientBuilder(environment)
                .using(iomsAppConfiguration.getHttpClientConfiguration()).build("HttpClientForSearchService");

        final UsersDao usersDao = new UsersDao(hibernate.getSessionFactory());
        final UserRoleDao userRoleDao = new UserRoleDao(hibernate.getSessionFactory());

        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(ServerProperties.WADL_FEATURE_DISABLE, false);
        environment.jersey().getResourceConfig().addProperties(properties);
        environment.jersey().register(IomsDbAccessExceptionMapper.class);
        environment.jersey().register(new EchoApi());
        environment.jersey().register(new UsersApi(usersDao));
        environment.jersey().register(new UserRoleApi(userRoleDao));
        environment.jersey().register(new LoginApi(usersDao));

        log.info("service started");
    }

}
