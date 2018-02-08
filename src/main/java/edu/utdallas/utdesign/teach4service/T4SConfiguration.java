package edu.utdallas.utdesign.teach4service;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class T4SConfiguration extends Configuration
{
    @Valid
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    private FlywayFactory flyway = new FlywayFactory();

    @Valid
    private AuthConfig auth = new AuthConfig();

    @Valid
    private OpenTokConfig opentok = new OpenTokConfig();

    @Data
    public static class AuthConfig
    {
        @NotBlank
        private String cookieSecret;

        @Min(600)
        @Max(86400)
        private int cookieAge = 3600;

        @NotNull
        private SystemUserConfig systemUser;
    }

    @Data
    public static class SystemUserConfig
    {
        private boolean enabled = false;

        @NotBlank
        private String password;
    }

    @Data
    public static class OpenTokConfig
    {
        @Min(10000000)
        @Max(100000000)
        private int    apiKey;
        @NotBlank
        private String secret;
    }
}
