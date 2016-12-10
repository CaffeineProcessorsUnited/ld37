package de.caffeineaddicted.ld37prep.action;


import com.badlogic.gdx.math.Vector2;
import de.caffeineaddicted.sgl.etities.Actor;

/**
 * @author Malte Heinzelmann
 */
public class MoveToAction extends com.badlogic.gdx.scenes.scene2d.actions.MoveToAction {

    Actor target;
    Updater updater;

    public float startX, startY;
    public float endX, endY;

    public interface Updater {
        void update(MoveToAction target, float percent);
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    @Override
    public void update(float percent) {
        if (updater != null) {
            updater.update(this, percent);
        } else {
            super.update(percent);
        }
    }

    public void setTarget (Actor target) {
        this.target = target;
    }

    public Actor getTarget () {
        return target;
    }

    protected void begin () {
        startX = target.getCenterPoint().x;
        startY = target.getCenterPoint().y;
    }

    public void setPosition(Vector2 end) {
        endX = end.x;
        endY = end.y;
    }
}