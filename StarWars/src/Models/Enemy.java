package Models;

import static Constants.Constants.*;

public class Enemy extends Rectangle {

    private long deathTimestamp;
    private int deathAnimationIndex;
    private int health;

    public Enemy(int x, int y, int z, int width, int height) {
        super(x, y, z, width, height);
        this.deathTimestamp = -1;
        this.deathAnimationIndex = 0;
        this.health = enemyHealth;
    }

    public void setDeathTimestamp(long deathTimestamp){
        this.deathTimestamp = deathTimestamp;
    }

    public long getDeathTimestamp(){
        return this.deathTimestamp;
    }

    public void setDeathAnimationIndex(int deathAnimationIndex){
        this.deathAnimationIndex = deathAnimationIndex;
    }

    public int getDeathAnimationIndex(){
        return this.deathAnimationIndex;
    }

    public int getHealht(){
        return this.health;
    }

    public void setHealht(int health){
        this.health = health;
    }
}
