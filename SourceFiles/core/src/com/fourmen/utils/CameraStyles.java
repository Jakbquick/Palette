package com.fourmen.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.fourmen.actors.Box2DPlayer;
import com.fourmen.actors.Enemy;
import com.fourmen.actors.Player;
import com.fourmen.actors.Box2DPlayer;
import com.fourmen.actors.Enemy;
import com.fourmen.actors.Player;

public class CameraStyles {
    public static void lockOnTarget(Camera camera, Player player){
        Vector3 position = camera.position;
        //position.x = camera.position.x + (player.getX() * PPM - camera.position.x) * .1f;
        //position.y = camera.position.y + (player.getY() * PPM - camera.position.y) * .1f;
        //position.x = player.getX();
        //position.y = player.getY();
        position.x = camera.position.x + (player.getX() + (player.getPlayerWidth() / 2f) - camera.position.x) * .1f;
        position.y = camera.position.y + (player.getY() + (player.getPlayerHeight() / 2f) - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }
    public static void lockOnTarget(Camera camera, Box2DPlayer player){
        Vector3 position = camera.position;
        //position.x = camera.position.x + (player.getX() * PPM - camera.position.x) * .1f;
        //position.y = camera.position.y + (player.getY() * PPM - camera.position.y) * .1f;
        //position.x = player.getX();
        //position.y = player.getY();
        //position.x = camera.position.x + (player.getX() + (player.getPlayerWidth() / 2f) - camera.position.x) * .1f;
        //position.y = camera.position.y + (player.getY() + (player.getPlayerHeight() / 2f) - camera.position.y) * .1f;
        //position.x = player.getX();
        //position.y = player.getY();
        position.x = camera.position.x + (player.getX() - camera.position.x) * .1f;
        position.y = camera.position.y + (player.getY() - camera.position.y) * .1f;
        camera.position.set(position);
        camera.update();
    }
    public static void boundary(Camera camera, float startX, float startY, float width, float height){
        Vector3 position = camera.position;

        if(position.x < startX){
            position.x = startX;
        }
        if(position.y < startY){
            position.y = startY;
        }
        if(position.x > startX + width){
            position.x = startX + width;
        }
        if(position.y > startY + height){
            position.y = startY + height;
        }

        camera.position.set(position);
        camera.update();
    }

    public static void lockAverageBetweenTargets(Camera camera, Player player, Enemy enemy){
        float playerXPosition = (player.getX() + (player.getPlayerWidth()/2f));
        float playerYPosition = (player.getY() + (player.getPlayerHeight()/2f));
        float enemyXPosition = (enemy.getX() + (enemy.getEnemyWidth())/2f);
        float enemyYPosition = (enemy.getY() + (enemy.getEnemyHeight())/2f);

        float avgX = (playerXPosition + enemyXPosition) / 2f;
        float avgY = (playerYPosition + enemyYPosition) / 2f;

        Vector3 position = camera.position;
        position.x = camera.position.x + (avgX - camera.position.x) * .1f;
        position.y = camera.position.y + (avgY - camera.position.y) * .1f;
        camera.position.set(position);

        camera.update();
    }
}
