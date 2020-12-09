package org.inria.restlet.mta.resources;

import org.inria.restlet.mta.backend.Backend;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class TunasResource extends ServerResource{

    /** Backend.*/
    private Backend backend_;
    
    /**
     * Constructor.
     * Call for every single user request.
     */
    public TunasResource()
    {
        backend_ = (Backend) getApplication().getContext().getAttributes()
                .get("backend");
    }

    @Get("json")
    public Representation getNbSardines() throws Exception
    {
        int nbSardines = backend_.getDatabase().getNbSardines();
        JSONObject sardineObject = new JSONObject();
        sardineObject.put("nbSardines", nbSardines);
        return new JsonRepresentation(sardineObject);
    }
    
    
}
