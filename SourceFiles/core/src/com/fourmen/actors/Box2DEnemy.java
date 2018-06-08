package com.fourmen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.fourmen.utils.Animator;

import javax.swing.*;


public class Box2DEnemy extends Entity {
    //constants
    private final static float ENEMY_SIZE = 1f;
    private final static float BOX2D_SCALE = .5f;
    private final static float ENEMY_WIDTH = 30 * ENEMY_SIZE;
    private final static float ENEMY_HEIGHT = 30 * ENEMY_SIZE;
    private static final float ACCELERATION_CONSTANT = 1f;
    private static final float DECELERATION_CONSTANT = 1f;
    private final int MAX_SPEED = 600;               // the max speed a player can move
    private final float DASH_SPEED = 800;
    private final float CHARGE_SPEED = 2000;
    private final static float DASH_COOLDOWN = .6f;
    private final static float DASH_DURATION = .1f;
    private enum PlayerState {
        MOVING, DASHING, CHARGING, ENDCHARGE, DYING
    }

    //instance variables
    private World world;

    private Vector2 direction;          // contains a x direction and y direction from -1 to 1
    private Vector2 dashDirection;
    private Vector2 targetSpeed;        // contains a target speed for x and y
    private Vector2 currentSpeed;       // how fast the player is currently going in x and y
    private Vector2 chargePos;
    private float acceleration;
    private float deceleration;
    private float prevX = 0;
    private float prevY = 0;

    private double dashDurationTimer = 0;
    private double dashCooldownTimer = 0;
    private float stateTime = 0;

    private int damage;

    private boolean dead;

    public boolean end;

    public TextureRegion currentFrame;
    private Animation<TextureRegion> moving;
    private Animation<TextureRegion> dash;
    private Animation<TextureRegion> charge;
    private Animation<TextureRegion> dying;

    private int lastDirectionfaced;
    private int LEFT = 0;
    private int RIGHT = 1;

    private PlayerState enemyState;
    private Box2DPlayer player;
    private Body body;
    private boolean towardsPlayer;

