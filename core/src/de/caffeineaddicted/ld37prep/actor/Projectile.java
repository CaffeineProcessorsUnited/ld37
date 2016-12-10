package de.caffeineaddicted.ld37prep.actor;

import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37prep.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Entity;
import de.caffeineaddicted.sgl.utils.MathUtils;

import java.util.ArrayList;

public class Projectile extends Entity {

    private float directionX, directionY;
    private boolean finished;
    private String ACTOR_PROJECTILE;
    private float speed, damage;

    public Projectile(float speed, float damage) {
        super();
        this.speed = speed;
        this.damage = damage;
        directionX = directionY = 0;
        finished = false;
        zindex(GameScreen.Z_INDEX.Projectile);
        ACTOR_PROJECTILE = addTexture("raw/projectile.png");
        update();
    }

    public void onDie() {
        SGL.provide(GameScreen.class).deleteLater.add(this);
    }

    @Override
    public void update() {
    }

    @Override
    public void act(float delta) {
        if (finished) {
            onDie();
            return;
        }
        moveBy(0, speed * delta);

        ArrayList<Entity> entities = Entity.getEntitiesInRange(getCenterPoint().x, getCenterPoint().y, getWidth() / 2);
        for (Entity entity : entities) {
            if (entity instanceof UnitEnemy) {
                UnitEnemy enemy = (UnitEnemy) entity;
                if (!enemy.alive())
                    continue;
                finished = true;
                enemy.receiveDamage(damage);
            }
        }
        
        if (getY() >= 0) {
            finished = true;
        }
    }
}
