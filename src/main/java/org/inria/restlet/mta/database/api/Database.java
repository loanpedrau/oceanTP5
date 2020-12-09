package org.inria.restlet.mta.database.api;

import java.util.Collection;
import java.util.List;

import org.inria.restlet.mta.backend.Requin;
import org.inria.restlet.mta.backend.Zone;
import org.inria.restlet.mta.internals.Tweet;
import org.inria.restlet.mta.internals.User;

/**
 *
 * Interface to the database.
 *
 * @author msimonin
 *
 */
public interface Database{

    /**
     *
     * Returns a zone
     *
     * @return zone
     */
    Zone getZone(String coordinates);

    int getNbRequins();
 
    Requin getRequin(int id);
    
    int getNbSardines();
    
    void addShark();
    
}
