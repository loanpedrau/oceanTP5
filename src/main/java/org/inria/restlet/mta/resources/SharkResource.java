package org.inria.restlet.mta.resources;

import org.inria.restlet.mta.backend.Backend;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Classe permettant de r�pondre aux fonctionnalit�s suivantes de l'api rest.
 * -r�cup�rer le nombre de requins encore pr�sents dans la simulation
 * -d�marrer un nouveau requin dans une zone al�atoire (sans requin)
 * @author Loan et Hafsa
 *
 */
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
    
    /**
     * Return a json with the number of shark still alive in the simulation.
     * @return json with nb sharks alive
     * @throws Exception
     */
    @Get("json")
    public Representation getNbSharks() throws Exception
    {
        int nbSharks = backend_.getDatabase().getNbRequins();
        JSONObject sharkObject = new JSONObject();
        sharkObject.put("sharksAlive", nbSharks);
        return new JsonRepresentation(sharkObject);
    }
    
    /**
     * Launch a new shark in the simulation
     * @param representation
     * @throws Exception
     */
    @Post
    public void addShark(JsonRepresentation representation)
        throws Exception
    {
        backend_.getDatabase().addShark();     
    }
    
}
