package org.inria.restlet.mta.resources;

import org.inria.restlet.mta.backend.Backend;
import org.inria.restlet.mta.backend.Zone;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ZoneResource extends ServerResource{

    /** Backend.*/
    private Backend backend_;
    
    /**
     * Constructor.
     * Call for every single user request.
     */
    public ZoneResource()
    {
        backend_ = (Backend) getApplication().getContext().getAttributes()
                .get("backend");
    }
    
    @Get("json")
    public Representation getZoneInformations() throws Exception
    {
        String coordinates = (String) getRequest().getAttributes().get("zoneId");
        Zone zone = backend_.getDatabase().getZone(coordinates);
        JSONObject zoneObject = new JSONObject();
        zoneObject.put("sharkIsPresent", zone.isSharkPresent());
        zoneObject.put("nbSardines", zone.getNbSardines());

        return new JsonRepresentation(zoneObject);
    }
    
}
