package edu.dhbw.mos.fim.pakt.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Defines base path for rest apis. Has to be set, since quarkus.http.root-path
 * does not work well with quinoa.
 */
@ApplicationPath("/api")
public class PaktApplication extends Application
{

}
