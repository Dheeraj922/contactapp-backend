package com.dheeraj.contactapp;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer{

    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class);
    }
}
