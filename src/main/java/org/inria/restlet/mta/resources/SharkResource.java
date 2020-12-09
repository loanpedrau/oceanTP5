package org.inria.restlet.mta.resources;

import org.inria.restlet.mta.backend.Backend;
import org.inria.restlet.mta.backend.Zone;
import org.inria.restlet.mta.database.api.impl.Ocean;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class SharkResource extends ServerResource{

    /** Backend.*/
    private Backend backend_;
    
    /**
     * Constructor.
     * Call for every single user request.
     */
    public SharkResource()
    {
        backend_ = (Backend) getApplication().getContext().getAttributes()
                .get("backend");
    }
    
    @Get("json")
    public Representation getNbSharks() throws Exception
    {
        int nbSharks = backend_.getDatabase().getNbRequins();
        JSONObject sharkObject = new JSONObject();
        sharkObject.put("sharksAlive", nbSharks);
        return new JsonRepresentation(sharkObject);
    }
    
    @Post
    public void addShark(JsonRepresentation representation)
        throws Exception
    {
        backend_.getDatabase().addShark();     
    }
    
}
