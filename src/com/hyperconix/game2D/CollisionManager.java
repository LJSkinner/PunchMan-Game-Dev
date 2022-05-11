/**
 * 
 */
package com.hyperconix.game2D;

/**
 * This class is responsible for managing 
 * collisions between sprites in the game. 
 * This was created to encapsulate more of the "generic" 
 * collision checks that are made, such as determining
 * whether collisions have occurred at certain tiles
 * within the tile
 * <br><br>
 * It also contains the logic for bounding box and bounding circle collisions
 * 
 * @author 2727141
 *
 */
public class CollisionManager {
	
	/**
	 * Responsible for storing the character that indicates the "air" or an empty space
	 * in the TileMap
	 */
	private char airTileCharacter;
	
	/**
	 * 
	 * @param airTileCharacter The air or empty space tile character being used 
	 */
	public CollisionManager(char airTileCharacter) {
		this.airTileCharacter = airTileCharacter;
	}
	
    /**
     * This method is responsible for checking if there has been a rectangle (bounding box) collision
     * between two sprites. This is a basic form of collision checking to determine the potential
     * of a collision.
     * 
     * @param firstSprite The first sprite to be compared in the collision check
     * @param secondSprite The second sprite to be compared in the collision check
     * 
     * @return {@code true} if there was a collision, {@code false} otherwise
     */
    public boolean boundingBoxCollision(Sprite firstSprite, Sprite secondSprite)
    {
        return ((firstSprite.getX() + firstSprite.getImage().getWidth(null) > secondSprite.getX()) &&
                (firstSprite.getX() < (secondSprite.getX() + secondSprite.getImage().getWidth(null))) &&
                ((firstSprite.getY() + firstSprite.getImage().getHeight(null) > secondSprite.getY()) &&
                        (firstSprite.getY() < secondSprite.getY() + secondSprite.getImage().getHeight(null))));
    }
    
    /**
     * 
     * This method is responsible for checking if there has been a circle collision
     * between two sprites. This is a more advanced check than the bounding box,
     * and is uses to check if the sprites have "overlapped" and their centres
     * are colliding. 
     * 
     * @param firstSprite The first sprite to be compared in the collision check
     * @param secondSprite The second sprite to be compared in the collision check
     * 
     * @return {@code true} if there was a collision, {@code false} otherwise
     */
    public boolean boundingCircleCollision(Sprite firstSprite, Sprite secondSprite)
    {
        int s1CentreWidth = (int) (firstSprite.getX() + (firstSprite.getWidth() / 2));
        
        int s1CentreHeight = (int) (firstSprite.getY() + (firstSprite.getHeight() / 2));
        
        int s2CentreWidth = (int) (secondSprite.getX() + (secondSprite.getWidth() / 2));
        
        int s2CentreHeight = (int) (secondSprite.getY() + (secondSprite.getHeight() / 2));

        int dx = s1CentreWidth - s2CentreWidth;
        
        int dy = s1CentreHeight - s2CentreHeight;
        
        int min = (int) (firstSprite.getRadius() + secondSprite.getRadius());

        return (((dx * dx) + (dy * dy)) < (min * min));
    }
    
    /**
     * This method is responsible for checking whether or not a collision has happened
     * between a sprite and a "Right" tile. That is a tile that is to the right of the sprite.
     * 
     * @param collidingTile The tile from the tilemap that is being collided with
     * @param collidingSprite The sprite that is colliding with the tile in the tilemap
     * @param currentTileMap The current tilemap that is in use
     * 
     * @return {@code true} If there a collision between a right tile and the sprite, otherwise {@code false}
     */
    public boolean collisionAtRight(Tile collidingTile, Sprite collidingSprite, TileMap currentTileMap) 
    {
    	return collidingTile.getCharacter() != airTileCharacter && 
    		   (collidingSprite.getX() + collidingSprite.getWidth() >
    		   collidingTile.getXC());
    }
    
    /**
     * This method is responsible for checking whether or not a collision has happened
     * between a sprite and a "Left" tile. That is a tile that is to the left of the sprite.
     * 
     * @param collidingTile The tile from the tilemap that is being collided with
     * @param collidingSprite The sprite that is colliding with the tile in the tilemap
     * @param currentTileMap The current tilemap that is in use 
     * 
     * @return {@code true} If there a collision between a right tile and the sprite, otherwise {@code false}
     */
    public boolean collisionAtLeft(Tile collidingTile, Sprite collidingSprite, TileMap currentTileMap) 
    {
    	return collidingTile.getCharacter() != airTileCharacter && 
    		   collidingSprite.getX() < (collidingTile.getXC() + currentTileMap.getTileWidth());
    		   
    }
    
    /**
     * This method is responsible for checking whether or not a collision has happened
     * between a sprite and a "Top" tile. That is a tile which is above the sprite.
     * 
     * @param collidingTile The tile from the tilemap that is being collided with
     * @param collidingSprite The sprite that is colliding with the tile in the tilemap
     * @param currentTileMap The current tilemap that is in use
     * 
     * @return {@code true} If there a collision between a right tile and the sprite, otherwise {@code false}
     */
    public boolean collisionAtTop(Tile collidingTile, Sprite collidingSprite, TileMap currentTileMap) 
    {
    	return collidingTile.getCharacter() != airTileCharacter && 
    		   collidingSprite.getY() < (collidingTile.getYC() + currentTileMap.getTileHeight());
    }
    
    /**
     * This method is responsible for checking whether or not a collision has happened
     * between a sprite and a "Bottom" tile. That is a tile which is at the bottom of the
     * sprite.
     * 
     * @param collidingTile The tile from the tilemap that is being collided with
     * @param collidingSprite The sprite that is colliding with the tile in the tilemap
     * @param currentTileMap The current tilemap that is in use
     * 
     * @return {@code true} If there a collision between a right tile and the sprite, otherwise {@code false}
     */
    public boolean collisionAtBottom(Tile collidingTile, Sprite collidingSprite, TileMap currentTileMap) 
    {
    	return collidingTile.getCharacter() != airTileCharacter && 
    		  (collidingSprite.getY() + collidingSprite.getHeight() >
    	      collidingTile.getYC()); 
    }
	
}
