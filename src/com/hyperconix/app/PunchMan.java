package com.hyperconix.app;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import com.hyperconix.game2D.Animation;
import com.hyperconix.game2D.CollisionManager;
import com.hyperconix.game2D.GameCore;
import com.hyperconix.game2D.MIDIPlayer;
import com.hyperconix.game2D.PlayerSprite;
import com.hyperconix.game2D.Sound;
import com.hyperconix.game2D.Sprite;
import com.hyperconix.game2D.Tile;
import com.hyperconix.game2D.TileMap;
import com.hyperconix.game2D.WalkingEnemy;

/**
 * This class is responsible for holding the game logic and also contains the
 * entry point (main method) which is responsible for running the game loop.
 * This class extends GameCore which contains a number of the core functionality
 * and methods used within the game, provided by the game2D library.
 * 
 * This game contains a number of sprites which were obtained from open source
 * locations, the attributions/references for all of these can be found in the
 * provided text file.
 *
 * @author Luke S
 */
@SuppressWarnings("serial")

public class PunchMan extends GameCore implements MouseListener {

	/**
	 * Responsible for storing the width of the game window.
	 */
	private static final int SCREEN_WIDTH = 800;

	/**
	 * Responsible for storing the height of the game window.
	 */
	private static final int SCREEN_HEIGHT = 400;
	
	/**
	 * Responsible for storing the number of enemies
	 */
	private static final int ENEMY_NO = 2;

	/**
	 * Responsible for storing the players starting x value.
	 */
	private float startingX = 64;

	/**
	 * Responsible for storing the players starting y value.
	 */
	private float startingY = 138;

	/**
	 * Responsible for storing the exit button x coord for the menu/pause screen.
	 */
	private static final int MENU_EXIT_BTN_X = 20;

	/**
	 * Responsible for storing the exit button y coord for the menu/pause screen.
	 */
	private static final int MENU_EXIT_BTN_Y = 40;

	/**
	 * Responsible for storing the play button x coord for the menu/pause screen.
	 */
	private static final int MENU_PLAY_BTN_X = 255;

	/**
	 * Responsible for storing the play button y coord for the menu/pause screen.
	 */
	private static final int MENU_PLAY_BTN_Y = 200;
	
	/**
	 * Responsible for storing the default UI font that is used.
	 */
	private static final String DEFAULT_UI_FONT = "Consolas"; 

	/**
	 * Responsible for storing the current coin count for the level.
	 */
	private int coinCount;

	/**
	 * Responsible for storing a float which represents the lift of the player when
	 * gravity is applied.
	 */
	private float lift = -0.12f;

	/*
	 * 8 Responsible for storing the background MIDIPlayer used for the background
	 * music.
	 */
	private MIDIPlayer backgroundPlayer;

	/**
	 * Responsible for storing the current status of the game.
	 */
	private GameStatus currentStatus;

	/**
	 * Responsible for storing the current level of the game.
	 */
	private Level currentLevel;

	/**
	 * Responsible for storing an enum which represents the levels in the game.
	 *
	 */
	private enum Level {
		LEVEL_1, LEVEL_2
	}

	/**
	 * Responsible for storing a float which represents the gravity of the player.
	 */
	private float gravity = 0.0003f;

	/**
	 * Responsible for storing a flag which indicates whether or not debug mode is
	 * enabled.
	 * 
	 * @see PunchMan#enableDebugMode()
	 */
	private boolean debugMode = false;

	/**
	 * Responsible for storing a flag which indicates whether the player is in the
	 * portal bounds or not.
	 */
	private boolean inPortalBounds = false;

	/**
	 * Responsible for storing a flag which indicates whether the player is in the
	 * bounds of the switch or not.
	 */
	private boolean inSwitchBounds = false;
	
	/**
	 * Responsible for storing a flag which indicates whether the switch (if there is one) is flipped
	 */
	private boolean switchFlipped = false;
	
	/**
	 * Responsible for storing a flag which which represents whether the background music should be muted.
	 */
	private boolean muteBackgroundMusic = false;

	/**
	 * Responsible for storing the Sprite that represents the player.
	 */
	private PlayerSprite player;

	/**
	 * Responsible for storing the list of enemies.
	 */
	private ArrayList<WalkingEnemy> enemies;

	/**
	 * Responsible for storing the portal sprite.
	 */
	private Sprite portal;

	/**
	 * Responsible for storing the switch sprite.
	 */
	private Sprite switchSprite;

	/**
	 * Responsible for storing the players animations.
	 */
	private Animation playerMove, playerIdle, playerAttack;

	/**
	 * Responsible for storing the blue spike enemies animation. This enemy only ha
	 * a move animation.
	 */
	private Animation blueSpikeMove;

	/**
	 * Responsible for storing the portal animations. Portals are used to traverse
	 * levels, and complete the game if it is the last level.
	 */
	private Animation portalAnimation;

	/**
	 * Responsible for storing the switch animations. Switches are used to change
	 * certain things in the environment. Such as revealing platforms or gems.
	 */
	private Animation switchAnimation;

	/**
	 * Responsible for storing the level 1 tile map, this is loaded in the init.
	 */
	private TileMap level1Map = new TileMap();

	/**
	 * Responsible for storing the level 2 tile map. This will be loaded when the
	 * player completes the first level and passes through the portal.
	 */
	private TileMap level2Map = new TileMap();

	/**
	 * Responsible for storing the current map. This will be changed depending on
	 * what level the player is currently on.
	 */
	private TileMap currentMap;

