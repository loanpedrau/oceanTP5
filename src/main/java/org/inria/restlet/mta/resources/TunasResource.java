package org.inria.restlet.mta.resources;

import org.inria.restlet.mta.backend.Backend;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Classe permettant de répondre aux fonctionnalités suivantes de l'api rest.
 * - récupérer le nombre global de sardines restantes
 * @author Loan et Hafsa
 *
 */
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

    /**
     * Retourne le nomble global de sardines restantes dans l'océan.
     * @return nb sardines restantes
     * @throws Exception
     */
    @Get("json")
    public Representation getNbSardines() throws Exception
    {
        int nbSardines = backend_.getDatabase().getNbSardines();
        JSONObject sardineObject = new JSONObject();
        sardineObject.put("nbSardines", nbSardines);
        return new JsonRepresentation(sardineObject);
    }
    
    
}
