package de.caffeineaddicted.ld37prep.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import de.caffeineaddicted.ld37prep.screen.GameScreen;
import de.caffeineaddicted.ld37prep.utils.Weapon;
import de.caffeineaddicted.sgl.SGL;

import java.util.ArrayList;

public class UnitPlayer extends UnitBase {

    private final static int baseHP = 100;
    private int money = 0;
    private Weapon weapon;
    private String ACTOR_BASE;


    public UnitPlayer() {
        ACTOR_BASE = addTexture("player.png");
        setWidth(getActor(ACTOR_BASE).getWidth());
        setHeight(getActor(ACTOR_BASE).getHeight());

        weapon = new Weapon();

        update();
    }

    public void fire() {
        ArrayList<Projectile> ps = weapon().spawProjectiles(getCenterPoint());
        float w = 0;
        for(int i = 0; i < ps.size(); i++) {
            w += ps.get(i).getWidth();
        }
        float l = getCenterPoint().x - (w / 2);
        for(int i = 0; i < ps.size(); i++) {
            Projectile p = ps.get(i);
            p.setCenterPosition(i * l, getCenterPoint().y);
            SGL.provide(GameScreen.class).addActor(p);
        }
    }

    @Override
    public void update() {
    }

    @Override
    protected void onDie() {
        SGL.provide(GameScreen.class).loseGame();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_BASE).draw(batch, parentAlpha);
    }

    public Weapon weapon() {
        return weapon;
    }
}
