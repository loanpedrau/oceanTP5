package org.inria.restlet.mta.backend;

/**
 * Les poissons pilotes se d�placent en s�ins�rant dans l�onde g�n�r�e par le
 * requin, et vont ainsi passer de zone en zone en s�attachant aux requins qui passent l� ou ils
 * se trouvent.
 * @author Loan et Hafsa
 *
 */
public class PoissonPilote extends Thread{

    private Zone[][] zones;
    private Zone actualZone;
    private Requin requin;
    private int nbCycleRestant;

    public PoissonPilote(Zone[][] zones, Zone actualZone) {
        this.zones = zones;
        this.actualZone = actualZone;
        this.nbCycleRestant = 2;  //les poissons pilotes se d�placent de deux cases
    }
    
    /**
     * Les poissons pilote cherchent � s�attacher � un requin dans leur zone courante (ils attendent pour cela
     * qu�un requin y soit pr�sent avec une place libre.)
     * Ils se d�placent ensuite: ils attendent la notification de l�arriv�e dans la zone suivante du
     * requin auquel ils se sont attach�s.
     */
    public void run() {
        while(nbCycleRestant >0) {
            this.actualZone.attendreEntrerRequin(); //poisson pilote attend qu'un requin entre dans la zone
            this.requin = this.actualZone.getRequin(); 
            if(this.requin.accrocher()) { //un requin est entr� donc il essai de s'y attacher (si il y a de la place)
                this.actualZone = this.requin.decrocher(this.actualZone); //il se d�croche du requin et atterit dans une nouvelle zone
            }
            nbCycleRestant--;
        }
    }
}
