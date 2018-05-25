package com.fourmen.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fourmen.Actors.Player;

public class CameraStyles {
    public static void lockOnTarget(Camera camera, Player player){
        Vector3 position = camera.position;
        //position.x = camera.position.x + (player.getX() * PPM - camera.position.x) * .1f;
        //position.y = camera.position.y + (player.getY() * PPM - camera.position.y) * .1f;
        //position.x = player.getX();
        //position.y = player.getY();
        position.x = camera.position.x + (player.getX() - camera.position.x) * .1f;
        position.y = camera.position.y + (player.getY() - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }
}
