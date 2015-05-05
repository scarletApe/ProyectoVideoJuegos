package com.marmar.games.marioex.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.marmar.games.marioex.base.BaseScene;
import com.marmar.games.marioex.constants.GameConstants;
import com.marmar.games.marioex.manager.ResourcesManager;
import com.marmar.games.marioex.manager.SceneManager;
import com.marmar.games.marioex.manager.SceneManager.SceneType;

public class LevelSelectorScene extends BaseScene {

	private LevelItem levelItems[];

	@Override
	public void createScene() {
		ResourcesManager.getInstance().loadGameResources();
		// TODO change the background image
		attachChild(new Sprite(0, 0, GameConstants.CAMERA_WIDTH,
				GameConstants.CAMERA_HEIGHT,
				resourcesManager.menu_background_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});

		levelItems = new LevelItem[6];

		// for(int i=0; i<levelItems.length;i++){
		// levelItems = new LevelItem(pX, pY, pWidth, pHeight, pTextureRegion,
		// pSpriteVertexBufferObject)
		// }

		float tx = 0;
		float ty = 0;
		levelItems[0] = new LevelItem( tx,  ty, 32f, 32f,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "Forest", 0);

		levelItems[1] = new LevelItem( tx+ 32+10,  ty, 32f, 32f,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "Desert", 1);

		levelItems[2] = new LevelItem( tx,  ty+32+10, 32f, 32f,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "Swamp", 2);

		levelItems[3] = new LevelItem( tx+32+10,  ty+32+10, 32f, 32f,
				ResourcesManager.getInstance().coin_region,
				engine.getVertexBufferObjectManager(), false, 0, "CandyLand", 3);

		// dont forget to attach them
		/* Register & Attach the levelTile object to the LevelSelector */
		// mScene.registerTouchArea(levelTile);
		for (int i = 0; i < 4; i++) {
			this.registerTouchArea(levelItems[i]);
			this.attachChild(levelItems[i]);
		}
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
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

class LevelItem extends Sprite {

	private boolean isPassed;
	private int score;
	private String text;
	private int levelNumber;
	private Text mTileText;

	public LevelItem(float pX, float pY, float pWidth, float pHeight,
			ITextureRegion pTextureRegion, VertexBufferObjectManager pVBOM,
			boolean isPassed, int score, String text, int levelNum) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pVBOM);
		// TODO Auto-generated constructor stub

		this.isPassed = isPassed;
		this.score = score;
		this.text = text;
		this.levelNumber = levelNum;

		changeText();
	}

	public void changeText() {
		int textPositionX = 0;
		int textPositionY = 0;
		String tempText = text + " " + score;
		/* Create the tile's text in the center of the tile */
		this.mTileText = new Text(textPositionX, textPositionY,
				ResourcesManager.getInstance().font, tempText,
				tempText.length(),
				ResourcesManager.getInstance().engine
						.getVertexBufferObjectManager());

		/* Attach the Text to the LevelTile */
		this.attachChild(mTileText);
	}

	public boolean isPassed() {
		return isPassed;
	}

	public void setPassed(boolean isPassed) {
		this.isPassed = isPassed;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		/* If a level tile is initially pressed down on */
		if (pSceneTouchEvent.isActionDown()) {

			/**
			 * Example level loading: LevelSelector.this.hide();
			 * SceneManager.loadLevel(this.mLevelNumber);
			 */
			switch (levelNumber) {
			case 0: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "test5.tmx");
				break;
			case 1: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "miTest.tmx");
				break;
			case 2: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "MarioTest.tmx");
				break;
			case 3: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "test5.tmx");
				break;
			default: SceneManager.getInstance().loadGameScene(ResourcesManager.getInstance().engine, "test5.tmx");
				break;
			}

			return true;
		}

		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
				pTouchAreaLocalY);
	}

}
