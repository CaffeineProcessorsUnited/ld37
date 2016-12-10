package de.caffeineaddicted.ld37prep.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.ld37prep.screen.GameScreen;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.etities.Image;
import de.caffeineaddicted.sgl.utils.MathUtils;

public class UnitEnemy extends UnitBase {
    public UnitEnemy.Type type;
    private float speed;

    private String ACTOR_UNIT;

    public UnitEnemy(UnitEnemy.Type type, float waveMultiplier) {
        this.type = type;
        setMaxhp(type.hp * waveMultiplier);
        setHp(type.hp * waveMultiplier);
        ACTOR_UNIT = addTexture(type.file);
        zindex(GameScreen.Z_INDEX.Enemy);
        speed = type.speed;
    }

    public UnitEnemy(UnitEnemy.Type type) {
        this(type, 1);
    }

    @Override
    public void receiveDamage(float damage) {
        if (getHp() < 0)
            return;
        super.receiveDamage(damage);
    }

    @Override
    protected void onDie() {
        SGL.provide(GameScreen.class).deleteLater.add(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 position = getCenterPoint();
        Vector2 target = new Vector2(800,600);
        if(position.dst(target) > (speed * delta)){
            Vector2 direction = target.cpy().sub(position).nor();
            SGL.debug("+++"+direction.x+"--"+direction.y);
            moveBy(direction.x * delta * speed,
                    direction.y * delta * speed
                    );
        } else {
            onDie();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        getActor(ACTOR_UNIT).draw(batch, parentAlpha);
    }

    @Override
    public String name() {
        return "UnitEnemy";
    }

    @Override
    public void update() {

    }

    @Override
    public String addTexture(String name, Texture texture) {
        return addActor(name, new Image(texture));
    }

    public static enum Type {
        Normal(50, 20, 500, 2, 10, "raw/enemy_normal.png", 100, 100, 4),
        Tank(100, 5, 750, 5, 5, "raw/enemy_tank.png", 70, 68, 1),
        Fast(25, 50, 150, 1, 2, "raw/enemy_fast.png", 70, 68, 0),
        Boss(1000, 10, 1000, 50, 10, "raw/enemy_boss.png", 80, 128, 10);
        public final float hp;
        public final float speed;
        public final float attackspeed;
        public final int points;
        public final int damage;
        public final String file;
        public final int width;
        public final int height;
        public final int firstWave;

        Type(float hp, float speed, float attackspeed, int points, int damage, String file, int width, int height, int firstWave) {
            this.hp = hp;
            this.speed = speed;
            this.attackspeed = attackspeed;
            this.points = points;
            this.damage = damage;
            this.file = file;
            this.width = width;
            this.height = height;
            this.firstWave = firstWave;
        }

        public static Type getRandom(int wavecount) {
            Type plannedType;
            do {
                plannedType = values()[MathUtils.random(0, values().length - 1)];
            } while (plannedType.firstWave > wavecount);
            return plannedType;
        }
    }
}
