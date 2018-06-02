package com.fourmen.box2D;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;


public class BoxWorld {
    public static World world;
    public BoxWorld(){
        Box2D.init();
        world = new World(new Vector2(0,0),true);
        Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    }
    private float accumulator = 0;


}
