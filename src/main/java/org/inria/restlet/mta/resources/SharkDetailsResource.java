package org.inria.restlet.mta.resources;

import org.inria.restlet.mta.backend.Backend;
import org.inria.restlet.mta.backend.Requin;
import org.inria.restlet.mta.backend.Zone;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class SharkDetailsResource extends ServerResource{

    /** Backend.*/
    private Backend backend_;
    
    /**
     * Constructor.
     * Call for every single user request.
     */
    public SharkDetailsResource()
    {
        backend_ = (Backend) getApplication().getContext().getAttributes()
                .get("backend");
    }
    
    @Get("json")
    public Representation getSharkDetails() throws Exception
    {
        String sharkIdString = (String) getRequest().getAttributes().get("sharkId");
        int sharkId = Integer.valueOf(sharkIdString);
        Requin requin = backend_.getDatabase().getRequin(sharkId);
        JSONObject sharkObject = new JSONObject();
        sharkObject.put("position", requin.getActualZone().getX()+","+requin.getActualZone().getY() );
        sharkObject.put("nbCyclesRestants", requin.getNbCycleRestants());
        return new JsonRepresentation(sharkObject);
    }

}
