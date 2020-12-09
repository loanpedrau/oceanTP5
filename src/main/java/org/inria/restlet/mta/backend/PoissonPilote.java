package org.inria.restlet.mta.backend;

import java.util.ArrayList;
import java.util.Collection;

public class PoissonPilote extends Thread{

    private Zone[][] zones;
    private Zone actualZone;
    private Requin requin;
    private int nbCycleRestant;

    public PoissonPilote(Zone[][] zones, Zone actualZone) {
        this.zones = zones;
        this.actualZone = actualZone;
        this.nbCycleRestant = 2;
    }
    
    public void run() {
        while(nbCycleRestant >0) {
            this.actualZone.attendreEntrerRequin();
            this.requin = this.actualZone.getRequin();
            if(this.requin.accrocher(this.actualZone)) {
                this.actualZone = this.requin.decrocher(this.actualZone);
            }
            nbCycleRestant--;
        }
    }
}
