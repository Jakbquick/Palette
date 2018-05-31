package com.fourmen.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {

    public static TextureRegion[] setUpSpriteSheet(String internalPath, int frameRows, int frameCols) {
        Texture spriteSheet = new Texture(internalPath);
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / frameCols, spriteSheet.getHeight() / frameRows);

        TextureRegion[] spriteFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;

        for (int r = 0; r < frameRows; r++) {
            for (int c = 0; c < frameCols; c++) {
                spriteFrames[index++] = tmp[r][c];
            }
        }

        return spriteFrames;
    }
}
