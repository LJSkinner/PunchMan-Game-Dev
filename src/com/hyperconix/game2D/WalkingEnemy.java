package com.hyperconix.game2D;

/**
 * This class models a subclass of Sprite which
 * is meant to represent a WalkingEnemy. That is
 * an enemy that walks back and forwards on a platform.
 * 
 * This implements an enemy contract which forces
 * that every enemy sprite must have a move algorithm
 * of some kind. Enemies and other sprites could be
 * expanded this way by using inheritance. 
 * 
 * @author Luke S
 *
 */
public class WalkingEnemy extends Sprite implements Enemy {

	public WalkingEnemy(Animation anim) {
		super(anim);
	}

	
	/**
	 * This method will implement a standard walking enemy
	 * algorithm where the enemy will check the tiles in front
	 * of it to see whether or not it has to turn around.
	 */
	public void move(char airTileChar, TileMap tmap, float speed) {
        int bottomLeftX = (int) (getX() / tmap.getTileWidth());
        
        int bottomLeftY = (int) ((getY() + getHeight()) / tmap.getTileHeight());
        
        int bottomRightX = (int) ((getX() + getWidth()) / tmap.getTileWidth());
        
        int bottomRightY = (int) ((getY() + getHeight()) / tmap.getTileHeight());
        
        boolean facingRight = getScaleX() > 0;
        
        char comparingTileChar;
        
        if(facingRight) {
        	setVelocityX(speed);
        	
        	comparingTileChar = tmap.getTileChar(bottomRightX + 1, bottomRightY);
        } else {
        	comparingTileChar = tmap.getTileChar(bottomLeftX - 1, bottomLeftY);
        	
        	setVelocityX(-speed);
        }
        
        if(comparingTileChar == airTileChar) {
        	setScale((float) -getScaleX(), (float) getScaleY());
        }

	}

}
