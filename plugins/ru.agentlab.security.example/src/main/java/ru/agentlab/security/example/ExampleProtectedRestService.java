/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;

@Path("/booking")
@Provider
@Component(service = ExampleProtectedRestService.class, property = { "osgi.jaxrs.resource=true" })
public class ExampleProtectedRestService {

    private final Map<Long, Booking> bookings = new HashMap<>();

    public ExampleProtectedRestService() {
        bookings.put(1L, new Booking() {
            {
                setId(1L);
                setCustomer("sdfsdf");
                setFlight("werewr");
            }
        });
    }

    @Path("/")
    @Produces("application/json")
    @GET
    public Collection<Booking> list() {
        return bookings.values();
    }

    @Path("/{id}")
    @Produces("application/json")
    @GET
    public Booking get(@PathParam("id") Long id) {
        return bookings.get(id);
    }

    @Path("/")
    @Consumes("application/json")
    @POST
    public void add(Booking booking) {
        bookings.put(booking.getId(), booking);
    }

    @Path("/")
    @Consumes("application/json")
    @PUT
    public void update(Booking booking) {
        bookings.remove(booking.getId());
        bookings.put(booking.getId(), booking);
    }

    @Path("/{id}")
    @DELETE
    public void remove(@PathParam("id") Long id) {
        bookings.remove(id);
    }
}
