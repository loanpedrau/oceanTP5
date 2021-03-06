package org.inria.restlet.mta.backend;

public class Zone {

    private int x;
    private int y;
    private int nbSardines;         //nb sardines pr�sentes
    private boolean sharkIsPresent;
    private Requin requin;
      
    public Zone(int x, int y) {
        this.nbSardines = 5;
        this.x = x;
        this.y = y;
    }
    
    public void setRequin(Requin requin) {
        this.requin = requin;
    }
    
    public void setSharkPresent(boolean present) {
        this.sharkIsPresent = present;
    }
    
    /**
     * Le requin mange une sardine. 
     */
    public synchronized void sharkEat(){
        if(this.nbSardines > 0){
            this.nbSardines --;
        }
    }
    
    /**
     * Permet � un requin d'entrer dans une zone.
     * @param requin qui veux entrer
     */
    public synchronized void entrer(Requin requin){
        while(sharkIsPresent){
            try {
                System.out.println("Le requin : "+Thread.currentThread().getName()+" attends dans la zone ("+this.x+" , "+this.y+")");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.sharkIsPresent = true;
        this.requin = requin;
        this.notifyAll();
        System.out.println("Le requin : "+Thread.currentThread().getName()+" est rentr� dans la zone ("+this.x+" , "+this.y+")");
    }

    /**
     * Permet � un requin de sortir d'une zone.
     */
    public synchronized void sortir(){
        this.sharkIsPresent = false;
        this.requin = null;
        this.notifyAll();
        System.out.println("Le requin : "+Thread.currentThread().getName()+" est sorti de la zone ("+this.x+" , "+this.y+")");
    }
    
    /**
     * Permet aux poissons pilote d'attendre qu'un requin entre dans la zone.
     */
    public synchronized void attendreEntrerRequin() {
        while(this.requin == null) {
            System.out.println(Thread.currentThread().getName()+" attends un requin dans la zone : "+x+","+y);
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName()+" a trouv� un requin dans la zone : "+x+","+y);
    } 
    
    /**
     * Retourne le requin pr�sent dans la zone.
     * @return
     */
    public Requin getRequin() {
        return this.requin;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
       return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Zone zone = (Zone) obj;
      if (x != zone.x)
        return false;
      if (y != zone.y)
        return false;
      return true;
    }
    
    public int getNbSardines() {
        return this.nbSardines;
    }
    
    public boolean isSharkPresent() {
        return this.sharkIsPresent;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