    //constructors
    public Box2DEnemy(World world, PlayerBounds myPlayerBounds, Box2DPlayer myPlayer) {
        super(1000, myPlayerBounds, ENEMY_WIDTH, ENEMY_HEIGHT, new Vector2(1500, 900));
        this.world = world;
        lastDirectionfaced = LEFT;
        direction = new Vector2(0, 0);
        dashDirection = new Vector2(0, 0);
        targetSpeed = new Vector2(0, 0);
        currentSpeed = new Vector2(0, 0);
        chargePos = new Vector2(0,0);
        acceleration = ACCELERATION_CONSTANT * MAX_SPEED;
        deceleration = DECELERATION_CONSTANT * MAX_SPEED;
        towardsPlayer = false;
        enemyState = enemyState.MOVING;
        player = myPlayer;
        dead = false;
        end = false;

        moving = new Animation<TextureRegion>(0.06f, Animator.setUpSpriteSheet("Images/EnemyMove.png", 1, 18));
        dash = new Animation<TextureRegion>(0.05f, Animator.setUpSpriteSheet("Images/EnemyMove.png", 1, 18));
        charge = new Animation<TextureRegion>(0.05f, Animator.setUpSpriteSheet("Images/EnemyAttack.png", 1, 7));
        dying = new Animation<TextureRegion>(0.05f, Animator.setUpSpriteSheet("Images/bossdeathsheet.png", 1, 20));
        currentFrame = moving.getKeyFrame(stateTime, true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(position));
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        PolygonShape bodySquare = new PolygonShape();
        bodySquare.setAsBox(150 * ENEMY_SIZE * BOX2D_SCALE, 150 * ENEMY_SIZE * BOX2D_SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodySquare;

        body.createFixture(fixtureDef);

        bodySquare.dispose();

        body.setUserData(this);
    }

    //methods
    public void act() {
        updateDirection();
        switch (enemyState) {
            case MOVING:
                currentFrame = getFrame(moving);
                move();
                if (dashCooldownTimer <= 0)
                    checkDash();
                checkCharge();

                break;
            case DASHING:
                currentFrame = getFrame(dash);
                dash();
                if (dashDurationTimer <= 0) {
                    enemyState = enemyState.MOVING;
                }
                break;
            case CHARGING:
                currentFrame = getFrame(charge);
                charge();
                if (position.equals(player.position) || dashDurationTimer <= -0.1) {
                    enemyState = enemyState.MOVING;
                }
            case DYING:
                currentFrame = dying.getKeyFrame(stateTime);
                if (dying.getKeyFrameIndex(stateTime) == 19) {
                    end = true;
                }
                break;
        }
        blockLeavingTheWorld();
        updateHealth();
        updatePosition();
    }

    private void updateDirection() {
        direction.x = 0;
        direction.y = 0;

        int randomFactor = MathUtils.random(2);
        if(randomFactor == 0) {
            if (distanceBetween(player, getX(), getY()) > 1000)
                generateDirection();
            else generateNegDirection();
        }
        else if(randomFactor == 1) {
            if (distanceBetween(player, getX(), getY()) > 400)
                generateDirection();
            else generateNegDirection();
        }
        else if(randomFactor == 2) {
            if (distanceBetween(player, getX(), getY()) > 200)
                generateDirection();
            else generateNegDirection();
        }

            if (Gdx.input.isKeyPressed(Input.Keys.H)) {
                position.set(0, 0);
            }
            direction.nor();

            targetSpeed.x = (int) (direction.x * MAX_SPEED);
            targetSpeed.y = (int) (direction.y * MAX_SPEED);

        }

    private void move(){
        Vector2 curDir = new Vector2(Math.signum(targetSpeed.x - currentSpeed.x), Math.signum(targetSpeed.y - currentSpeed.y));

        if(targetSpeed.x == 0) {                                            // checks want to be stopped on the x axis
            currentSpeed.x += deceleration * curDir.x;                      // decelerates the player's x movement speed
        }
        else {
            currentSpeed.x += acceleration * curDir.x;                      // accelerates the player's x movement speed
        }

        if(targetSpeed.y == 0) {                                            // checks want to be stopped on the y axis
            currentSpeed.y += deceleration * curDir.y;                      // decelerates the player's x movement speed
        }
        else {
            currentSpeed.y += acceleration * curDir.y;                      // accelerates the player's x movement speed
        }

        if(Math.signum(targetSpeed.x - currentSpeed.x) != curDir.x) {       // checks if the player passes the x targetSpeed
            currentSpeed.x = targetSpeed.x;
        }
        if(Math.signum(targetSpeed.y - currentSpeed.y) != curDir.y) {       // checks if the player passes the y targetSpeed
            currentSpeed.y = targetSpeed.y;
        }

        setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());        // changes the x position of the entity
        setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());        // changes the y position of the entity
    }

    private void checkDash() {

        int dashChance  = MathUtils.random(4);

        if(dashChance == 0) {
            enemyState = enemyState.DASHING;
            dashDirection.x = direction.x;
            dashDirection.y = direction.y;

            currentSpeed = new Vector2(0,0);
            dashDurationTimer = DASH_DURATION;
            dashCooldownTimer = DASH_COOLDOWN;
        }
    }

    private void checkCharge() {

        int chargeChance  = MathUtils.random(50);

        if(chargeChance == 0 && distanceBetween(player, getX(), getY()) <= 600 && distanceBetween(player, getX(), getY()) >= 400) {
            enemyState = enemyState.CHARGING;
            generateChargeDirection();
            dashDirection.x = direction.x;
            dashDirection.y = direction.y;

            currentSpeed = new Vector2(0,0);
            dashDurationTimer = DASH_DURATION;
            dashCooldownTimer = DASH_COOLDOWN;
        }
    }

    private void dash() {
        currentSpeed.x = DASH_SPEED * dashDirection.x;
        currentSpeed.y = DASH_SPEED * dashDirection.y;

        setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());
        setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());
    }

    private void charge() {
        currentSpeed.x = CHARGE_SPEED * dashDirection.x;
        currentSpeed.y = CHARGE_SPEED * dashDirection.y;

        setX(getX() + currentSpeed.x * Gdx.graphics.getDeltaTime());
        setY(getY() + currentSpeed.y * Gdx.graphics.getDeltaTime());
    }

    public void generateChargeDirection() {
        direction.x = 0;
        direction.y = 0;

        if(getX() >= player.getX())
            direction.x--;
        else direction.x++;

        if(getY() >= player.getY())
            direction.y--;
        else direction.y++;
    }

    public void generateDirection() {
        int dirVal = MathUtils.random(2);
        // 1 if boss is at (-1,1) relative to player
        if (getX() < player.getX() && getY() > player.getY()) {
            if (dirVal == 0) {
                direction.x++;
            } else if(dirVal == 1) {
                direction.x++;
                direction.y--;
            } else if (dirVal == 2)
                direction.y--;
        }
        // 2 if boss is at (0,1) relative to player
        else if (getX() == player.getX() && getY() > player.getY()) {
            if (dirVal == 0) {
                direction.x++;
                direction.y--;
            } else if (dirVal == 1) {
                direction.y--;
            } else if (dirVal == 2) {
                direction.x--;
                direction.y--;
            }
        }
        // 3 if boss is at (1,1) relative to player
        else if (getX() > player.getX() && getY() > player.getY()) {
            if (dirVal == 0) {
                direction.y--;
            } else if(dirVal == 1) {
                direction.x--;
                direction.y--;
            } else if (dirVal == 2) {
                direction.x--;
            }
        }
        // 4 if boss is at (1,0) relative to player
        else if (getX() > player.getX() && getY() == player.getY()) {
            if (dirVal == 0) {
                direction.x--;
                direction.y--;
            } else if(dirVal == 1) {
                direction.x--;
            } else if (dirVal == 2) {
                direction.x--;
                direction.y++;
            }
        }
        // 5 if boss is at (1,-1) relative to player
        else if (getX() > player.getX() && getY() < player.getY()) {
            if (dirVal == 0) {
                direction.x--;
            } else if (dirVal == 1) {
                direction.x--;
                direction.y++;
            } else if (dirVal == 2) {
                direction.y++;
            }
        }
        // 6 if boss is at (0,-1) relative to player
        else if (getX() == player.getX() && getY() < player.getY()) {
            if (dirVal == 0) {
                direction.x--;
                direction.y++;
            } else if (dirVal == 1) {
                direction.y++;
            } else if (dirVal == 2) {
                direction.x++;
                direction.y++;
            }
        }
        // 7 if boss is at (-1,-1) relative to player
        else if (getX() < player.getX() && getY() < player.getY()) {
            if (dirVal == 0) {
                direction.y++;
            } else if (dirVal == 1) {
                direction.x++;
                direction.y++;
            } else if (dirVal == 2) {
                direction.x++;
            }
        }
        // 8 if boss is at (-1,0) relative to player
        else if (getX() < player.getX() && getY() == player.getY()) {
            if (dirVal == 0) {
                direction.x++;
                direction.y++;
            } else if (dirVal == 1) {
                direction.x++;
            } else if (dirVal == 2) {
                direction.x++;
                direction.y--;
            }
        }

        towardsPlayer = true;
    }

    public void generateNegDirection() {
        int dirVal = MathUtils.random(1);
        // 1 if boss is at (-1,1) relative to player
        if (getX() < player.getX() && getY() > player.getY()) {
            if (dirVal == 0)
                direction.x--;
            else if (dirVal == 1)
                direction.y++;
        }
        // 2 if boss is at (0,1) relative to player
        else if (getX() == player.getX() && getY() > player.getY()) {
            if (dirVal == 0) {
                direction.x--;
                direction.y++;
            } else if (dirVal == 1) {
                direction.x++;
                direction.y++;
            }
        }
        // 3 if boss is at (1,1) relative to player
        else if (getX() > player.getX() && getY() > player.getY()) {
            if (dirVal == 0) {
                direction.y++;
            } else if (dirVal == 1) {
                direction.x++;
            }
        }
        // 4 if boss is at (1,0) relative to player
        else if (getX() > player.getX() && getY() == player.getY()) {
            if (dirVal == 0) {
                direction.x++;
                direction.y++;
            } else if (dirVal == 1) {
                direction.x++;
                direction.y--;
            }
        }
        // 5 if boss is at (1,-1) relative to player
        else if (getX() > player.getX() && getY() < player.getY()) {
            if (dirVal == 0) {
                direction.x++;
            } else if (dirVal == 1) {
                direction.y--;
            }
        }
        // 6 if boss is at (0,-1) relative to player
        else if (getX() == player.getX() && getY() < player.getY()) {
            if (dirVal == 0) {
                direction.x++;
                direction.y--;
            } else if (dirVal == 1) {
                direction.x--;
                direction.y--;
            }
        }
        // 7 if boss is at (-1,-1) relative to player
        else if (getX() < player.getX() && getY() < player.getY()) {
            if (dirVal == 0) {
                direction.y--;
            } else if (dirVal == 1) {
                direction.x--;
            }
        }
        // 8 if boss is at (-1,0) relative to player
        else if (getX() < player.getX() && getY() == player.getY()) {
            if (dirVal == 0) {
                direction.x--;
                direction.y--;
            } else if (dirVal == 1) {
                direction.x--;
                direction.y++;
            }
        }

        towardsPlayer = false;
    }

    private double distanceBetween(Box2DPlayer player1, float xVal, float yVal){
        return Math.sqrt(Math.pow((xVal - player1.getX()), 2) + Math.pow((yVal - player1.getY()), 2));
    }

    public void draw(Batch batch) {
        boolean flip = (lastDirectionfaced == RIGHT);
        batch.draw(currentFrame, flip ? getX() - (16f * ENEMY_SIZE) - (141.5f * ENEMY_SIZE)+currentFrame.getRegionWidth()* ENEMY_SIZE : getX() - (141.5f * ENEMY_SIZE),
                getY() - 146 * ENEMY_SIZE, flip ? -currentFrame.getRegionWidth()* ENEMY_SIZE: currentFrame.getRegionWidth()* ENEMY_SIZE,
                currentFrame.getRegionHeight()* ENEMY_SIZE);
    }

    public TextureRegion getFrame(Animation<TextureRegion> frame) {
        if(frame.isAnimationFinished(stateTime)) {
            stateTime = 0;
        }
        return frame.getKeyFrame(stateTime, true);
    }

    protected void updateHealth() {
        if (fixtureCollisions > 0 && !invincible && invTimer <= 0) {
            health -= 10 * damage;
            invTimer = INV_COOLDOWN / 3;
        }
        if (health <= 0 && !dead) {
            enemyState = enemyState.DYING;
            dead = true;
            stateTime = 0;
        }
    }

    public void updateDamage(int hitValue) {
        damage += hitValue;
    }

    public float getEnemyWidth(){
        return ENEMY_WIDTH;
    }

    public float getEnemyHeight(){
        return ENEMY_HEIGHT;
    }

    public Body getPlayerBody(){
        return body;
    }

    public boolean isDead() {
        return dead;
    }

    public void updatePosition() {
        body.setTransform(position, 0);
    }

    public void update(float delta) {
        super.update(delta);
        dashDurationTimer -= delta;
        dashCooldownTimer -= delta;
        stateTime += delta;
    }

    public void dispose() {

    }
}