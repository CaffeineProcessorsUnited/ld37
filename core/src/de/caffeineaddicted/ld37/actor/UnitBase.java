package de.caffeineaddicted.ld37.actor;

import de.caffeineaddicted.sgl.etities.Entity;

abstract public class UnitBase extends Entity {
    private float hp;
    private float maxhp;

    public float getMaxHP() {
        return maxhp;
    }

    public void setMaxhp(float maxhp) {
        this.maxhp = maxhp;
    }

    abstract protected void onDie();

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }


    public void receiveDamage(float damage) {
        hp -= damage;
        if (hp < 0) {
            onDie();
        }
    }

    public boolean alive() {
        return hp >= 0;
    }
}
