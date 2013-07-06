package fr.duchesses.moov.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import fr.duchesses.moov.apis.AutolibApiService;
import fr.duchesses.moov.apis.RatpApiService;
import fr.duchesses.moov.apis.SncfApiService;
import fr.duchesses.moov.apis.VelibApiService;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.util.HashMap;
import java.util.Map;

public class GuiceConfiguration extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {

                bind(AutolibApiService.class).in(Singleton.class);
                bind(VelibApiService.class).in(Singleton.class);
                bind(RatpApiService.class).in(Singleton.class);
                bind(SncfApiService.class).in(Singleton.class);

                // hook Jackson into Jersey as the POJO <-> JSON mapper
                bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);

                ResourceConfig rc = new PackagesResourceConfig("fr.duchesses.moov");
                for (Class<?> resource : rc.getClasses()) {
                    bind(resource);
                }

                Map<String, String> jerseyParams = new HashMap<String, String>();
                jerseyParams.put("com.sun.jersey.config.property.packages", "fr.duchesses.moov");
                jerseyParams.put("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.GZIPContentEncodingFilter");
                serve("/rest/*").with(GuiceContainer.class, jerseyParams);

                //serve("/rest/*").with(GuiceContainer.class);
            }
        });
    }

}
