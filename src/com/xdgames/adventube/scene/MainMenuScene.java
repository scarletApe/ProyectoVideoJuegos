package com.xdgames.adventube.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.xdgames.adventube.base.BaseScene;
import com.xdgames.adventube.constants.GameConstants;
import com.xdgames.adventube.manager.SceneManager;
import com.xdgames.adventube.manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements
		IOnMenuItemClickListener {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private MenuScene menuChildScene;

	private final int MENU_PLAY = 0;

	// ---------------------------------------------
	// METHODS FROM SUPERCLASS
	// ---------------------------------------------

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
	}

	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_PLAY:
			// Load Game Scene!
//			SceneManager.getInstance().loadGameScene(engine);
			SceneManager.getInstance().loadLevelSelector(engine);
			return true;
		default:
			return false;
		}
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createBackground() {

		attachChild(new Sprite(0, 0, GameConstants.CAMERA_WIDTH,
				GameConstants.CAMERA_HEIGHT,
				resourcesManager.menu_background_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}

	private void createMenuChildScene() {
		menuChildScene = new MenuScene(camera);
		// menuChildScene.setPosition(0, 0);

		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(
				new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region,
						vbom), 1.2f, 1);

//		menuChildScene.setPosition(0, 0);
		menuChildScene.addMenuItem(playMenuItem);

		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		playMenuItem.setPosition(playMenuItem.getX()+5, playMenuItem.getY()+50);

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);
	}
}