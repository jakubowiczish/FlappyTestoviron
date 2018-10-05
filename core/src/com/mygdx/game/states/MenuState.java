package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Flappy;

public class MenuState extends State {

    private int bestScore;
    private int score;
    private Texture background;
    private Texture playBtn;
    private boolean showScore;
    GlyphLayout glyphLayout = new GlyphLayout();
    private Music menuSong;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, Flappy.WIDTH / 2, Flappy.HEIGHT / 2);
        background = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");
        menuSong = Gdx.audio.newMusic(Gdx.files.internal("maintesto.mp3"));
        menuSong.play();

    }

    public MenuState(GameStateManager gsm, int score) {
        this(gsm);
        this.score = score;
        this.showScore = true;
        this.bestScore = gsm.getPrefs().getInteger("best_score", 0);
        if(score > bestScore) {
            gsm.getPrefs().putInteger("best_score", score);
            gsm.getPrefs().flush();
            bestScore = score;
        }
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0);
        sb.draw(playBtn, cam.position.x - playBtn.getWidth() / 2, cam.position.y);
        sb.end();

        if (showScore) {
            sb.setProjectionMatrix(gsm.getProjectionMatrix());
            sb.begin();
            sb.flush();
            String text = "Score: " + score;
            glyphLayout.setText(gsm.getFont(), text);
            gsm.getFont().draw(sb, text, Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                    Gdx.graphics.getHeight() - gsm.getFont().getAscent());

            String text2 = "Best score: " + bestScore;
            glyphLayout.setText(gsm.getFont(), text2);
            gsm.getFont().draw(sb, text2, Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                    Gdx.graphics.getHeight() - gsm.getFont().getAscent() - gsm.getFont().getLineHeight());
            sb.flush();
            sb.end();
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        menuSong.dispose();
    }
}
