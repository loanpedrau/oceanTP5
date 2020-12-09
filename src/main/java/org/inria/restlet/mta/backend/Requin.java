package org.inria.restlet.mta.backend;

import java.util.ArrayList;
import java.util.Collection;

public class Requin extends Thread{
    
    private int nbCycleRestant;
    private Zone[][] zones;
    private Zone actualZone;
    private int nbZone;
    private int capacitePilotes;
    private int cptPilotes;
    private int sleep = 2500;
    
    public Requin(Zone[][] zones, Zone actualZone, int nbZone ) {
        this.nbCycleRestant = 5;
        this.zones = zones;
        this.actualZone = actualZone;
        this.nbZone = nbZone;
        this.capacitePilotes = 5;
        this.cptPilotes = 0;
    }
    
    public void run() {
        while(nbCycleRestant > 0) {
            actualZone.sortir();
            Zone nextZone = null;
            int random = (int)(Math.random() * nbZone);
            switch(random) {
            case 0:
                nextZone = leftZone(actualZone);
                break;
            case 1 :
                nextZone = rightZone(actualZone);
                break;
            case 2:
                nextZone = upperZone(actualZone);
                break;
            case 3 :
                nextZone = underZone(actualZone);
                break;
            default :
                nextZone = underZone(actualZone);
            }
            this.actualZone = nextZone;
            this.actualZone.entrer(this);
            this.avertirPoissonPilote();
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.actualZone.sharkEat();
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }            
            nbCycleRestant--;
        }
        this.actualZone.sortir();
        this.avertirPoissonPilote();
    }

    private Zone leftZone(Zone zone){
        int x = zone.getX();
        int y = zone.getY();
        if(y == 0) {
            return this.zones[x][nbZone-1];
        }else {
            return this.zones[x][y-1];
        }
    }
    
    private Zone rightZone(Zone zone){
        int x = zone.getX();
        int y = zone.getY();
        if(y == nbZone-1) {
            return this.zones[x][0];
        }else {
            return this.zones[x][y+1];
        }
    }
    
    private Zone upperZone(Zone zone){
        int x = zone.getX();
        int y = zone.getY();
        if(x == 0) {
            return this.zones[nbZone-1][y];
        }else {
            return this.zones[x-1][y];
        }
    }
    
    private Zone underZone(Zone zone){
        int x = zone.getX();
        int y = zone.getY();
        if(x == nbZone-1) {
            return this.zones[0][y];
        }else {
            return this.zones[x+1][y];
        }
    }
    
    public Zone getActualZone() {
        return this.actualZone;
    }
    
    public synchronized void avertirPoissonPilote() {
        this.notifyAll();
    }   
    
    public synchronized boolean accrocher(Zone zone) {
        boolean accrocher = false;
        if(this.nbCycleRestant > 1) {
            while(this.cptPilotes >= this.capacitePilotes){
                try {
                    System.out.println("Le poisson pilote : "+Thread.currentThread().getName()+
                            " attends le requin "+this.getName());
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.cptPilotes++;
            accrocher = true;
            System.out.println("Le poisson pilote : " + Thread.currentThread().getName() + " " +
                    "s'est accroché au requin "+this.getName());
        }
        return accrocher;
    }
    
    public synchronized Zone decrocher(Zone zone) {
        while(this.actualZone.getRequin() == null || this.actualZone.equals(zone)) {
            try {
                System.out.println("Le poisson pilote : "+Thread.currentThread().getName()+
                        " attends que le requin "+this.getName()+" arrive dans une nouvelle zone ");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cptPilotes --;
        this.notifyAll();
        System.out.println(" poisson pilote : " + Thread.currentThread().getName() + " " +
                "s est deccroché du requin "+this.getName()+" dans la zone "+this.actualZone.getX()+","+this.actualZone.getY());
        return this.actualZone;
    }
    
    public int getNbCycleRestants() {
        return this.nbCycleRestant;
    }
}
