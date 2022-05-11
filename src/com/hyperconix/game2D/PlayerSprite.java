/**
 * 
 */
package com.hyperconix.game2D;

/**
 * This class is responsible for further encapsulating details
 * about the "Player" in Punch Man. For example, it makes more sense in OOP that
 * a Player character would know about its health, which direction it is currently moving in etc. This extends the Sprite
 * class so that it can retain compatibility with the rest of the game.  
 * 
 * Note: I have left Sprite as a non-abstract class because I still want to be able to create instances of generic Sprites (hud elements F.E)
 * 
 * @author Luke S
 *
 */
public class PlayerSprite extends Sprite 
{
	
	/**
	 * Responsible for storing the players health, if it drops to zero then the player is dead
	 */
	private int hits = 3; 
	
	/**
	 * Responsible for storing the number of lives the player has
	 */
	private int lives = 3;
	
	/**
	 * Responsible for storing the number of diamonds the player currently has
	 */
	private int diamonds = 0;
	
	  /**
     * Responsible for storing a flag which indicates whether or not the player is moving left (aka the left arrow key is pressed down)
     */
	private boolean movingLeft = false;
	
	  /**
     * Responsible for storing a flag which indicates whether or not the player is moving right (aka the right arrow key is pressed down)
     */
	private boolean movingRight = false; 
	
	/**
	 * Responsible for storing a flag which indicates whether or not the player is jumping
	 */
	private boolean jumping = false;
	
	/**
	 * Responsible for storing a flag which indicates whether or not the player is attacking
	 */
	private boolean attacking = false;
	
	
	/**
	 *  This creates the state of the Player Character sprite, as this is a Sprite, an animation must be provided 
	 *  that will be used for the Sprite. Additionally, the health value of the Player should be supplied
	 *  here.
	 *  
	 * @param anim The animation to be used for the sprite
	 * @param health The health value for the player
	 */
	public PlayerSprite(Animation anim) 
	{
		super(anim);
	
	}
	
	/**
	 * This method is responsible for returning an indication of whether or not the Player is currently moving right,
	 * essentially if the User has the d key pressed.
	 * 
	 * @return {@code true} if the player is moving right {@code false} otherwise
	 */
	public boolean isMovingRight() 
	{
		return movingRight;
	}
	
	/**
	 * This method is responsible for setting the flag of whether or not the Player is currently moving right
	 * 
	 * @param movingRight {@code true} The character is said to be moving right, {@code false} otherwise
	 */
	public void setMovingRight(boolean movingRight)
	{
		this.movingRight = movingRight;
	}
	
	/**
	 * This method is responsible for returning an indication of whether or not the Player is currently moving left,
	 * essentially if the User has the s key pressed.
	 * 
	 * @return {@code true} if the player is moving right {@code false} otherwise
	 */
	public boolean isMovingLeft() {
		return movingLeft;
	}
	
	/**
	 * This method is responsible for setting the flag of whether or not the Player is currently moving left
	 * 
	 * @param movingRight {@code true} The character is said to be moving left, {@code false} otherwise
	 */
	public void setMovingLeft(boolean movingLeft) 
	{
		this.movingLeft = movingLeft;
	}
	

	/**
	 * This method will return whether or not the character is currently jumping.
	 * 
	 * @return {@code true} if the character is currently jumping, {@code false} otherwise
	 */
	public boolean isJumping() {
		return jumping;
	}
	
	/**
	 * This method is responsible for setting the jumping state of the player.
	 * 
	 * @param jumping {@code true} Player is jumping {@code false} Player is not jumping
	 */
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
	
	
	/**
	 * This method will return whether or not the character is currently attacking
	 * 
	 * @return {@code true} If the player is currently attacking {@code false} otherwise
	 */
	public boolean isAttacking() {
		return attacking;
	}
	
	/**
	 * This method is responsible for setting the attack state of the player
	 * 
	 * @param attacking {@code true} Player is attacking {@code false} Player is not attacking
	 */
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	/**
	 * This method is responsible for returning an indication of whether or not the Player is currently idle. 
	 * The Player character is considered to be in the idle state when it is neither moving left or right.
	 * 
	 * @return {@code true} If the Player character is currently idle, {@code false} otherwise
	 */
	public boolean isIdle() 
	{
		return !movingLeft && !movingRight; 
	}

	
	/**
	 * This method is responsible for returning the number of hits a player has.
	 * 
	 * @return The number of hits the player currently has
	 */
	public int getHits() {
		return hits;
	}
	
	/**
	 * This method is responsible for setting the hits
	 * of the player.
	 * 
	 * @param hits The number of hits
	 */
	public void setHits(int hits) {
		this.hits = hits;
	}
	
	/**
	 * This method is responsible for returning the number of lives
	 * 
	 * @return The number of lives the player currently has
	 */
	public int getLives() {
		return lives;
	}
	
	/**
	 * This method is responsible for setting the number of lives 
	 * @param lives
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getDiamonds() {
		return diamonds;
	}

	public void setDiamonds(int diamonds) {
		this.diamonds = diamonds;
	}
	
}
