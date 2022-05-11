package com.hyperconix.game2D;

public interface Enemy {
	
	/**
	 * This represents an enemies move algorithm. It is not
	 * important from the place of this contract to know the
	 * kind of movement is but every Enemy will have some kind
	 * of movement that interacts with the environment and the player.
	 */
	void move(char airTileChar, TileMap tmap, float speed);
	
	
	//void attack();
	
	

}
