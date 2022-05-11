/**
 * 
 */
package com.hyperconix.app;

/**
 * 
 * This enum is responsible for 
 * representing the various states
 * that the game can be in during execution.
 * 
 * @author Luke S
 *
 */
enum GameStatus {
	
	/**
	 * This status indicates that the game has started, the player
	 * is "playing" a level.
	 */
	GAME_STARTED, 
	
	/**
	 * This status indicates that the game has been paused
	 * by the player. The menu will be drawn again in this
	 * case.
	 */
	GAME_PAUSED,
	
	/**
	 * This status indicates that the player has lost
	 * all their lives, and the game as a result.
	 */
	GAME_OVER, 
	
	/**
	 * This status indicates that the player has completed
	 * the game and will display victory information.
	 */
	GAME_WIN,
	
	/**
	 * This status indicates that the player is at the menu.
	 * This will be the status of the game when the player first
	 * launches the game.
	 */
	GAME_MENU
}
