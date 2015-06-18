package com.xdgames.adventube.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import com.xdgames.adventube.base.BaseScene;
import com.xdgames.adventube.constants.GameConstants;
import com.xdgames.adventube.manager.ResourcesManager;
import com.xdgames.adventube.manager.SceneManager;
import com.xdgames.adventube.manager.SceneManager.SceneType;

public class LevelPassedScene extends BaseScene {

	public static int score;
	private Text mTileText;

	@Override
	public void createScene() {
		// change the background image
		attachChild(new Sprite(0, 0, GameConstants.CAMERA_WIDTH,
				GameConstants.CAMERA_HEIGHT,
				resourcesManager.level_passed_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});

		int textPositionX = 190;
		int textPositionY = 82;
		String tempText = score + "";// + " " + score;
		/* Create the tile's text in the center of the tile */
		this.mTileText = new Text(
				textPositionX, textPositionY,
				ResourcesManager.getInstance().font_20px,
				tempText,
				tempText.length(),
				ResourcesManager.getInstance().engine.getVertexBufferObjectManager());
		mTileText.setColor(Color.CYAN);

		/* Attach the Text to the LevelTile */
		this.attachChild(mTileText);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadLevelSelector(engine);

	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LEVEL_SELECTOR;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}
}
