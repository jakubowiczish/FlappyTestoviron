package com.mygdx.game.powerups;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class PowerupBuilder {
    private Texture texture;
    private float width;
    private float height;
    private PowerupEvent powerupEvent;

    public PowerupBuilder addTexture(Texture tex, float width) {
        this.texture = tex;
        this.width = width;
        this.height = width * (tex.getHeight() / (float) tex.getWidth());
        return this;
    }


    public Powerup create(float x, float y) {
        Powerup powerup = new Powerup(texture, x, y, width, height);
        powerup.setAcquireEvent(powerupEvent);
        return powerup;
    }


    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public PowerupBuilder addAcquireEvent(PowerupEvent powerupEvent) {
        this.powerupEvent = powerupEvent;
        return this;
    }
}
