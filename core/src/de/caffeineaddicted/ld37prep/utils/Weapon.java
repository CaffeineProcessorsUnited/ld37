package de.caffeineaddicted.ld37prep.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import de.caffeineaddicted.ld37prep.actor.Projectile;

import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class Weapon {
    private float projectiles = 1;
    private float speed = 2;
    private float damage = 10;

    public Weapon() {

    }

    public float projectiles() {
        return projectiles;
    }

    public void upgradeProjectiles() {
        projectiles++;
    }

    public float speed() {
        return speed;
    }

    public void upgradeSpeed() {
        speed *= 0.90;
    }

    public float damage() {
        return damage;
    }

    public void upgradeDamage() {
        damage *= 1.1;
    }

    public ArrayList<Projectile> spawProjectiles(Vector2 playerCenter) {
        ArrayList<Projectile> p = new ArrayList<Projectile>();
        for (int i = 0; i < projectiles; i++) {
            p.add(new Projectile(speed, damage));
        }
        return p;
    }
}
