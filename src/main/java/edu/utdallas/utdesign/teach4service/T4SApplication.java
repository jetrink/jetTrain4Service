package edu.utdallas.utdesign.teach4service;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.opentok.OpenTok;
import edu.utdallas.utdesign.teach4service.auth.AuthHelper;
import edu.utdallas.utdesign.teach4service.auth.SessionToken;
import edu.utdallas.utdesign.teach4service.auth.T4SAuthenticator;
import edu.utdallas.utdesign.teach4service.auth.T4SAuthorizer;
import edu.utdallas.utdesign.teach4service.auth.csrfAuth.CSRFAuthFilter;
import edu.utdallas.utdesign.teach4service.db.*;
import edu.utdallas.utdesign.teach4service.resources.*;
import edu.utdallas.utdesign.teach4service.resources.admin.AdminAuthResource;
import edu.utdallas.utdesign.teach4service.resources.admin.AdminProfileResource;
import edu.utdallas.utdesign.teach4service.resources.admin.SubjectsResource;
import edu.utdallas.utdesign.teach4service.resources.admin.TutorApprovalResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import java.security.Permission;

public class T4SApplication extends Application<T4SConfiguration>
{
    public static void main(String[] args) throws Exception
    {
        class MySecurityManager extends SecurityManager
        {

            @Override
            public void checkPermission(Permission perm)
            {
                String permName = perm.getName() != null ? perm.getName() : "missing";
                if(permName.startsWith("exitVM"))
                {
                    throw new ExitTrappedException();
                }
                else if("setSecurityManager".equals(permName))
                {
                    System.out.println("REPLACED!!!");
//                    throw new SecurityException("Cannot replace the FML security manager");
                }

                return;
            }

            @Override
            public void checkPermission(Permission perm, Object context)
            {
                checkPermission(perm);
            }

            class ExitTrappedException extends SecurityException
            {
                private static final long serialVersionUID = 1L;
            }
        }

        System.setSecurityManager(new MySecurityManager());

        new T4SApplication().run(args);
    }

    private final HibernateBundle<T4SConfiguration> hibernate = new ScanningHibernateBundle<T4SConfiguration>("edu.utdallas.utdesign.teach4service.db.entities")
    {
        public DataSourceFactory getDataSourceFactory(T4SConfiguration configuration)
        {
            return configuration.getDatabase();
        }
    };

    @Override
    public void initialize(Bootstrap<T4SConfiguration> bootstrap)
    {
        // host the static web folder
        bootstrap.addBundle(new AssetsBundle("/static", "/", "index.html"));

        // connect to the DB
        bootstrap.addBundle(hibernate);

        // add flyway for migrations
        bootstrap.addBundle(new FlywayBundle<T4SConfiguration>()
        {
            @Override
            public DataSourceFactory getDataSourceFactory(T4SConfiguration configuration)
            {
                return configuration.getDatabase();
            }

            @Override
            public FlywayFactory getFlywayFactory(T4SConfiguration configuration)
            {
                return configuration.getFlyway();
            }
        });
    }

    @Override
    public void run(T4SConfiguration config, Environment environment) throws Exception
    {
        // use ISO 8601 date format instead of unix timestamps
        environment.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // register auth
        final AuthHelper authHelper = new AuthHelper(config.getAuth());
        environment.jersey().register(
                new AuthDynamicFeature(
                        new CSRFAuthFilter.Builder<SessionToken>()
                                .setCookieName(AuthHelper.COOKIE_NAME)
                                .setCsrfHeader(AuthHelper.CSRF_HEADER)
                                .setAuthenticator(new T4SAuthenticator(authHelper))
                                .setAuthorizer(new T4SAuthorizer())
                                .setPrefix("T4SAuth")
                                .buildAuthFilter()
                )
        );
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(SessionToken.class));

        // setup OpenTok
        OpenTok opentok = new OpenTok(config.getOpentok().getApiKey(), config.getOpentok().getSecret());

        // TODO: register healthchecks (if any)

        // register the endpoints
        environment.jersey().register(new UserResource(new UserDAO(hibernate.getSessionFactory()), authHelper));
        environment.jersey().register(new SessionResource(
                new SessionDAO(hibernate.getSessionFactory()),
                new UserDAO(hibernate.getSessionFactory()),
                new StudentDAO(hibernate.getSessionFactory()),
                new TutorDAO(hibernate.getSessionFactory()),
                new SessionRequestDAO(hibernate.getSessionFactory())));
        environment.jersey().register(new ProfileResource(
                new UserDAO(hibernate.getSessionFactory()),
                new StudentDAO(hibernate.getSessionFactory()),
                new PersonDAO(hibernate.getSessionFactory()),
                new SubjectDAO(hibernate.getSessionFactory()),
                new TutorDAO(hibernate.getSessionFactory()),
                new TutorExpertiseDAO(hibernate.getSessionFactory()),
                new ScheduleDAO(hibernate.getSessionFactory()),
                authHelper)
        );
        environment.jersey().register(new DashboardResource(
                new UserDAO(hibernate.getSessionFactory()),
                new StudentDAO(hibernate.getSessionFactory()),
                new SessionDAO(hibernate.getSessionFactory()),
                new TutorReviewsDAO(hibernate.getSessionFactory()),
                new TutorDAO(hibernate.getSessionFactory()),
                new StudentReviewsDAO(hibernate.getSessionFactory()),
                new PersonDAO(hibernate.getSessionFactory())
        ));
        environment.jersey().register(new AdminAuthResource(new AdminDAO(hibernate.getSessionFactory()), authHelper, config.getAuth().getSystemUser()));
        environment.jersey().register(new AdminProfileResource(new AdminDAO(hibernate.getSessionFactory())));
        environment.jersey().register(new SubjectsResource(new SubjectDAO(hibernate.getSessionFactory())));
        environment.jersey().register(new TutorApprovalResource(new TutorDAO(hibernate.getSessionFactory())));

        environment.jersey().register(new OpenTokResource(config.getOpentok(), opentok, new SessionDAO(hibernate.getSessionFactory())));
    }
}
