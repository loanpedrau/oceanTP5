package org.inria.restlet.mta.application;

import org.inria.restlet.mta.resources.SharkDetailsResource;
import org.inria.restlet.mta.resources.SharkResource;
import org.inria.restlet.mta.resources.TunasResource;
import org.inria.restlet.mta.resources.ZoneResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 *
 * Ocean application.
 *
 * @author Loan et Hafsa
 *
 */
public class MyOceanApplication extends Application
{

    public MyOceanApplication(Context context)
    {
        super(context);
    }

    @Override
    public Restlet createInboundRoot()
    {
        Router router = new Router(getContext());
        router.attach("/sharks", SharkResource.class);
        router.attach("/sharks/{sharkId}", SharkDetailsResource.class);
        router.attach("/zones/{zoneId}", ZoneResource.class);
        router.attach("/tunas", TunasResource.class);
        return router;
    }
}
