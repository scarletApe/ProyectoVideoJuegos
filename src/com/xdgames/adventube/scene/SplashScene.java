package com.xdgames.adventube.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;

import com.xdgames.adventube.base.BaseScene;
import com.xdgames.adventube.constants.GameConstants;
import com.xdgames.adventube.manager.SceneManager.SceneType;

public class SplashScene extends BaseScene {

	private Sprite splash;

	@Override
	public void disposeScene() {
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
		this.dispose();
	}

	@Override
	public void createScene() {
		ITextureRegion tr = resourcesManager.xd_games_region;
		
		splash = new Sprite(0, 0,GameConstants.CAMERA_WIDTH, GameConstants.CAMERA_HEIGHT, tr, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		attachChild(splash);

	}

	@Override
	public void onBackKeyPressed() {
		return;
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SPLASH;
	}
}
