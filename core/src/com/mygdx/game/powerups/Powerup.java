package com.mygdx.game.powerups;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.sprites.Bird;

public class Powerup {
    private final Sprite sprite;
    private final Rectangle bounds;
    private boolean toBeRemoved;
    private PowerupEvent acquireEvent;

    public Powerup(Texture texture, float x, float y, float width, float height) {
        this.sprite = new Sprite(texture);
        sprite.setBounds(x, y, width, height);
        bounds = new Rectangle(x, y, width, height);

    }


    public boolean isToBeRemoved() {
        return toBeRemoved;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void update(OrthographicCamera camera, Bird bird) {
        if(getBounds().overlaps(bird.getBounds())
                || bird.getBounds().overlaps(getBounds())) {
            toBeRemoved = true;
            if(acquireEvent != null) acquireEvent.acquired();
        }
        if (camera.position.x - camera.viewportWidth / 2f > sprite.getX() + sprite.getWidth())
            toBeRemoved = true;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setAcquireEvent(PowerupEvent acquireEvent) {
        this.acquireEvent = acquireEvent;
    }
}
