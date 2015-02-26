package com.marmargames.proyectovg;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;

import com.marmargames.proyectovg.GameLevels.elements.ManagedGameScene;
import com.marmargames.proyectovg.managers.ResourceManager;
import com.marmargames.proyectovg.managers.SceneManager;
import com.marmargames.proyectovg.menus.MainMenu;
import com.marmargames.proyectovg.menus.ManagedMenuScene;

public class ApplyingSceneManager extends BaseGameActivity {

	// ====================================================
	// CONSTANTS
	// ====================================================
	// We define these constants to setup the game to use an
	// appropriate camera resolution independent of the actual
	// end-user's screen resolution.

	// The resolution of the screen with which you are developing.
	static float DESIGN_SCREEN_WIDTH_PIXELS = 800f;
	static float DESIGN_SCREEN_HEIGHT_PIXELS = 480f;
	// The physical size of the screen with which you are developing.
	static float DESIGN_SCREEN_WIDTH_INCHES = 4.472441f;
	static float DESIGN_SCREEN_HEIGHT_INCHES = 2.805118f;
	// Define a minimum and maximum screen resolution (to prevent
	// cramped or overlapping screen elements).
	static float MIN_WIDTH_PIXELS = 320f, MIN_HEIGHT_PIXELS = 240f;
	static float MAX_WIDTH_PIXELS = 1600f, MAX_HEIGHT_PIXELS = 960f;

	// ====================================================
	// VARIABLES
	// ====================================================
	// These variables will be set in onCreateEngineOptions().
	public float cameraWidth;
	public float cameraHeight;
	public float actualScreenWidthInches;
	public float actualScreenHeightInches;
	private BoundCamera camera;
	
	private Scene splashScene;
	private BitmapTextureAtlas splashTextureAtlas;
    private ITextureRegion splashTextureRegion;
    private Sprite splash;

	// If a Layer is open when the Back button is pressed, hide the layer.
	// If a Game scene or non-MainMenu is active, go back to the MainMenu.
	// Otherwise, exit the game.
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (ResourceManager.getInstance().engine != null) {
				if (SceneManager.getInstance().isLayerShown)
					SceneManager.getInstance().currentLayer.onHideLayer();
				else if (SceneManager.getInstance().mCurrentScene.getClass()
						.getGenericSuperclass().equals(ManagedGameScene.class)
						|| (SceneManager.getInstance().mCurrentScene.getClass()
								.getGenericSuperclass()
								.equals(ManagedMenuScene.class) & !SceneManager
								.getInstance().mCurrentScene.getClass().equals(
								MainMenu.class)))
					SceneManager.getInstance().showMainMenu();
				else
					System.exit(0);
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	// ====================================================
	// CREATE ENGINE OPTIONS
	// ====================================================
	@Override
	public EngineOptions onCreateEngineOptions() {
		// Determine the device's physical screen size.
		actualScreenWidthInches = getResources().getDisplayMetrics().widthPixels
				/ getResources().getDisplayMetrics().xdpi;
		actualScreenHeightInches = getResources().getDisplayMetrics().heightPixels
				/ getResources().getDisplayMetrics().ydpi;
		// Set the Camera's Width & Height according to the device with which
		// you design the game.
		cameraWidth = 800;// Math.round(Math.max(Math.min(DESIGN_SCREEN_WIDTH_PIXELS
							// * (actualScreenWidthInches /
							// DESIGN_SCREEN_WIDTH_INCHES),MAX_WIDTH_PIXELS),MIN_WIDTH_PIXELS));
		cameraHeight = 480;// Math.round(Math.max(Math.min(DESIGN_SCREEN_HEIGHT_PIXELS
							// * (actualScreenHeightInches /
							// DESIGN_SCREEN_HEIGHT_INCHES),MAX_HEIGHT_PIXELS),MIN_HEIGHT_PIXELS));
		camera = new BoundCamera(0, 0, cameraWidth, cameraHeight);
		// Create the EngineOptions.
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(),
				camera);
		// Enable sounds.
		engineOptions.getAudioOptions().setNeedsSound(true);
		// Enable music.
		engineOptions.getAudioOptions().setNeedsMusic(true);
		// Turn on Dithering to smooth texture gradients.
		engineOptions.getRenderOptions().setDithering(true);
		// Turn on MultiSampling to smooth the alias of hard-edge elements.
		// engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		// Set the Wake Lock options to prevent the engine from dumping textures
		// when focus changes.
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	// ====================================================
	// CREATE RESOURCES
	// ====================================================
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		// Setup the ResourceManager.
		ResourceManager.getInstance().setup(this.getEngine(),
				this.getApplicationContext(), cameraWidth, cameraHeight,
				cameraWidth / DESIGN_SCREEN_WIDTH_PIXELS,
				cameraHeight / DESIGN_SCREEN_HEIGHT_PIXELS, camera);
		
//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//        splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 300, 200, TextureOptions.DEFAULT);
//        splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, this, "cloud.png", 0, 0);
//        splashTextureAtlas.load();
        
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// ====================================================
	// CREATE SCENE
	// ====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		// Register an FPSLogger to output the game's FPS during development.
		mEngine.registerUpdateHandler(new FPSLogger());
		
//		initSplashScene();
		
		// Tell the SceneManager to show the MainMenu.
		SceneManager.getInstance().showMainMenu();
		// Set the MainMenu to the Engine's scene.
		pOnCreateSceneCallback.onCreateSceneFinished(MainMenu.getInstance());
	}

	// ====================================================
	// POPULATE SCENE
	// ====================================================
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		// Our SceneManager will handle the population of the scenes, so we do
		// nothing here.
//		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() 
//		{
//            public void onTimePassed(final TimerHandler pTimerHandler) 
//            {
//                mEngine.unregisterUpdateHandler(pTimerHandler);
//        
//                splash.detachSelf();
//             // Tell the SceneManager to show the MainMenu.
//        		SceneManager.getInstance().showMainMenu();
////                mEngine.setScene(mainScene);
////                currentScene = SceneType.MAIN;
//            }
//		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
//	private void initSplashScene()
//	{
//    	splashScene = new Scene();
//    	splash = new Sprite(0, 0, splashTextureRegion, mEngine.getVertexBufferObjectManager())
//    	{
//    		@Override
//            protected void preDraw(GLState pGLState, Camera pCamera) 
//    		{
//                super.preDraw(pGLState, pCamera);
//                pGLState.enableDither();
//            }
//    	};
//    	
////    	splash.setScale(1.5f);
//    	splash.setPosition((cameraWidth - splash.getWidth()) * 0.5f, (cameraHeight - splash.getHeight()) * 0.5f);
//    	splashScene.attachChild(splash);
//    	this.getEngine().setScene(splashScene);
//	}
}