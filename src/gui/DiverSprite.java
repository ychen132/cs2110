package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JPanel;

import graph.Node;
import graph.Sewers;
import graph.Sewers.Direction;

/** Responsible for managing the sewer diver and drawing it on the screen. <br>
 * Handles functions to update the sewer diver and update its drawing as well. */
public class DiverSprite extends JPanel {
	private static final long serialVersionUID= 1L;
	/** The GUI that created this DiverSprite */
	private GUI gui;

	/** Sprite class to handle animating the diver */
	private Sprite sprite;

	/** Width and height (in pixels) of a single diver image on the spritesheet */
	private int SPRITE_WIDTH= 29, SPRITE_HEIGHT= 36;

	/** SewerDiver's row and column indexes (updated only once move completes) */
	private int row, col;

	/** x- and y- coordinates (pixels) */
	private int posX, posY;

	/** List of moves we need to make to get to the goal location */
	private BlockingQueue<MovePair> queuedMoves;

	/** Direction the diver is currently facing? */
	private Sewers.Direction dir= Direction.NORTH;

	/** Allow our moveTo to block until complete */
	private Semaphore blockUntilDone;

	/** Threads that update the diver's location and animation */
	private Thread updateThread, animationUpdateThread;

	/** Number of animation frames displayed per second */
	private double ANIMATION_FPS= 10;

	/** Location of the spritesheet image */
	private String spriteSheet= "res/explorer_sprites.png";

	/** Constructor: an instance starting at (startRow, startCol), created for gui. */
	public DiverSprite(int startRow, int startCol, GUI gui) {
		this.gui= gui;
		// Initialize fields
		sprite= new Sprite(spriteSheet, SPRITE_WIDTH, SPRITE_HEIGHT, 3);
		if (sprite == null) {
			System.out.println("new sprite is: " + sprite);
			System.exit(0);
		}
		queuedMoves= new SynchronousQueue<>();
		blockUntilDone= new Semaphore(0);

		// Initialize the starting location
		row= startRow;
		col= startCol;
		posX= row * MazePanel.TILE_WIDTH;
		posY= col * MazePanel.TILE_HEIGHT;
		// Create a thread that will periodically update the diver's position
		updateThread= new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						int frames= GUI.FRAMES_PER_MOVE;
						MovePair move= queuedMoves.take();
						// Move to the goal
						for (int i= 1; i <= frames; i++ ) {
							long startTime= System.currentTimeMillis();
							// Get the next move to make
							update(frames, i, move);
							long lagTime= System.currentTimeMillis() - startTime;
							if (lagTime < 1000 / GUI.FRAMES_PER_SECOND) {
								Thread.sleep(1000 / GUI.FRAMES_PER_SECOND - lagTime);
							}
						}
						blockUntilDone.release();

					} catch (InterruptedException e) {
						return;
					}
				}
			}
		});

		updateThread.start();

		// Create a thread that will periodically update the diver's animation
		animationUpdateThread= new Thread(new Runnable() {
			public @Override void run() {
				while (true) {
					try {
						long startTime= System.currentTimeMillis();
						if (gui.gameState.fleeSucceeded()) { return; }

						sprite.tick();
						long lagTime= System.currentTimeMillis() - startTime;
						if (lagTime < 1000 / ANIMATION_FPS) {
							Thread.sleep((long) (1000 / ANIMATION_FPS - lagTime));
						}
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		});

		animationUpdateThread.start();
	}

	/** Return the image representing the current state of the diver. */
	public BufferedImage sprite() {
		/* Use the direction to determine which offset into the
		 * spritesheet to use. The Sprite class handles animation. */
		if (gui.gameState.fleeSucceeded()) {
			System.out.println("In DiverSprite.sprite. painting exitEnd");
			return gui.mazePanel.exitEnd;
		}
		switch (dir) {
		case NORTH:
			return sprite.getSprite(0, 0);
		case SOUTH:
			return sprite.getSprite(0, 3);
		case WEST:
			return sprite.getSprite(1, 0);
		case EAST:
			return sprite.getSprite(1, 3);
		default:
			return sprite.getSprite(0, 0);
		}
	}

	/** Return the diver's row on the grid. Will remain the diver's<br>
	 * old position until the diver has completely arrived at the new one. */
	public int row() {
		return row;
	}

	/** Return the diver's column on the grid. Will remain the diver's <br>
	 * old position until the diver has completely arrived at the new one. */
	public int col() {
		return col;
	}

	/** Tell the diver to move from its current location to node dst.<br>
	 * After making move, calling thread will block until the move completes on GUI.<br>
	 * Precondition: dst must be adjacent to the current location and not currently moving.<br>
	 *
	 * @throws InterruptedException */
	public void moveTo(Node dst) throws InterruptedException {
		dir= getDirection(row, col, dst.getTile().row(), dst.getTile().column());
		// Determine sequence of moves to add to queue to get to goal
		int xDiff= (dst.getTile().column() - col) * MazePanel.TILE_WIDTH;
		int yDiff= (dst.getTile().row() - row) * MazePanel.TILE_HEIGHT;
		queuedMoves.put(new MovePair(xDiff, yDiff));

		blockUntilDone.acquire();
		row= dst.getTile().row();
		col= dst.getTile().column();
	}

	/** Draw the diver on its own panel. */
	@Override
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		if (gui.gameState.fleeSucceeded()) {
			page.drawImage(gui.mazePanel.exitEnd, MazePanel.TILE_WIDTH * col,
				MazePanel.TILE_HEIGHT * row,
				MazePanel.TILE_WIDTH, MazePanel.TILE_HEIGHT, null);
			return;
		}
		page.drawImage(sprite(), posX, posY, MazePanel.TILE_WIDTH, MazePanel.TILE_HEIGHT, null);
	}

	/** Update the location of the diver as necessary. */
	private void update(int framesPerMove, int framesIntoMove, MovePair move) {
		// Make the move toward our destination
		posX= MazePanel.TILE_WIDTH * col() + framesIntoMove * move.xDiff / framesPerMove;
		posY= MazePanel.TILE_HEIGHT * row() + framesIntoMove * move.yDiff / framesPerMove;
		repaint();
	}

	/** Return the direction from current location (row, col) to (goalRow, goalCol).<br>
	 * If already there, return the current direction. */
	private Direction getDirection(int row, int col, int goalRow, int goalCol) {
		if (goalRow < row) return Direction.NORTH;
		if (goalRow > row) return Direction.SOUTH;
		if (goalCol < col) return Direction.WEST;
		if (goalCol > col) return Direction.EAST;
		return dir;
	}

	/** Contains information that uniquely represents a move that can be made. */
	private class MovePair {
		public final int xDiff; // x-change to new position
		public final int yDiff; // y-change to new position

		/** Constructor: an instance with change (xChange, yChange). */
		public MovePair(int xChange, int yChange) {
			xDiff= xChange;
			yDiff= yChange;
		}
	}
}