	/**
	 * Responsible for storing the total score for the player.
	 */
	private long total;

	/**
	 * Responsible for storing the Collision Manager for the game.
	 */
	private CollisionManager collisionManager;
	
	/**
	 * Responsible for storing the UI Images for the UI elements of the game.
	 */
	private Image UICoin, UIHeart, UILife, UIDiamond, UIBackground, UIPlay, UIExit;

	/**
	 * The obligatory main method that creates an instance of our class and starts
	 * it running
	 *
	 * @param args The list of parameters this program might use (ignored)
	 */
	public static void main(String[] args) {
		PunchMan gct = new PunchMan();

		gct.init();

		// Start in windowed mode with the given screen height and width
		gct.run(false, SCREEN_WIDTH, SCREEN_HEIGHT);
	}

	/**
	 * Initialise the class, e.g. set up variables, load images, create animations,
	 * register event handlers
	 */
	public void init() {

		collisionManager = new CollisionManager('.');

		currentStatus = GameStatus.GAME_MENU;

		currentLevel = Level.LEVEL_1;

		backgroundPlayer = new MIDIPlayer("sounds/004AdventureScores(Free)140BPM2-4GMinor.mid");

		// Load the tile map and print it out so we can check it is valid
		level1Map.loadMap("maps", "level1_map.txt");

		level2Map.loadMap("maps", "level2_map.txt");

		currentMap = level1Map;

		// Defining some offsets to position adjust the size

		int heightOffset = 72;

		int widthOffset = 3;

		setSize(currentMap.getPixelWidth() / widthOffset, currentMap.getPixelHeight() - heightOffset);

		setVisible(true);

		setResizable(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		addMouseListener(this);

		setAnimations();

		loadGameImages();

		enemies = new ArrayList<>();

		for (int i = 0; i < ENEMY_NO; i++) {
			WalkingEnemy enemy = new WalkingEnemy(blueSpikeMove);
			enemy.setScale(2.0f);
			enemies.add(enemy);
		}

		player = new PlayerSprite(playerIdle);

		portal = new Sprite(portalAnimation);

		switchSprite = new Sprite(switchAnimation);

		player.setScale(2.0f);

		portal.setScale(1.0f);

		initialiseGame();

		System.out.println(currentMap);
	}

	/**
	 * You will probably want to put code to restart a game in a separate method so
	 * that you can call it to restart the game.
	 */
	public void initialiseGame() {
		total = 0;

	    
		player.setVelocityX(0);

		player.setVelocityY(0);

		placePlayer();

		placeEnemies();

		placeSwitches();

	}

	/**
	 * Draw the current state of the game. There are multiple states in the game
	 * which change depending on the players actions
	 */
	public void draw(Graphics2D g) {

		// Drawing will now draw based on the status of the game
		switch (currentStatus) {
		case GAME_MENU:
			drawMenu(g);
			break;
		case GAME_OVER:
			drawGameOver(g);
			break;
		case GAME_STARTED:
			drawLevel(g);
			break;
		case GAME_PAUSED:
			drawMenu(g);
			break;
		case GAME_WIN:
			drawWinning(g);
			break;
		default:
			break;
		}

	}

	/**
	 * This method will draw the heads up display elements for the game. Such as
	 * health, diamonds and lives.
	 * 
	 * @param g The graphics object to draw on
	 */
	public void drawHUD(Graphics2D g) {

		// First we will draw the common indicators, life, hearts or hit points and the
		// diamonds
		drawHUDCommonIndicator(g, UILife, 21, player.getLives(), 540, 41);

		drawHUDCommonIndicator(g, UIHeart, 21, player.getHits(), 611, 41);

		drawHUDCommonIndicator(g, UIDiamond, 21, player.getDiamonds(), 21, 370);

		// Next we will draw the coin indicator which will be updated
		g.drawImage(UICoin, 28, 40, null);

		String msg = String.format("%d", coinCount);

		g.setFont(new Font("Arial", Font.BOLD, 20));

		g.setColor(coinCount == 0 ? Color.red : Color.yellow);

		g.drawString(msg, 52, 56);
	}

	/**
	 * This method is responsible for drawing "common indicators", that is heads up
	 * display elements which will either increase or decrease depending on
	 * gameplay.
	 * 
	 * @param g             The graphics object to draw on
	 * @param resourceImage The image for the UI element
	 * @param spacing       The spacing between each element
	 * @param limit         The limit, I.E how many to draw.
	 * @param x             The x position
	 * @param y             The y position
	 */
	public void drawHUDCommonIndicator(Graphics2D g, Image resourceImage, int spacing, int limit, int x, int y) {
		for (int i = 0; i < limit; i++) {
			g.drawImage(resourceImage, x, y, null);
			x += spacing;
		}
	}

	/**
	 * This method is responsible for drawing the menu which is displayed when the
	 * player first launches the game and when the game is paused.
	 * 
	 * @param g The graphics object to draw on
	 */
	public void drawMenu(Graphics2D g) {
		// Draw background first
		g.drawImage(UIBackground, (SCREEN_WIDTH / 2) - (UIBackground.getWidth(null) / 2), 0, null);

		g.setFont(new Font(DEFAULT_UI_FONT, Font.BOLD, 60));

		g.setColor(Color.black);

		g.drawString("Punch Man", 216, 100);

		g.drawImage(UIPlay, MENU_PLAY_BTN_X, MENU_PLAY_BTN_Y, null);

		g.drawImage(UIExit, MENU_EXIT_BTN_X, MENU_EXIT_BTN_Y, null);

	}

	/**
	 * This method is responsible for drawing the screen which is shown whenever the
	 * player encounters a game over.
	 * 
	 * @param g The graphics object to draw on
	 */
	public void drawGameOver(Graphics2D g) {
		g.drawImage(UIBackground, (SCREEN_WIDTH / 2) - (UIBackground.getWidth(null) / 2), 0, null);

		g.setFont(new Font(DEFAULT_UI_FONT, Font.BOLD, 60));

		g.setColor(Color.black);

		g.drawString("You Lose", 230, 75);

		g.setFont(new Font(DEFAULT_UI_FONT, Font.BOLD, 30));

		g.setColor(Color.black);

		g.drawString("Press Enter to continue", 163, 240);
	}

	/**
	 * This method is responsible for drawing the current level which is being
	 * played.
	 * 
	 * @param g The graphics object to draw on
	 */
	public void drawLevel(Graphics2D g) {

		// X Off relative to the players X position

		int relativeXOffset = (SCREEN_WIDTH / 2) - Math.round(player.getX());

		// Y Off relative to the players Y position.

		int relativeYOffset = (SCREEN_HEIGHT / 2) - Math.round(player.getY());

		// Define background layers (when expanding levels, might be good to define
		// these elsewhere)

		Image bgBack = java.awt.Toolkit.getDefaultToolkit().getImage("images/Backgrounds/back.png");

		Image bgMiddle = java.awt.Toolkit.getDefaultToolkit().getImage("images/Backgrounds/middle.png");

		Image bgNear = java.awt.Toolkit.getDefaultToolkit().getImage("images/Backgrounds/near.png");

		// Draw background

		g.drawImage(bgBack, toParallax(50, bgBack, relativeXOffset), 0, null);

		g.drawImage(bgMiddle, toParallax(30, bgMiddle, relativeXOffset), 0, null);

		g.drawImage(bgNear, toParallax(10, bgNear, relativeXOffset), 0, null);

		// Adjust the relative offsets so they have a minimum and maximum points, and
		// keep the "camera" on the player.

		relativeXOffset = Math.min(relativeXOffset, 0);

		relativeXOffset = Math.max(SCREEN_WIDTH - currentMap.getPixelWidth(), relativeXOffset);

		relativeYOffset = Math.min(relativeYOffset, 0);

		relativeYOffset = Math.max(SCREEN_HEIGHT - currentMap.getPixelHeight(), relativeYOffset);

		// Apply offsets to sprites and draw
		player.setOffsets(relativeXOffset, relativeYOffset);

		portal.setOffsets(relativeXOffset, relativeYOffset);

		switchSprite.setOffsets(relativeXOffset, relativeYOffset);

		for (Sprite enemy : enemies) {
			enemy.setOffsets(relativeXOffset, relativeYOffset);
		}

		player.drawTransformed(g);

		portal.drawTransformed(g);

		switchSprite.drawTransformed(g);

		drawEnemies(g);

		// Apply offsets to tile map and draw it
		currentMap.draw(g, relativeXOffset, relativeYOffset);

		drawHUD(g);

		if (debugMode) {
			g.setColor(Color.blue);

			player.drawBoundingBox(g);

			portal.drawBoundingBox(g);

			switchSprite.drawBoundingBox(g);

			g.setColor(Color.yellow);

			portal.drawBoundingCircle(g);

			player.drawBoundingCircle(g);

			switchSprite.drawBoundingCircle(g);
		}
	}

	/**
	 * This method is responsible for drawing the screen which will be shown when
	 * the user has completed the game.
	 * 
	 * @param g The graphics object to draw on
	 */
	public void drawWinning(Graphics2D g) {
		g.drawImage(UIBackground, (SCREEN_WIDTH / 2) - (UIBackground.getWidth(null) / 2), 0, null);

		g.setFont(new Font(DEFAULT_UI_FONT, Font.BOLD, 60));

		g.setColor(Color.black);

		g.drawString("Well Done!", 200, 75);

		g.setFont(new Font(DEFAULT_UI_FONT, Font.BOLD, 30));

		g.setColor(Color.black);

		g.drawString("Press Enter to Restart", 163, 240);

		g.setFont(new Font(DEFAULT_UI_FONT, Font.BOLD, 30));

		g.setColor(Color.black);

		String msg = String.format("%d", total);

		g.drawString("Total Score: " + msg, 159, 280);
	}

	public void drawEnemies(Graphics2D g) {
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).drawTransformed(g);

			if (debugMode) {
				g.setColor(Color.blue);

				enemies.get(i).drawBoundingBox(g);

				g.setColor(Color.magenta);

				enemies.get(i).drawBoundingCircle(g);
			}

		}

	}

	/**
	 * Update any sprites and check for collisions
	 *
	 * @param elapsed The elapsed time between this call and the previous call of
	 *                elapsed
	 */
	public void update(long elapsed) {

		if (currentStatus != GameStatus.GAME_STARTED) {
			return;
		}
			
		player.setAnimationSpeed(1.0f);

		portal.setAnimationSpeed(1.0f);

		if (player.isDead() || player.isOnDeathTile()) {
			Sound s = new Sound("sounds/death.wav");

			s.start();

			respawnPlayer();

			player.setOnDeathTile(false);
		}

		if (player.getLives() == 0)
			currentStatus = GameStatus.GAME_OVER;

		if (player.isMovingRight()) {
			player.setVelocityX(0.1f);
		}

		if (player.isMovingLeft()) {
			player.setVelocityX(-0.1f);
		}

		// Apply conditional gravity to player when they are not on the ground
		if (!player.isOnGround()) {
			player.setVelocityY(player.getVelocityY() + (gravity * elapsed));
		}

		// Now update the sprites animation and position
		player.update(elapsed);

		portal.update(elapsed);
		
		switchSprite.update(elapsed);

		checkAttackState();

		checkIdleState();

		checkBackgroundMusic();

		checkEnemyPathFinding();

		checkPortalUnlockStatus();

		checkPlayerToSpriteCollisions();

		for (Sprite enemy : enemies) {
			enemy.update(elapsed);
		}

		handleScreenEdge(player, currentMap, elapsed);

		checkTileCollision(player, currentMap);

		for (Sprite enemy : enemies) {
			checkTileCollision(enemy, currentMap);
		}

	}

	/**
	 * This method will parallax scroll an image. Given an image and the speed
	 * desired, this method will return a scroll speed for that image. In layered
	 * backgrounds, this will create the illusion of depth.
	 * 
	 * @param speed           The image speed
	 * @param imageToParallax The image to parallax
	 * @return The scroll speed of the parallaxed image
	 */
	public int toParallax(int speed, Image imageToParallax, int relativeXOffset) {
		// We need the X offset first. We will adjust the x offset (which is relative to
		// the player) by subtracting the map width
		int adjustedXOffset = relativeXOffset - currentMap.getMapWidth();

		// We adjust the offset in the same way here, so the scrolling is also
		// consistent with where the player is
		adjustedXOffset = Math.min(adjustedXOffset, 0);

		adjustedXOffset = Math.max(adjustedXOffset, SCREEN_WIDTH - currentMap.getPixelWidth());

		return adjustedXOffset * (SCREEN_WIDTH + imageToParallax.getWidth(null))
				/ (SCREEN_WIDTH * speed - currentMap.getMapWidth());
	}

	/**
	 * This method is responsible for handling the idle state of the player. The
	 * player is considered idle if they are neither moving left or right. This will
	 * trigger an idle animation when the player is in this state.
	 * 
	 */
	public void checkIdleState() {

		if (player.isAttacking()) {
			return;
		}

		Animation currentAnimation = player.getAnimation();

		if (player.isIdle()) {
			player.setAnimation(playerIdle);
		} else {
			currentAnimation.play();
		}

	}

	/**
	 * This method will handle the attack state of the player. While the attack()
	 * method is the logic contained when the player presses the attack button F.
	 * This method ensures the full animation loops before resetting.
	 */
	public void checkAttackState() {

		if (!player.isAttacking()) {
			return;
		}
			
		player.setAnimation(playerAttack);

		Animation attackAnimation = player.getAnimation();

		if (attackAnimation.hasLooped()) {
			attackAnimation.start();
			
			player.setAttacking(false);
		}

	}

	/**
	 * This method is responsible for handling the enemy path finding. This means
	 * for the respective level, calling the move and attack functionality, where
	 * present. These enemies will interact with the TileMap currently in play.
	 */
	public void checkEnemyPathFinding() {
		for (WalkingEnemy enemy : enemies) {
			enemy.move('.', currentMap, 0.1f);
		}
	}

	/**
	 * This method is responsible for checking if the portal has been unlocked
	 */
	public void checkPortalUnlockStatus() {
		if (player.getDiamonds() == 3) {
			showPortal();
		}
	}

	/**
	 * This method is responsible for showing the portal whenever the conditions are
	 * met, depending on the level the player is currently in.
	 */
	public void showPortal() {
		switch (currentLevel) {
		case LEVEL_1:
			portal.setX(startingX);
			portal.setY(startingY - 10);
			portal.show();
			break;
		case LEVEL_2:
			portal.setX(startingX);
			portal.setY(startingY - 25);
			portal.show();
			break;
		default:
			break;

		}
	}

	/**
	 * This method is responsible for handling the background music which plays.
	 * This uses a midi track, which is controlled by a class which can modify parts
	 * of the track at different moments.
	 */
	public void checkBackgroundMusic() {
		
		if(muteBackgroundMusic) {
			backgroundPlayer.stopScore();
			
			return;
		}

		if (!backgroundPlayer.playing()) {
			backgroundPlayer.playScore(true);
		}

		if (player.getHits() == 1) {
			backgroundPlayer.solo(1, true);

			backgroundPlayer.alterTempo(2f);
		} else {
			backgroundPlayer.solo(1, false);

			backgroundPlayer.alterTempo(1f);
		}
	}

	/**
	 * This method is responsible for checking PlayerToSprite collisions within
	 * whatever level is currently being played.
	 */
	public void checkPlayerToSpriteCollisions() {

		clearBoundFlags();

		// Iterate over Sprites in the level
		for (int i = 0; i < enemies.size(); i++) {
			Sprite currentEnemy = enemies.get(i);

			if (collisionManager.boundingBoxCollision(player, currentEnemy)) {
				// We use the bounding box collision as a first check, then check for a bounding
				// circle collision
				if (collisionManager.boundingCircleCollision(player, currentEnemy) && !currentEnemy.isDead()) {
					if (player.isAttacking()) {
						Sound s = new Sound("sounds/Hit4.wav");

						s.start();
						
						currentEnemy.hide();

						currentEnemy.stop();

						currentEnemy.setDead(true);
					} else {
						
						Sound s = new Sound("sounds/Hit5.wav");

						s.start();
						
						currentEnemy.setScale((float) -currentEnemy.getScaleX(), (float) currentEnemy.getScaleY());

						currentEnemy.setVelocityX(-currentEnemy.getVelocityX());

						player.stop();

						// How much to knock the player back when they get hit. Push them left or right depending on which direction they are facing.
						float pushBack = player.getScaleX() > 0 ? player.getX() - 4f : player.getX() + 4f;

						player.setHits(player.getHits() - 1);

						if (player.getHits() == 0) {
							player.setDead(true);
						}

						player.setX(pushBack);
						
						// We will push the player up into the air similar to a jump.
						// This is to indicate damage has been dealt, but also mitigate them getting stuck when colliding from certain angles.
						player.setVelocityY(lift);

					}
				}

			}
		}
		
		if (collisionManager.boundingBoxCollision(player, portal)) {
			if (collisionManager.boundingCircleCollision(player, portal)) {
				inPortalBounds = true;
			}
		}

		if (collisionManager.boundingBoxCollision(player, switchSprite)) {
			if (collisionManager.boundingCircleCollision(player, switchSprite)) {
				inSwitchBounds = true;
			}
		}
	}

	/**
	 * This method is responsible for respawning the player after they have lost all
	 * of their health. It will reset their health as well as their dead state.
	 */
	public void respawnPlayer() {

		player.setLives(player.getLives() - 1);
		player.setDead(false);
		player.setHits(3);

		player.setX(startingX);

		player.setY(startingY);

		player.setVelocityX(0);

		player.setVelocityY(0);
	}
	
	/**
	 * This method is responsible for clearing the bounds flags
	 */
	public void clearBoundFlags() {
		inPortalBounds = false;

		inSwitchBounds = false;
	}

	/**
	 * Checks and handles collisions with the edge of the screen
	 *
	 * @param s       The Sprite to check collisions for
	 * @param tmap    The tile map to check
	 * @param elapsed How much time has gone by since the last call
	 */
	public void handleScreenEdge(Sprite s, TileMap tmap, long elapsed) {
		if (s.getY() + s.getHeight() > tmap.getPixelHeight()) {
			// If the sprite falls of the edge then we will set its dead state to true
			s.setDead(true);
		}
	}

	/**
	 * Override of the keyPressed event defined in GameCore to catch our own events
	 *
	 * @param e The event that has been generated
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (currentStatus == GameStatus.GAME_PAUSED) {
			return;
		}

		switch (key) {
		case KeyEvent.VK_ESCAPE:
			stop();
			break;
		case KeyEvent.VK_SPACE:
			startJump();
			break;
		case KeyEvent.VK_1:
			skipToLevel1();
			break;
		case KeyEvent.VK_2:
			skipToLevel2();
			break;
		case KeyEvent.VK_A:
			movePlayerLeft();
			break;
		case KeyEvent.VK_D:
			movePlayerRight();
			break;
		case KeyEvent.VK_F:
			startAttack();
			break;
		case KeyEvent.VK_V:
			enableDebugMode();
			break;
		case KeyEvent.VK_P:
			pauseGame();
			break;
		case KeyEvent.VK_ENTER:
			startOver();
			break;
		case KeyEvent.VK_E:
			determineInteractiveAction();
			break;
		case KeyEvent.VK_M:
			toggleBackgroundMusic();
			break;
		default:
			break;
		}

	}
	
	/**
	 * This method is responsible for determining which interactive
	 * action to activate. This is called whenever the E button is
	 * pressed.
	 */
	public void determineInteractiveAction() {
		
		if(inPortalBounds) {
			enterPortal();
		}
		else if(inSwitchBounds) {
			flipSwitch();
		}	
	}

	/**
	 * This method is responsible for setting the state of the player to move left,
	 * and if they were facing right it will flip them. This is called whenever the
	 * A key is pressed.
	 */
	public void movePlayerLeft() {
		// We do not want to allow the player to move while attacking
		if (player.isAttacking()) {
			return;
		}

		// We want to flip the sprite when they are not moving left already and facing
		// right x > 0
		if (!player.isMovingLeft() && player.getScaleX() > 0) {
			player.setScale((float) -player.getScaleX(), (float) player.getScaleY());
		}

		player.setMovingLeft(true);

		player.setAnimation(playerMove);

	}

	/**
	 * This method is responsible for setting the state of the player to move right,
	 * and if they were facing left it will flip them. This is called whenever the D
	 * key is pressed.
	 */
	public void movePlayerRight() {
		// We do not want to allow the player to move while attacking
		if (player.isAttacking()) {
			return;
		}

		// We want to flip the sprite when they are not moving right already and facing
		// left x < 0
		if (!player.isMovingRight() && player.getScaleX() < 0) {
			player.setScale((float) -player.getScaleX(), (float) player.getScaleY());
		}

		player.setMovingRight(true);

		player.setAnimation(playerMove);
	}

	/**
	 * This method is responsible for determining whether the player can jump and
	 * setting that state accordingly. The player must be on the ground and not
	 * already jumping to be able to jump.
	 */
	public void startJump() {

		boolean jumpPossible = player.isOnGround() && !player.isJumping();

		if (!jumpPossible) {
			return;
		}
			
		player.setJumping(true);

		player.setVelocityY(lift);

		player.setOnGround(false);

		Sound s = new Sound("sounds/Jump1.wav");

		s.start();
	}

	/**
	 * This method is responsible for determining whether the player can attack and
	 * setting that state accordingly. The player must not be already attacking or
	 * moving, and they must be on the ground for the attack to be valid.
	 * 
	 * Controlling the attack animation state is controlled by another method which
	 * is called in the update loop.
	 * 
	 * @see PunchMan#checkAttackState()
	 */
	public void startAttack() {
		boolean attackValid = !player.isAttacking() && (!player.isMovingLeft() && !player.isMovingRight())
				&& !player.getAnimation().equals(playerAttack) && player.isOnGround();

		if (!attackValid) {
			return;
		}
			
		player.setAttacking(true);

		Sound s = new Sound("sounds/hit28.wav");

		s.start();
	}

	/**
	 * This method will enable debug mode for the game. This will render debug
	 * information such as bounding box/circles around sprite. Information about the
	 * sprites in the current level etc. This toggles a boolean flag representing
	 * whether to debug or not.
	 * 
	 * @see PunchMan#debugMode
	 */
	public void enableDebugMode() {
		debugMode = !debugMode;
	}

	/**
	 * This method will pause the game, which means it will set the state to the
	 * game menu, provided it is not already set. Pressing the P button will toggle
	 * the pause and then player can click the play button again to resume.
	 */
	public void pauseGame() {
		if (currentStatus != GameStatus.GAME_MENU) {
			currentStatus = GameStatus.GAME_MENU;
		}
	
	}

	/**
	 * This method is responsible for starting over when a player has lost and
	 * resulted in a game over or if the player has completed the game and wants to
	 * restart. This set the state back to GAME_STARTED and reload the level.
	 * 
	 * @see PunchMan#reload()
	 */
	public void startOver() {
		if (currentStatus == GameStatus.GAME_OVER) {
			currentStatus = GameStatus.GAME_STARTED;

		} else if (currentStatus == GameStatus.GAME_WIN) {
			currentLevel = Level.LEVEL_1;

			currentStatus = GameStatus.GAME_STARTED;
		}

		reload();

	}

	/**
	 * This method is responsible for entering the portal. They must be in the
	 * bounds of the portal. This is an interactive action which will be called
	 * as a result of the player pressing the E button near a portal.
	 * 
	 * @see PunchMan#determineInteractiveAction()
	 */
	public void enterPortal() {
		Sound s = new Sound("sounds/time_travel_clip.wav");

		s.start();

		s.useFilter(true);

		total += coinCount;

		if (currentLevel == Level.LEVEL_1) {
			currentLevel = Level.LEVEL_2;

			currentMap = level2Map;

		} else {
			currentStatus = GameStatus.GAME_WIN;
			return;
		}

		reload();
	}
	
	/**
	 * This method is responsible for flipping the switch. They must
	 * be in the bounds of the switch. This is an interactive action
	 * which will be called as a result of the player pressing
	 * the E button near a portal.
	 * 
	 * @see PunchMan#determineInteractiveAction()
	 */
	public void flipSwitch() {
		switchFlipped = !switchFlipped;
		
		Sound s = new Sound("sounds/Select4.wav");

		s.start();
		
		switchSprite.getAnimation().setAnimationFrame(switchFlipped ? 0 : 1);
		
		if(currentLevel == Level.LEVEL_1) {
			
			char actionTileChar = switchFlipped ? 'p' : '.';
			
			// When the switch is flipped in level 1, reveal the platform tile.
			currentMap.setTileChar(actionTileChar, 45, 9);
		}
		else if(currentLevel == Level.LEVEL_2) {
			char actionTileChar = switchFlipped ? 'v' : '.';
			
			// When the switch is flipped in level 2, reveal the final diamond near the start of the level.
			currentMap.setTileChar(actionTileChar, 7, 6);
		}
	}
	
	/**
	 * This method is responsible for skipping or reloading to level
	 * 1. This will be called when the 1 key is pressed.
	 */
	public void skipToLevel1() {
		currentLevel = Level.LEVEL_1;
		
		reload();
	}
	
	/**
	 * This method is responsible for skipping or reloading to level
	 * 2. This will be called when the 2 key is pressed.
	 */
	public void skipToLevel2() {
		currentLevel = Level.LEVEL_2;
		
		reload();
	}
	
	/**
	 * This method is responsible for toggling the background music
	 * of the game.
	 */
	public void toggleBackgroundMusic() {
		muteBackgroundMusic = !muteBackgroundMusic;
	}

	/**
	 * This method is responsible for reloading the level that is currently being
	 * played, and setting the players values back to their defaults.
	 */
	public void reload() {
		coinCount = 0;
		
		total = 0;

		player.setDiamonds(0);

		player.setHits(3);

		player.setLives(3);

		player.setDead(false);

	    clearBoundFlags();
	    
	    switchFlipped = false;
	    
	    switchSprite.getAnimation().setAnimationFrame(switchFlipped ? 0 : 1);

		portal.hide();
		
		placePlayer();
		
		placeSwitches();
		
		placeEnemies();

		if (currentLevel == Level.LEVEL_1) {
			currentMap.loadMap("maps", "level1_map.txt");
		} else {
			currentMap.loadMap("maps", "level2_map.txt");
		}
		
	}

	/**
	 * This method is responsible for setting the animations which will be used in
	 * the game and their speeds.
	 */
	public void setAnimations() {
		playerMove = new Animation();

		playerIdle = new Animation();

		playerAttack = new Animation();

		blueSpikeMove = new Animation();

		portalAnimation = new Animation();

		switchAnimation = new Animation();

		playerMove.addFrame(loadImage("images/Characters/Player/Move/Right1.png"), 130);

		playerMove.addFrame(loadImage("images/Characters/Player/Move/Right2.png"), 130);

		playerMove.addFrame(loadImage("images/Characters/Player/Move/Right3.png"), 130);

		playerIdle.addFrame(loadImage("images/Characters/Player/Idle/Right1.png"), 130);

		playerIdle.addFrame(loadImage("images/Characters/Player/Idle/Right2.png"), 130);

		playerIdle.addFrame(loadImage("images/Characters/Player/Idle/Right3.png"), 130);

		playerIdle.addFrame(loadImage("images/Characters/Player/Idle/Right4.png"), 130);

		playerAttack.addFrame(loadImage("images/Characters/Player/Attack/Right1.png"), 100);

		playerAttack.addFrame(loadImage("images/Characters/Player/Attack/Right2.png"), 100);

		playerAttack.addFrame(loadImage("images/Characters/Player/Attack/Right3.png"), 100);

		playerAttack.addFrame(loadImage("images/Characters/Player/Attack/Right4.png"), 100);

		playerAttack.addFrame(loadImage("images/Characters/Player/Attack/Right5.png"), 100);

		blueSpikeMove.addFrame(loadImage("images/Characters/Enemies/Enemy1/Right1.png"), 240);

		blueSpikeMove.addFrame(loadImage("images/Characters/Enemies/Enemy1/Right2.png"), 240);

		blueSpikeMove.addFrame(loadImage("images/Characters/Enemies/Enemy1/Right3.png"), 240);

		blueSpikeMove.addFrame(loadImage("images/Characters/Enemies/Enemy1/Right4.png"), 240);

		blueSpikeMove.addFrame(loadImage("images/Characters/Enemies/Enemy1/Right5.png"), 240);

		blueSpikeMove.addFrame(loadImage("images/Characters/Enemies/Enemy1/Right6.png"), 240);

		blueSpikeMove.setAnimationSpeed(1.0f);

		portalAnimation.addFrame(loadImage("images/WorldSprites/portal.gif"), 130);
		
		switchAnimation.addFrame(loadImage("images/WorldSprites/switchRight.png"), 130);

		switchAnimation.addFrame(loadImage("images/WorldSprites/switchLeft.png"), 130);
		
		switchAnimation.pauseAt(1);

	}

	/**
	 * This method is responsible for loading the game images. This is mainly for UI
	 * elements, such as gems, coins and the life & health counter.
	 */
	public void loadGameImages() {
		UICoin = loadImage("images/UI/coin_element.png");
		UIHeart = loadImage("images/UI/heart_element.png");
		UILife = loadImage("images/UI/life_element.png");
		UIDiamond = loadImage("images/UI/gem_element.png");
		UIBackground = loadImage("images/Backgrounds/title_screen.png");
		UIPlay = loadImage("images/UI/play_button.png");
		UIExit = loadImage("images/UI/exit_button.png");
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		switch (key) {
		case KeyEvent.VK_A:
			player.setMovingLeft(false);

			player.setVelocityX(0);
			break;
		case KeyEvent.VK_D:
			player.setMovingRight(false);

			player.setVelocityX(0);
			break;
		case KeyEvent.VK_SPACE:
			player.setJumping(false);
			break;
		default:
			break;
		}
	}

	/**
	 * Check and handles collisions with a tile map for the given sprite 's'.
	 * Initial functionality is limited...
	 *
	 * @param s    The Sprite to check collisions for
	 * @param tmap The tile map to check
	 */

	public void checkTileCollision(Sprite s, TileMap tmap) {

		// Take a note of a sprite's current position
		float sx = s.getX();

		float sy = s.getY();

		// Find out how wide and how tall a tile is
		float tileWidth = tmap.getTileWidth();

		float tileHeight = tmap.getTileHeight();

		// Retrieve coordinates, then the tile at that position
		int topLeftX = (int) (sx / tileWidth);

		int topLeftY = (int) (sy / tileHeight);

		Tile topLeft = tmap.getTile(topLeftX, topLeftY);

		int topRightX = (int) ((sx + s.getWidth()) / tileWidth);

		int topRightY = (int) (sy / tileHeight);

		Tile topRight = tmap.getTile(topRightX, topRightY);

		int bottomLeftX = (int) (sx / tileWidth);

		int bottomLeftY = (int) ((sy + s.getHeight()) / tileHeight);

		Tile bottomLeft = tmap.getTile(bottomLeftX, bottomLeftY);

		int bottomRightX = (int) ((sx + s.getWidth()) / tileWidth);

		int bottomRightY = (int) ((sy + s.getHeight()) / tileHeight);

		Tile bottomRight = tmap.getTile(bottomRightX, bottomRightY);

		// We do not want to check tiles if any of them could cause a null exception
		if (bottomRight == null || bottomLeft == null || topRight == null || topLeft == null) {
			return;
		}

		if (collisionManager.collisionAtBottom(bottomRight, s, tmap)
				|| collisionManager.collisionAtBottom(bottomLeft, s, tmap)) {

			boolean isCollectable = s.equals(player)
					&& (bottomLeft.getCharacter() == 'c' || bottomLeft.getCharacter() == 'v'
							|| bottomRight.getCharacter() == 'c' || bottomRight.getCharacter() == 'v');

			if (isCollectable) {
				if (player.isMovingLeft()) {
					pickUpItem(bottomRight);
				} else {
					pickUpItem(bottomLeft);
				}

			} else {
				s.setVelocityY(0);
				s.setY((bottomLeftY * tileHeight) - s.getHeight());
			}

		}

		if (collisionManager.collisionAtTop(topLeft, s, tmap) && collisionManager.collisionAtTop(topRight, s, tmap)) {
			s.setY(topRight.getYC() + tileWidth);
		}

		else if (collisionManager.collisionAtLeft(topLeft, s, tmap)) {

			boolean isCollectable = s.equals(player)
					&& (topLeft.getCharacter() == 'c' || topLeft.getCharacter() == 'v');

			if (isCollectable) {
				pickUpItem(topLeft);
			} else {
				s.setX(bottomLeft.getXC() + tileWidth);
			}

		}

		else if (collisionManager.collisionAtRight(topRight, s, tmap)) {
			boolean isCollectable = s.equals(player)
					&& (topRight.getCharacter() == 'c' || topRight.getCharacter() == 'v');

			if (isCollectable) {
				pickUpItem(topRight);
			} else {
				s.setVelocityX(-s.getVelocityX());

				s.setX((bottomRight.getXC() - tileWidth) - 1);
			}

		}

		// We will set some conditions which apply to all sprites, based on what they
		// have collided with.

		char topLeftCh = topLeft.getCharacter();

		char topRightCh = topRight.getCharacter();

		char bottomLeftCh = bottomLeft.getCharacter();

		char bottomRightCh = bottomRight.getCharacter();

		s.setOnGround(bottomLeftCh != '.' || bottomRightCh != '.');

		s.setOnDeathTile(topLeftCh == 's' || topRightCh == 's' || bottomRightCh == 's' || bottomLeftCh == 's');
	}

	/**
	 * This method is responsible for handling an collectable item being picked up
	 * by the player. This requires the tile that was set for collectable. When the
	 * collectable is picked up, the Tile is set to the airTile so it will vanish
	 * when "picked up".
	 * 
	 * @param collectableTile The tile that contains the collectable to be picked up
	 */
	public void pickUpItem(Tile collectableTile) {

		char collectableChar = collectableTile.getCharacter();

		// May be better to define a class for tiles being collected, as this will
		// likely be hard to maintain.
		Sound collectableSound = new Sound(collectableChar == 'c' ? "sounds/Coin1.wav" : "sounds/ding_1.wav");

		if (collectableChar == 'c') {
			collectableSound.start();
			coinCount++;
		} else if (collectableChar == 'v') {
			collectableSound.start();

			player.setDiamonds(player.getDiamonds() + 1);

		}

		collectableTile.setCharacter('.');

	}
	
	/**
	 * This method is responsible for placing the switches, according to
	 * the level that is being played.
	 */
	public void placeSwitches() {
		
		switchSprite.show();
		
		switch (currentLevel) {
		case LEVEL_1:
			switchSprite.setX(1278);
			switchSprite.setY(290);
			break;
		case LEVEL_2:
			switchSprite.setX(1248);
			switchSprite.setY(290);
			break;
		default:
			break;

		}
	}
	
	/**
	 * This method is responsible for placing the enemies
	 * according to the level that is being played.
	 */
	public void placeEnemies() {
		for (Sprite enemy : enemies) {
			enemy.setDead(false);
		}
		
		switch (currentLevel) {
		case LEVEL_1:
			enemies.get(0).setX(1032);

			enemies.get(0).setY(189);

			enemies.get(0).show();

			enemies.get(1).setX(1550);

			enemies.get(1).setY(221);
			break;
		case LEVEL_2:
			enemies.get(0).setX(1032);

			enemies.get(0).setY(189);

			enemies.get(0).show();
			break;
		default:
			break;

		}
	}
	
	/**
	 * This method is responsible for placing the player, 
	 * according to the level that is being played.
	 */
	public void placePlayer() {
		player.show();
		
		player.setVelocityX(0);

		player.setVelocityY(0);
		
		switch (currentLevel) {
		case LEVEL_1:
			startingX = 64;
			
			startingY = 138;
			
			player.setX(startingX);
			
			player.setY(startingY);
		
			break;
		case LEVEL_2:
			startingX = 81;
			
			startingY = 215;
			
			player.setX(startingX);
			
			player.setY(startingY);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();

		int mouseY = e.getY();
		
		// Get the bounds of the buttons, I.E where the user has to click

		boolean inExitBtnBounds = mouseX >= MENU_EXIT_BTN_X && mouseX <= MENU_EXIT_BTN_X + UIExit.getWidth(null)
				&& mouseY >= MENU_EXIT_BTN_Y && mouseY <= MENU_EXIT_BTN_Y + UIExit.getHeight(null);

		boolean inPlayBtnBounds = mouseX >= MENU_PLAY_BTN_X && mouseX <= MENU_PLAY_BTN_X + UIPlay.getWidth(null)
				&& mouseY >= MENU_PLAY_BTN_Y && mouseY <= MENU_PLAY_BTN_Y + UIPlay.getHeight(null);

		if (inExitBtnBounds) {
			stop();
		} else if (inPlayBtnBounds) {
			currentStatus = GameStatus.GAME_STARTED;
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
        // Mouse pressed included as part of MouseListener interface, not yet used.
    }

	@Override
	public void mouseReleased(MouseEvent e) {
		// Mouse released included as part of MouseListener interface, not yet used.

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Mouse entered included as part of MouseListener interface, not yet used.
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Mouse exited included as part of MouseListener interface, not yet used.
	}
}
