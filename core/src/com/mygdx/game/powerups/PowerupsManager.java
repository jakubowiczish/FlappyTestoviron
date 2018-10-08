package com.mygdx.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Tube;
import com.mygdx.game.states.PlayState;

import java.util.ArrayList;

public class PowerupsManager {
    private final ArrayList<PowerupBuilder> builders = new ArrayList<PowerupBuilder>();
    private final ArrayList<Powerup> currentPowerups;


    final private int MAX_NEXT_POWERUP_COUNTER = 5;
    private final Texture nightCallBackground;
    private final Music nightcallMusic;
    int nextPowerupCounter = MathUtils.random(MAX_NEXT_POWERUP_COUNTER);

    private Sound[] calvinSound = new Sound[3];
    private PlayState playstate;

    public PowerupsManager() {

        final Sound rolexSound = Gdx.audio.newSound(Gdx.files.internal("powerups/rolex.ogg"));
        final Sound orangeSound1 = Gdx.audio.newSound(Gdx.files.internal("powerups/orange1.ogg"));
        nightcallMusic = Gdx.audio.newMusic(Gdx.files.internal("powerups/nightcall.ogg"));
        nightCallBackground = new Texture("powerups/nightcall.png");
        nightCallBackground.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        for(int i = 0; i < 3; i++){
            calvinSound[i] = Gdx.audio.newSound(Gdx.files.internal("powerups/calvin" + (i + 1) + ".ogg"));
        }

        builders.add(new PowerupBuilder()
                .addTexture(new Texture(Gdx.files.internal("powerups/rolex.png")), 13f)
                .addAcquireEvent(new PowerupEvent(){

                    @Override
                    public void acquired() {
                        playstate.addScore(1);
                        rolexSound.play(0.8f);
                    }
                }));
        builders.add(new PowerupBuilder()
                .addTexture(new Texture(Gdx.files.internal("powerups/orange.png")), 13f)
                .addAcquireEvent(new PowerupEvent(){

                    @Override
                    public void acquired() {
                        playstate.addScore(1);
                        orangeSound1.play(0.8f);
                    }
                }));
        builders.add(new PowerupBuilder()
                .addTexture(new Texture(Gdx.files.internal("powerups/calvin.png")), 13f)
                .addAcquireEvent(new PowerupEvent(){

                    @Override
                    public void acquired() {
                        playstate.addScore(1);
                        calvinSound[MathUtils.random(0, calvinSound.length - 1)].play(1f);
                    }
                }));
        builders.add(new PowerupBuilder()
                .addTexture(new Texture(Gdx.files.internal("powerups/calvin.png")), 25f)
                .addAcquireEvent(new PowerupEvent(){

                    @Override
                    public void acquired() {
                        playstate.pauseDefaultMusic(true);
                        playstate.setBackground(nightCallBackground);
                        playstate.setGameSpeed(100f);
                        playstate.addScore(1);
                        nightcallMusic.play();
                        nightcallMusic.setOnCompletionListener(new Music.OnCompletionListener() {
                            @Override
                            public void onCompletion(Music music) {
                                playstate.pauseDefaultMusic(false);
                                playstate.setGameSpeed(0f);
                                playstate.setBackground(null);

                            }
                        });
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
        float x = MathUtils.random(minX, maxX - builder.getWidth());
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
            addNewPowerup(tube.getPosBotTube().x,
                    tube.getPosBotTube().x + tube.getTopTube().getWidth(),
                    tube.getPosBotTube().y + tube.getBottomTube().getHeight(),
                    tube.getPosTopTube().y);
        }

    }

    public void clear() {
        currentPowerups.clear();
    }

    public void setPlayState(PlayState playstate) {
        this.playstate = playstate;
    }

    public void onDie() {
        nightcallMusic.stop();
        playstate.setBackground(null);

    }
}
