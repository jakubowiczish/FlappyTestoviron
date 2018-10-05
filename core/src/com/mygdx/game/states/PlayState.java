package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Flappy;
import com.mygdx.game.powerups.PowerupsManager;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Tube;

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -60;
    private static final int FIRST_TUBE_SPACING = 100;
    private final PowerupsManager powerupsManager;

    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;


    private Array<Tube> tubes;
    private int score;


    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 200);
        cam.setToOrtho(false, Flappy.WIDTH / 2, Flappy.HEIGHT / 2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

        powerupsManager = gsm.createPowerupsManager();
        powerupsManager.clear();

        tubes = new Array<Tube>();

        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(FIRST_TUBE_SPACING + i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
            powerupsManager.newTubeAppeared(tubes.get(tubes.size-1));
        }
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            bird.jump();
    }

    @Override
    public void update(float dt) {

        handleInput();
        updateGround();

        Vector3 prevPosition = bird.getPosition().cpy();
        bird.update(dt);
        powerupsManager.update(cam, bird);

        for (Tube tube : tubes) {
            float tubePositionX = tube.getPosBotTube().x + tube.getBottomTube().getWidth() / 2f;

            if (prevPosition.x + bird.getTexture().getRegionWidth() / 2f < tubePositionX
                    && bird.getPosition().x + bird.getTexture().getRegionWidth() / 2f > tubePositionX)
                score++;
        }

        cam.position.x = bird.getPosition().x + 80;

        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);

            if (cam.position.x - (cam.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT);
                powerupsManager.newTubeAppeared(tube);

            }

            if (tube.collides(bird.getBounds())) {
                gsm.set(new MenuState(gsm, score));
            }
        }

        if (bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET)
            gsm.set(new MenuState(gsm , score));

        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        bird.draw(sb);

        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);

        powerupsManager.draw(cam, sb);

        sb.end();

        sb.setProjectionMatrix(gsm.getProjectionMatrix());
        sb.begin();
        sb.flush();
        float padding = Gdx.graphics.getWidth()*0.01f;
        gsm.getFont().draw(sb, "Score: " + score, padding,Gdx.graphics.getHeight() - gsm.getFont().getAscent() - padding);
        sb.flush();
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();

        for (Tube tube : tubes)
            tube.dispose();
    }

    private void updateGround() {
        if (cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if (cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
    }
}
