package org.inria.restlet.mta.database.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import org.inria.restlet.mta.backend.PoissonPilote;
import org.inria.restlet.mta.backend.Requin;
import org.inria.restlet.mta.backend.Zone;
import org.inria.restlet.mta.database.api.Database;

public class Ocean implements Database{
    
    private static Zone[][] zones;
    private ArrayList<Requin> requins;
    private ArrayList<PoissonPilote> poissonsPilotes;
    private static final int NB_ZONE = 4;
    
    public Ocean() {
        Random random = new Random();
        this.zones = new Zone[NB_ZONE][NB_ZONE];
        this.requins = new ArrayList<Requin>();
        this.poissonsPilotes = new ArrayList<PoissonPilote>();
        for(int i=0; i<NB_ZONE; i++) {
            for(int y =0; y<NB_ZONE; y++) {
                Zone zone = new Zone(i,y);
                this.zones[i][y] = zone;
            }
        }
        for(int i=0; i<NB_ZONE; i++) {
            for(int y =0; y<NB_ZONE; y++) {
                if(random.nextBoolean()) {
                    Zone actualZone = this.zones[i][y];
                    Requin requin =  new Requin(this.zones, actualZone, NB_ZONE);
                    this.requins.add(requin);
                    actualZone.setSharkPresent(true);
                    actualZone.setRequin(requin);
                }
            }
        }
        for(int i =0; i<5; i++) {
            
            PoissonPilote poissonPilote = new PoissonPilote(this.zones, this.zones[1][1]);
            poissonsPilotes.add(poissonPilote);
            
        }
        for(int i=0; i< this.poissonsPilotes.size(); i++) {
            this.poissonsPilotes.get(i).start();
        }
        for(int i=0; i< this.requins.size(); i++) {
            this.requins.get(i).start();
        }
    }

    @Override
    public Zone getZone(String coordinates) {
        String[] xy = coordinates.split(",");
        int x = Integer.valueOf(xy[0]);
        int y = Integer.valueOf(xy[1]);
        return this.zones[x][y];
    }


    @Override
    public int getNbRequins() {
        int nbRequinActifs = 0;
        for(int i=0; i< this.requins.size(); i++) {
            if(this.requins.get(i).isAlive()) {
                nbRequinActifs++;
            }
        }
        return nbRequinActifs;
    }


    @Override
    public Requin getRequin(int id) {
        return this.requins.get(id);
    }


    @Override
    public int getNbSardines() {
        int nbSardines = 0;
        for(int i=0; i<this.NB_ZONE; i++) {
            for(int y=0; y<this.NB_ZONE; y++) {
               nbSardines += this.zones[i][y].getNbSardines();
            }
        }
        return nbSardines;
    }
    
    public void addShark() {
        Optional<Zone> optionalZone = getAvailableZone();
        if(!optionalZone.isEmpty()) {
            Zone zone = optionalZone.get();
            Requin requin =  new Requin(this.zones, zone, NB_ZONE);
            this.requins.add(requin);
            zone.setSharkPresent(true);
            zone.setRequin(requin);
            requin.start();
        }
    }
    
    private Optional<Zone> getAvailableZone() {
        for(int i=0; i<this.NB_ZONE; i++) {
            for(int y=0; y<this.NB_ZONE; y++) {
               if(!this.zones[i][y].isSharkPresent()) {
                   return Optional.ofNullable(this.zones[i][y]);
               }
            }
        }
        return Optional.empty();
    }
}
