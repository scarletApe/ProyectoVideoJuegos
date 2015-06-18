package com.xdgames.adventube.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.xdgames.adventube.base.BaseScene;
import com.xdgames.adventube.scene.GameScene;
import com.xdgames.adventube.scene.LevelPassedScene;
import com.xdgames.adventube.scene.LevelSelectorScene;
import com.xdgames.adventube.scene.LoadingScene;
import com.xdgames.adventube.scene.MainMenuScene;
import com.xdgames.adventube.scene.SplashScene;

/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class SceneManager {
	// ---------------------------------------------
	// SCENES
	// ---------------------------------------------

	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene loadingScene;
	private BaseScene levelSelector;
	private BaseScene levelPassed;

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final SceneManager INSTANCE = new SceneManager();

	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private BaseScene currentScene;

	private Engine engine = ResourcesManager.getInstance().engine;
	
	///
	private boolean gameResourcesLoaded = false;

	public enum SceneType {
		SCENE_SPLASH, SCENE_MENU, SCENE_GAME, SCENE_LOADING, SCENE_LEVEL_SELECTOR,
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	public void setScene(SceneType sceneType) {
		switch (sceneType) {
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_GAME:
			setScene(gameScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		default:
			break;
		}
	}

	public void createMenuScene() {
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		SceneManager.getInstance().setScene(menuScene);
		disposeSplashScene();
	}

	public BaseScene createSplashScene() {
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		return splashScene;
	}

	private void disposeSplashScene() {
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}

	public void loadGameScene(final Engine mEngine, final String levelName) {
		currentScene.disposeScene();
		setScene(loadingScene);
//		System.out.println(levelName+" in method");
		// ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						GameScene.levelName = levelName;
						if(!gameResourcesLoaded){
//						ResourcesManager.getInstance().loadGameResources();
//						gameResourcesLoaded = true;
						}
//						System.out.println(levelName+" in updatehandeler");
						gameScene = new GameScene(levelName);
						setScene(gameScene);
					}
				}));
	}

	public void loadLevelSelector(final Engine mEngine) {
		//setScene(loadingScene);
		currentScene.disposeScene();
		setScene(loadingScene);
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						if (levelSelector == null) {
							ResourcesManager.getInstance()
									.loadAllOtherResources();
							ResourcesManager.getInstance().loadGameResources();
							
							levelSelector = new LevelSelectorScene();
						}
						setScene(levelSelector);
					}
				}));
	}

	public void loadMenuScene(final Engine mEngine) {
		currentScene.disposeScene();
		// ResourcesManager.getInstance().unloadGameTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						// ResourcesManager.getInstance().loadMenuTextures();
						setScene(menuScene);
					}
				}));
	}
	
	public void loadLevelPassedScene(final Engine mEngine, final int score) {
		currentScene.disposeScene();
		setScene(loadingScene);
		// ResourcesManager.getInstance().unloadGameTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						// ResourcesManager.getInstance().loadMenuTextures();
						LevelPassedScene.score = score;
						levelPassed = new LevelPassedScene();
						setScene(levelPassed);
					}
				}));
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}

	public BaseScene getCurrentScene() {
		return currentScene;
	}
}