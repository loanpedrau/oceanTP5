package org.inria.restlet.mta.backend;

/**
 * Un requin a une durée de vie constante (par exemple 5 cycles). 
 * Ils sont initialement affectés à une zone choisie aléatoirement à la création de l’océan.
 * @author Loan et Hafsa
 *
 */
public class Requin extends Thread{
    
    private int nbCycleRestant;
    private Zone[][] zones;
    private Zone actualZone;
    private int nbZone;
    private int capacitePilotes; //nombre maximum de poissons pilotes qu'il peut transporter
    private int cptPilotes; //nombre de poisson pilotes accrochés à lui
    private int sleep = 100;
    
    public Requin(Zone[][] zones, Zone actualZone, int nbZone ) {
        this.nbCycleRestant = 5;
        this.zones = zones;
        this.actualZone = actualZone;
        this.nbZone = nbZone;
        this.capacitePilotes = 5;
        this.cptPilotes = 0;
    }
    
    /**
     *  Cycle de vie du requin :
     *  - Il sort de sa zone du cycle précédent
     *  - Il choisit de facon aléatoire une zone contigue à celle-ci (à gauche, à droite, au-dessus, en-dessous)
     *  - Il entre dans la zone si celle-ci n’est pas déjà occupée par un autre requin, sinon, il doit attendre que celui-ci sorte pour y rentrer
     *  - Il reste un certain temps (fixe ou aléatoire) dans sa nouvelle zone, et en profite pour
     *  manger quelques sardines (on veillera à maintenir correctement le nombre de sardines de chaque zone à tout instant.)
     */
    public void run() {
        while(nbCycleRestant > 0) {
            actualZone.sortir();
            Zone nextZone = null;
            int random = (int)(Math.random() * nbZone);
            switch(random) {  //choix zone aléatoire
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
            this.actualZone.entrer(this); //il entre dans la nouvelle zone
            this.avertirPoissonPilote();  //il avertit les poissons pilotes accrochés qu'ils peuvent se détacher
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.actualZone.sharkEat(); //il mange des sardines
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }            
            nbCycleRestant--;
        }
        this.actualZone.sortir(); //quand il a fini ces cycles il libère la zone et "meurt"
        this.avertirPoissonPilote();
    }

    /**
     * Retourne la zone a gauche de la zone courante.
     * L’océan se replie (c'est un tore).
     * @param zone courante
     * @return zone gauche
     */
    private Zone leftZone(Zone zone){
        int x = zone.getX();
        int y = zone.getY();
        if(y == 0) {
            return this.zones[x][nbZone-1];
        }else {
            return this.zones[x][y-1];
        }
    }
    
    /**
     * Retourne la zone a droite de la zone courante.
     * L’océan se replie (c'est un tore).
     * @param zone courante
     * @return zone droite
     */
    private Zone rightZone(Zone zone){
        int x = zone.getX();
        int y = zone.getY();
        if(y == nbZone-1) {
            return this.zones[x][0];
        }else {
            return this.zones[x][y+1];
        }
    }
    
    /**
     * Retourne la zone en haut de la zone courante.
     * L’océan se replie (c'est un tore).
     * @param zone courante
     * @return zone au dessus
     */
    private Zone upperZone(Zone zone){
        int x = zone.getX();
        int y = zone.getY();
        if(x == 0) {
            return this.zones[nbZone-1][y];
        }else {
            return this.zones[x-1][y];
        }
    }
    
    /**
     * Retourne la zone en dessous de la zone courante.
     * L’océan se replie (c'est un tore).
     * @param zone courante
     * @return zone en dessous
     */
    private Zone underZone(Zone zone){
        int x = zone.getX();
        int y = zone.getY();
        if(x == nbZone-1) {
            return this.zones[0][y];
        }else {
            return this.zones[x+1][y];
        }
    }
    
    /**
     * Retourne la zone actuel
     * @return zone actuel
     */
    public Zone getActualZone() {
        return this.actualZone;
    }
    
    /**
     * Averti les poissons pilotes
     */
    public synchronized void avertirPoissonPilote() {
        this.notifyAll();
    }   
    
    /**
     * Permet au poissons pilotes de s'accrocher au requin.
     * @return true si le poisson pilote a reussi à s'accrocher
     */
    public synchronized boolean accrocher() {
        boolean accrocher = false;
        //on empeche les poissons pilotes de s'accrocher dans le dernier cycle du requin, 
        //car le requin va seulement sortir de sa zone et "mourir"
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
    
    /**
     * Permet aux poissons pilotes de se décrocher du requin.
     * @param zone actuel
     * @return nouvelle zone du requin et donc du poisson pilote
     */
    public synchronized Zone decrocher(Zone zone) {
        //tant que le requin n'est pas arrivé dans une nouvelle zone
        //ou qu'il est encore dans la meme zone, le poisson pilote attend
        while(this.actualZone.getRequin() == null || this.actualZone.equals(zone)) {
            try {
                System.out.println("Le poisson pilote : "+Thread.currentThread().getName()+
                        " attends que le requin "+this.getName()+" arrive dans une nouvelle zone");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cptPilotes --;
        this.notifyAll();
        System.out.println("Le poisson pilote : " + Thread.currentThread().getName() + " " +
                "s'est deccroché du requin "+this.getName()+" dans la zone "+this.actualZone.getX()+","+this.actualZone.getY());
        return this.actualZone;
    }
    
    /**
     * Retourne le nombre de cycles restants du requin.
     * @return nbCyclesRestant
     */
    public int getNbCycleRestants() {
        return this.nbCycleRestant;
    }
}
