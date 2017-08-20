package com.test.web.crawler;

import com.test.web.crawler.configuration.LocalCache;
import com.test.web.crawler.configuration.MainConfiguration;
import com.test.web.crawler.configuration.SwaggerConfiguration;
import com.test.web.crawler.configuration.WebConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 *
 */
public class AppInitializer implements WebApplicationInitializer {

    private static final String CACHE_PROFILE_ENV_VAR = "CACHE_PROFILE";
    private static final String DEFAULT_CACHE_PROFILE = LocalCache.PROFILE;

    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(MainConfiguration.class);

        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(rootContext);
        contextLoaderListener.setContextInitializers(new EnvironmentAwareApplicationContextInitializer());
        servletContext.addListener(contextLoaderListener);

        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

        dispatcherContext.setServletContext(servletContext);
        dispatcherContext.setParent(rootContext);
        dispatcherContext.register(WebConfiguration.class, SwaggerConfiguration.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    private class EnvironmentAwareApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

        @Override
        public void initialize(ConfigurableWebApplicationContext applicationContext) {
            String cacheProfile = getProfile(CACHE_PROFILE_ENV_VAR, DEFAULT_CACHE_PROFILE);

            if (!cacheProfile.equals(LocalCache.PROFILE)) {
                throw new IllegalArgumentException(CACHE_PROFILE_ENV_VAR + " value " + cacheProfile + " is illegal. Allowed values is " + LocalCache.PROFILE);
            }

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.setActiveProfiles(cacheProfile);
        }

        private String getProfile(String profileEnvVariable, String defaultValue) {
            String appProfile = System.getenv(profileEnvVariable);
            if (appProfile == null) {
                appProfile = System.getProperty(profileEnvVariable);
            }
            if (isBlank(appProfile)) {
                appProfile = defaultValue;
            }

            return trim(appProfile);
        }
    }

}
