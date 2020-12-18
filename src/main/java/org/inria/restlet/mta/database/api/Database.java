package org.inria.restlet.mta.database.api;

import org.inria.restlet.mta.backend.Requin;
import org.inria.restlet.mta.backend.Zone;

/**
 *
 * Interface to the ocean (database role).
 * Functionnalities of the Rest API
 * @author Loan et Hafsa
 *
 */
public interface Database{

    Zone getZone(String coordinates);

    int getNbRequins();
 
    Requin getRequin(int id);
    
    int getNbSardines();
    
    void addShark();
    
}
