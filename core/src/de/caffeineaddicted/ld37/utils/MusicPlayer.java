package de.caffeineaddicted.ld37.utils;

import com.badlogic.gdx.audio.Music;
import de.caffeineaddicted.sgl.SGL;
import de.caffeineaddicted.sgl.impl.exceptions.ProvidedObjectIsNullException;
import de.caffeineaddicted.sgl.utils.MathUtils;
import de.caffeineaddicted.sgl.utils.SGLAssets;

import java.util.ArrayList;

/**
 * @author Malte Heinzelmann
 */
public class MusicPlayer implements Music, Music.OnCompletionListener {

    private ArrayList<Music> list;
    private boolean looping, shuffle, playing = false;
    private float volume = 1, pan = 0;
    private OnCompletionListener onCompletionListener;
    private int current = -1;

    public MusicPlayer() {
        list = new ArrayList<Music>();
    }

    public void add(Music music) {
        if (music == null) {
            return;
        }
        music.setOnCompletionListener(this);
        list.add(music);
        if (current < 0) {
            current = 0;
        }
    }

    public void add(String file) {
        try {
            add(SGL.provide(SGLAssets.class).get(file, Music.class));
        } catch (ProvidedObjectIsNullException poine) {
            SGL.debug("Can't load music from AssetManager because the AssetManager is no loaded!");
        }
    }

    private void setCurrent(int current) {
        current().stop();
        this.current = current;
        configChanged();
    }

    private void configChanged() {
        if (isPlaying()) {
            SGL.debug("Playing song " + current);
            current().play();
        } else {
            current().pause();
        }
        current().setLooping(false);
        current().setPan(pan, volume);
    }

    private boolean validIndex() {
        return validIndex(current);
    }

    private boolean validIndex(int index) {
        return (index >= 0 && index < list.size());
    }

    private Music current() {
        return list.get(current);
    }

    @Override
    public void play() {
        playing = true;
        configChanged();
    }

    @Override
    public void pause() {
        playing = false;
        configChanged();
    }

    @Override
    public void stop() {
        playing = false;
        current().stop();
        configChanged();
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    public void next() {
        if (isShuffle()) {
            setCurrent(MathUtils.random(0, list.size() - 1));
        } else {
            setCurrent((current + 1) % list.size());
        }
    }

    public void prev() {
        if (isShuffle()) {
            next(); // Is the same as next()
        } else {
            setCurrent(current = (list.size() + current - 1) % list.size());
        }
    }

    @Override
    public boolean isLooping() {
        return looping;
    }

    @Override
    public void setLooping(boolean isLooping) {
        looping = isLooping;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean isShuffle) {
        this.shuffle = isShuffle;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public void setVolume(float volume) {
        this.volume = volume;
        configChanged();
    }

    @Override
    public void setPan(float pan, float volume) {
        this.pan = pan;
        this.volume = volume;
        configChanged();
    }

    @Override
    public float getPosition() {
        return current().getPosition();
    }

    @Override
    public void setPosition(float position) {
        current().setPosition(position);
    }

    @Override
    public void dispose() {
        for (Music m : list) {
            m.dispose();
        }
        list.clear();
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        onCompletionListener = listener;
    }

    @Override
    public void onCompletion(Music music) {
        SGL.debug("completed music");
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(music);
        }
        if (isLooping() || isShuffle()) {
            next();
        }
    }
}
