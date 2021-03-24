/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.cloud.policies.app.rest;

import com.redhat.cloud.policies.app.NotificationSystem;
import com.redhat.cloud.policies.app.auth.RhIdPrincipal;
import com.redhat.cloud.policies.app.model.Msg;
import com.redhat.cloud.policies.app.model.SettingsValues;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.logging.Logger;

/**
 * @author hrupp
 */
@Path("/api/policies/v1.0/preferences")
@Produces("application/json")
@Consumes("application/json")
@SimplyTimed(absolute = true, name = "UserConfigSvc")
@RequestScoped
public class UserPreferencesService {

  public static final String FALSE = "false";
  public static final String TRUE = "true";
  private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

  @SuppressWarnings("CdiInjectionPointsInspection")
  @Inject
  RhIdPrincipal user;

  @Inject
  @RestClient
  NotificationSystem notifications;

  @GET
  @Path("/preferences")
  public Response getSettingsSchema() {

    if (!user.canReadPolicies()) {
       return Response.status(Response.Status.FORBIDDEN).entity("You don't have permission to read settings").type(MediaType.TEXT_PLAIN_TYPE).build();
     }

    String response ;
    Response.ResponseBuilder builder;
    try {
      response = settingsString;

      // Now we need to find the user record and populate the reply accordingly
      SettingsValues values = SettingsValues.findById(user.getName());
      if (values != null) {
        response = response.replace("%1", values.immediateEmail ? TRUE : FALSE);
        response = response.replace("%2", values.dailyEmail ? TRUE : FALSE);
      }
      else {
        // User's record does not yet exist, so use defaults
        response = response.replace("%1", FALSE);
        response = response.replace("%2", FALSE);
      }
      builder = Response.ok(response);
      EntityTag etag = new EntityTag(String.valueOf(response.hashCode()));
      builder.header("ETag",etag);
    }
    catch (Exception e) {
      builder= Response.serverError();
      builder.entity(new Msg(e.getMessage()));
      log.warning("Retrieving settings failed: " + e.getMessage());
    }

    return builder.build();
  }
}
