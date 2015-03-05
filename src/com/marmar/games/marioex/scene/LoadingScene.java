package com.marmar.games.marioex.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.marmar.games.marioex.base.BaseScene;
import com.marmar.games.marioex.constants.GameConstants;
import com.marmar.games.marioex.manager.SceneManager.SceneType;

public class LoadingScene extends BaseScene {
	@Override
	public void createScene() {
		setBackground(new Background(Color.BLACK));
		Text text = new Text(0, 0, resourcesManager.font, "Loading...", vbom);
		text.setPosition(GameConstants.CAMERA_WIDTH / 2 - text.getWidth() / 2,
				GameConstants.CAMERA_HEIGHT / 2 - text.getHeight() / 2);
		attachChild(text);
	}

	@Override
	public void onBackKeyPressed() {
		return;
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {

	}
}