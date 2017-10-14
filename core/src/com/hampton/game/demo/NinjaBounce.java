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
    private final float MAX_MOVE = 10;
    private Actor ball;
    private Actor [] bricks = new Actor[20];
    private Actor bar;
    private Sound popSound;
    private Sound dub;
    private Actor cat;


    @Override
    public void initialize() {

    }

    @Override
    public void createActors() {
        backgroundColor = new Color(1, 1, 1, 1);

        cat = ActorUtils.createActorFromImage("cat.png");
        //cat.setSize(cat.getWidth()*2, cat.getHeight()*2);
        //cat.setPosition(stage.getViewport().getScreenHeight() * 2, cat.getWidth() * 2);
        stage.addActor(cat);
        cat.toBack();

        ball = ActorUtils.createActorFromImage("ball.png");
        ball.setSize(ball.getWidth()/3, ball.getHeight()/3);
        ball.setPosition(
                stage.getViewport().getScreenWidth()/2 - ball.getWidth()/2,
                stage.getViewport().getScreenHeight()/2 - ball.getHeight()/2);
        stage.addActor(ball);

        for(int i = 0; i < bricks.length; i++) {
            bricks[i] = ActorUtils.createActorFromImage("brick.png");
            bricks[i].setSize(bricks[i].getWidth(), bricks[i].getHeight());
            bricks[i].setPosition(
                    (bricks[i].getWidth() + 1) * i,
                    stage.getViewport().getScreenHeight() - bricks[i].getHeight());
            stage.addActor(bricks[i]);
        }

        bar = ActorUtils.createActorFromImage("bar.png");
        bar.setSize(bar.getWidth()/3, bar.getHeight()/3);
        bar.setPosition(
                stage.getViewport().getScreenWidth()/2 - bar.getWidth()/2,
                20);
        stage.addActor(bar);

        popSound = Gdx.audio.newSound(Gdx.files.internal("pop.wav"));

        dub = Gdx.audio.newSound(Gdx.files.internal("dub.mp3"));
            dub.play();
    }



    @Override
    public void setInputForActors() {
        ball.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Stop any other actions
                ball.clearActions();
                xMove = MathUtils.random(MAX_MOVE) - MAX_MOVE/2;
                yMove = MathUtils.random(MAX_MOVE) - MAX_MOVE/2;
                ball.addAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        if (ball.getX() + xMove < 0) {
                            xMove = -xMove;
                        }
                        if (ball.getX() + ball.getWidth() + xMove > stage.getViewport().getScreenWidth()) {
                            xMove = -xMove;
                        }
                        if (ball.getY() + yMove < 0) {
                            ball.clearActions();
                        }
                        if (ball.getY() + ball.getHeight() + yMove > stage.getViewport().getScreenHeight()) {
                            yMove = -yMove;
                        }
                        ball.moveBy(xMove, yMove);
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

        if (ActorUtils.actorsCollided(bar,ball)){
            yMove = Math.abs(yMove);
        }
        for(int i = 0; i < bricks.length; i++){
            if (ActorUtils.actorsCollided(bricks[i], ball)){
                popSound.play();
                bricks[i].remove();
                yMove = -Math.abs(yMove);
            }
        }
    }
}
