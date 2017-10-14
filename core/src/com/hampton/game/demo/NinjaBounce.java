package com.hampton.game.demo;

import com.badlogic.gdx.Gdx;
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

    private Actor ninja;
    private float xMove;
    private float yMove;
    private final float MAX_MOVE = 10;


    @Override
    public void initialize() {

    }

    @Override
    public void createActors() {
        backgroundColor = new Color(1, 1, 1, 1);
        ninja = ActorUtils.createActorFromImage("ninja-2swords.png");
        ninja.setSize(ninja.getWidth()/3, ninja.getHeight()/3);
        ninja.setPosition(
                stage.getViewport().getScreenWidth()/2 - ninja.getWidth()/2,
                stage.getViewport().getScreenHeight()/2 - ninja.getHeight()/2);
        stage.addActor(ninja);
    }

    @Override
    public void setInputForActors() {
        ninja.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Stop any other actions
                ninja.clearActions();
                xMove = MathUtils.random(MAX_MOVE) - MAX_MOVE/2;
                yMove = MathUtils.random(MAX_MOVE) - MAX_MOVE/2;
                ninja.addAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        if (ninja.getX() + xMove < 0) {
                            xMove = -xMove;
                        }
                        if (ninja.getX() + ninja.getWidth() + xMove > stage.getViewport().getScreenWidth()) {
                            xMove = -xMove;
                        }
                        if (ninja.getY() + yMove < 0) {
                            yMove = -yMove;
                        }
                        if (ninja.getY() + ninja.getHeight() + yMove > stage.getViewport().getScreenHeight()) {
                            yMove = -yMove;
                        }
                        ninja.moveBy(xMove, yMove);
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
            // Only move to the point if we didn't click on the ninja
            if(!ActorUtils.actorContainsPoint(ninja, touchPoint)) {
                ninja.clearActions();
                // Move to touched location in 3 seconds
                ninja.addAction(Actions.moveTo(
                        touchPoint.x - ninja.getWidth() / 2,
                        touchPoint.y - ninja.getHeight() / 2,
                        3,
                        Interpolation.circleOut));
            }
        }
    }
}
