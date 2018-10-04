package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Bird {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 200;
    private final TextureRegion birdRegion;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;

    private Texture texture;
    private Texture bird;

    private Sound[] flap = new Sound[5];
    float birdWidth = 30f;
    float birdHeight;

    public Bird(int x, int y) {
        position = new Vector3(x, y, 0); // not using z
        velocity = new Vector3(0, 0, 0); // not moving yet
        texture = new Texture("birdanimation.png");
        bird = new Texture("testo.png");
        birdRegion = new TextureRegion(bird);
        birdHeight = getTexture().getRegionHeight() / (float) getTexture().getRegionWidth() * birdWidth;
        bounds = new Rectangle(x, y, birdWidth/2f, birdHeight);
        for(int i = 0; i < 5; i++){
            flap[i] = Gdx.audio.newSound(Gdx.files.internal("testo"+ (i + 1) + ".ogg"));
        }
    }

    public void update(float dt) {
        if (position.y > 0)
            velocity.add(0, GRAVITY, 0);

        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);

        if (position.y < 0)
            position.y = 0;

        velocity.scl(1 / dt);

        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return birdRegion;
    }

    public void jump() {
        velocity.y = 250;
        flap[MathUtils.random(0, flap.length - 1)].play(0.5f);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
        for (Sound sound : flap) {
            sound.dispose();
        }
    }

    public void draw(SpriteBatch sb) {
        sb.draw(getTexture(), getPosition().x, getPosition().y, birdWidth, birdHeight);
    }
}
