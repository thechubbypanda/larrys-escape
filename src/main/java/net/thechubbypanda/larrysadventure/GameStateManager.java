//package net.thechubbypanda.larrysadventure;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.audio.Music;
//import com.badlogic.gdx.graphics.Cursor.SystemCursor;
//import net.thechubbypanda.larrysadventure.old.entities.Player;
//import net.thechubbypanda.larrysadventure.states.*;
//import net.thechubbypanda.larrysadventure.states.GameState.StateType;
//import net.thechubbypanda.larrysadventure.old.levels.Maze;
//
//import java.util.Stack;
//
//import static net.thechubbypanda.larrysadventure.Constants.SOUND;
//
//public class GameStateManager {
//
//	// The current level
//	public static int level = 0;
//
//	// Stores the states that are currently in use
//	private Stack<GameState> stateStack;
//
//	// Stores the id of the current state
//	private short currentState;
//
//	// Stores the state in which the game starts
//	private short startingState;
//
//	// True if a state is loading
//	private boolean loading = false;
//
//	// True if the game is paused
//	public static boolean paused = false;
//
//	// Time of last state change to or from the pause state
//	private long lastTime;
//
//	// Music files
//	private Music menuMusic, gameMusic;
//
//	// The type of the last state
//	private StateType lastType = StateType.menu;
//
//	public GameStateManager(short startingState) {
//
//		// Create a stack to store the loaded states
//		// Using a stack means that the last state pushed is the on that is rendered
//		// Especially helpful with the pause menu.
//		stateStack = new Stack<>();
//
//		// Set the starting state
//		this.startingState = startingState;
//		pushState(startingState);
//
//		// Set lastTime to the current time
//		lastTime = System.currentTimeMillis();
//
//		// Setup the music and start playing
//		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.mp3"));
//		menuMusic.setLooping(true);
//		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/game.mp3"));
//		gameMusic.setLooping(true);
//
//		menuMusic.setVolume(0);
//		menuMusic.play();
//
//		// Setting the game cursor
//		Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
//	}
//
//	public void update() {
//		// Listen for escape key and toggle pause state
//		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
//			if (currentState != GameState.MENU && currentState != GameState.GAMEOVER) {
//				// Delay ensures cannot click multiple times in a row
//				if (System.currentTimeMillis() > lastTime + 300) {
//					togglePause();
//					lastTime = System.currentTimeMillis();
//				}
//			}
//		}
//		if (!loading) {
//			// Update the top state on the stack
//			//stateStack.peek().update();
//		}
//
//		// Checking whether the user wants sound on or not and adjusting volume
//		// accordingly
//		if (SOUND) {
//			menuMusic.setVolume(0.6f);
//			gameMusic.setVolume(0.3f);
//		} else {
//			menuMusic.setVolume(0);
//			gameMusic.setVolume(0);
//		}
//	}
//
//	public void render() {
//
//	}
//
//	// Resets the game to the start
//	public void reset() {
//		level = 0;
//		setState(startingState);
//		Player.reset();
//	}
//
//	// Pauses and unpauses the game
//	public void togglePause() {
//		if (!paused) {
//			pushState(GameState.PAUSE);
//			paused = true;
//		} else {
//			paused = false;
//		}
//	}
//
//	// Gets a new instance of the requested state
//	private GameState getState(short state) {
//		if (state == GameState.MAZE) {
//			level++;
//			return new Maze(this, level);
//		}
//		return null;
//	}
//
//	// Sets a new state
//	public void setState(short state) {
//		loading = true;
//		pushState(state);
//		loading = false;
//	}
//
//	// Pushes a new state to the stack
//	private void pushState(short state) {
//		if (state != GameState.PAUSE) {
//			currentState = state;
//		}
//		stateStack.push(getState(state));
//	}
//
//	public void dispose() {
//		for (GameState gameState : stateStack) {
//		}
//		stateStack = null;
//		menuMusic.dispose();
//		gameMusic.dispose();
//	}
//}
