package org.inria.restlet.mta.database.api.impl;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import org.inria.restlet.mta.backend.PoissonPilote;
import org.inria.restlet.mta.backend.Requin;
import org.inria.restlet.mta.backend.Zone;
import org.inria.restlet.mta.database.api.Database;

/**
 * Classe principale, l'océan est composé de NxN zones, chacune
 * contenant initialement un nombre fixe ou aléatoire de sardines.
 * L'océan est également composé de requins, une case contient 0 ou 1 requin.
 * Pour faciliter le déplacement des poissons, on considère que l’océan se replie (un tore).
 * @author Loan et Hafsa
 *
 */
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
        //initialisation de chaque zone de l'océan
        for(int i=0; i<NB_ZONE; i++) {
            for(int y =0; y<NB_ZONE; y++) {
                Zone zone = new Zone(i,y);
                this.zones[i][y] = zone;
            }
        }
        //ajout aléatoire des requins dans les zones
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
        //ajout de 5 poissons pilotes dans la zone (1,1)
        for(int i =0; i<5; i++) {
            PoissonPilote poissonPilote = new PoissonPilote(this.zones, this.zones[1][1]);
            poissonsPilotes.add(poissonPilote);        
        }
        //lancement des poissons pilotes
        for(int i=0; i< this.poissonsPilotes.size(); i++) {
            this.poissonsPilotes.get(i).start();
        }
        //lancement des requins
        for(int i=0; i< this.requins.size(); i++) {
            this.requins.get(i).start();
        }
    }

    /**
     * Retourne la zone correspondante aux coordonées passés en paramètre
     * de la forme "1,1".
     * @return zone correspondante
     */
    @Override
    public Zone getZone(String coordinates) {
        String[] xy = coordinates.split(",");
        int x = Integer.valueOf(xy[0]);
        int y = Integer.valueOf(xy[1]);
        return this.zones[x][y];
    }


    /**
     * Retourne le nombre total de requin restant dans la simulation.
     * @return nbRequin restant
     */
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


    /**
     * Récupère un requin via un id (ici sa position dans la liste).
     * @return requin
     */
    @Override
    public Requin getRequin(int id) {
        return this.requins.get(id);
    }


    /**
     * Récupère le nombre global de sardines restantes.
     * @return nb sardines restantes
     */
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
    
    /**
     * Demarrer un nouveau requin dans une zone aléatoire (sans requin)
     */
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
    
    /**
     * Retourne une zone aléatoire ne contenant pas de requin.
     * @return zone sans requin
     */
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
