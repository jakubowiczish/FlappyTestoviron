package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.game.powerups.PowerupsManager;

import java.util.Stack;

public class GameStateManager {
    private final BitmapFont font;
    private final Preferences prefs;
    private Stack<State> states;
    private Matrix4 projectionMatrix;
    private PowerupsManager powerupsManager;

    public GameStateManager(){
        powerupsManager = new PowerupsManager();
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.getData().setScale(Gdx.graphics.getWidth() / 1080f * 2f); // size of score
        prefs = Gdx.app.getPreferences("testo");

        states = new Stack<State>();
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop().dispose();
    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }

    public BitmapFont getFont() {
        return font;
    }


    public void setDefaultProjectionMatrix(Matrix4 projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public Matrix4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public PowerupsManager createPowerupsManager() {
        return powerupsManager;
    }
}
