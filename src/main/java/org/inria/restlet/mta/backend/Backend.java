package org.inria.restlet.mta.backend;

import org.inria.restlet.mta.database.api.Database;
import org.inria.restlet.mta.database.api.impl.Ocean;

/**
 *
 * Backend for all resources.
 * 
 * @author ctedeschi
 * @author msimonin
 *
 */
public class Backend
{
    /** The object ocean simulate the database.*/
    private Ocean ocean;

    public Backend()
    {
        ocean = new Ocean();
    }

    public Database getDatabase()
    {
        return ocean;
    }

}
