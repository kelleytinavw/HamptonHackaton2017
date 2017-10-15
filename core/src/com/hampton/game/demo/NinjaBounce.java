package com.hampton.game.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.hampton.game.GameScreen;
import com.hampton.game.utils.ActorUtils;

/**
 * Created by turnerd on 10/13/17.
 */
public class NinjaBounce extends GameScreen {

    private float xMove;
    private float yMove;
    private final float MAX_MOVE = 20;
    private Actor ball1;
    private Actor [] bricks = new Actor[300];
    private Actor bar;
    private Sound popSound;
    private Music dub;
    private Actor cat3;


    @Override
    public void initialize() {
    
    }

    @Override
    public void createActors() {
        backgroundColor = new Color(1, 1, 1, 1);

        cat3 = ActorUtils.createActorFromImage("cat3.jpg");
        cat3.setSize(stage.getViewport().getScreenWidth(),stage.getViewport().getScreenHeight());
        stage.addActor(cat3);
        cat3.toBack();

        ball1 = ActorUtils.createActorFromImage("ball1.jpg");
        ball1.setSize(ball1.getWidth(), ball1.getHeight());
        ball1.setPosition(
                stage.getViewport().getScreenWidth()/2 - ball1.getWidth()/2,
                stage.getViewport().getScreenHeight()/3 - ball1.getHeight()/2);
        stage.addActor(ball1);

        for(int i = 0; i < bricks.length; i++) {
            bricks[i] = ActorUtils.createActorFromImage("brick.png");
            bricks[i].setSize(bricks[i].getWidth() * 3, bricks[i].getHeight() * 3);
            bricks[i].setPosition(
                    (bricks[i].getWidth() + 1) * (i % 30),
                    stage.getViewport().getScreenHeight() - bricks[i].getHeight()* (i / 30));
            stage.addActor(bricks[i]);
        }

        bar = ActorUtils.createActorFromImage("bar.png");
        bar.setSize(bar.getWidth(), bar.getHeight());
        bar.setPosition(
                stage.getViewport().getScreenWidth()/2 - bar.getWidth()/2,
                60);
        stage.addActor(bar);

        popSound = Gdx.audio.newSound(Gdx.files.internal("pop.wav"));

        dub = Gdx.audio.newMusic(Gdx.files.internal("dub.mp3"));
        dub.setLooping(true);
        dub.play();
    }



    @Override
    public void setInputForActors() {
        ball1.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Stop any other actions
                ball1.clearActions();
                xMove = MathUtils.random(MAX_MOVE) - MAX_MOVE/2;
                yMove = MathUtils.random(MAX_MOVE) - MAX_MOVE/2;
                ball1.addAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        if (ball1.getX() + xMove < 0) {
                            xMove = -xMove;
                        }
                        if (ball1.getX() + ball1.getWidth() + xMove > stage.getViewport().getScreenWidth()) {
                            xMove = -xMove;
                        }
                        if (ball1.getY() + yMove < 0) {
                            ball1.clearActions();
                        }
                        if (ball1.getY() + ball1.getHeight() + yMove > stage.getViewport().getScreenHeight()) {
                            yMove = -yMove;
                        }
                        ball1.moveBy(xMove, yMove);
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public void setActionsForActors() {
    }

    @Override
    protected void calledEveryFrame() {
        if(Gdx.input.isTouched()) {
            // input.getY sets 0 as the top but actors use 0 for the bottom so we have to flip it
            Vector2 touchPoint = new Vector2(
                    Gdx.input.getX(),
                    stage.getViewport().getScreenHeight() - Gdx.input.getY());
            // Moves the bar
            bar.setPosition(touchPoint.x - bar.getWidth()/ 2, bar.getY());
        }

        if (ActorUtils.actorsCollided(bar,ball1)){
            yMove = Math.abs(yMove);
        }
        
        for(int i = 0; i < bricks.length; i++){
            if (bricks[i] != null && ActorUtils.actorsCollided(bricks[i], ball1)){
                popSound.play();
                bricks[i].remove();
                bricks [i]= null;
                yMove = -Math.abs(yMove);
            }
        }
    }
}
