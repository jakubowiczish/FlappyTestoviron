package com.mygdx.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Tube;

import java.util.ArrayList;

public class PowerupsManager {
    private final ArrayList<PowerupBuilder> builders = new ArrayList<PowerupBuilder>();
    private final ArrayList<Powerup> currentPowerups;


    final private int MAX_NEXT_POWERUP_COUNTER = 5;
    int nextPowerupCounter = MathUtils.random(MAX_NEXT_POWERUP_COUNTER);

    private Sound[] calvinSound = new Sound[3];

    public PowerupsManager() {

        final Sound rolexSound = Gdx.audio.newSound(Gdx.files.internal("powerups/rolex.ogg"));
        final Sound orangeSound1 = Gdx.audio.newSound(Gdx.files.internal("powerups/orange1.ogg"));

        for(int i = 0; i < 3; i++){
            calvinSound[i] = Gdx.audio.newSound(Gdx.files.internal("powerups/calvin" + (i + 1) + ".ogg"));
        }

        builders.add(new PowerupBuilder()
                .addTexture(new Texture(Gdx.files.internal("powerups/rolex.png")), 13f)
                .addAcquireEvent(new PowerupEvent(){

                    @Override
                    public void acquired() {
                        rolexSound.play(0.8f);
                    }
                }));
        builders.add(new PowerupBuilder()
                .addTexture(new Texture(Gdx.files.internal("powerups/orange.png")), 13f)
                .addAcquireEvent(new PowerupEvent(){

                    @Override
                    public void acquired() {
                        orangeSound1.play(0.8f);
                    }
                }));
        builders.add(new PowerupBuilder()
                .addTexture(new Texture(Gdx.files.internal("powerups/calvin.png")), 13f)
                .addAcquireEvent(new PowerupEvent(){

                    @Override
                    public void acquired() {
                        calvinSound[MathUtils.random(0, calvinSound.length - 1)].play(1f);
                    }
                }));
        currentPowerups = new ArrayList<Powerup>();
    }

    public void draw(OrthographicCamera camera, SpriteBatch batch) {
        for (int i = 0; i < currentPowerups.size(); i++) {
            if (currentPowerups.get(i).isToBeRemoved())
                currentPowerups.remove(i--);
            else
                currentPowerups.get(i).draw(batch);

        }
    }

    public void addNewPowerup(float minX, float maxX, float minY, float maxY) {
        PowerupBuilder builder = builders.get(MathUtils.random(0, builders.size()-1));
        float x = MathUtils.random(minX, maxX);
        float y = MathUtils.random(minY, maxY - builder.getHeight());

        currentPowerups.add(builder.create(x, y));

    }

    public void update(OrthographicCamera camera, Bird bird) {
        for (int i = 0; i < currentPowerups.size(); i++) {
            currentPowerups.get(i).update(camera, bird);

        }
    }

    public void newTubeAppeared(Tube tube) {
        nextPowerupCounter--;
        if(nextPowerupCounter < 0) {
            nextPowerupCounter = MathUtils.random(MAX_NEXT_POWERUP_COUNTER);
            float X_PADDING = 5f;

            addNewPowerup(tube.getPosBotTube().x - X_PADDING,
                    tube.getPosBotTube().x + X_PADDING,
                    tube.getPosBotTube().y + tube.getBottomTube().getHeight(),
                    tube.getPosTopTube().y);
        }

    }

    public void clear() {
        currentPowerups.clear();
    }
}
