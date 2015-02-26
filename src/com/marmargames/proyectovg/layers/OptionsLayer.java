package com.marmargames.proyectovg.layers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import com.marmargames.proyectovg.managers.ResourceManager;
import com.marmargames.proyectovg.managers.SceneManager;

public class OptionsLayer extends ManagedLayer {
	private static final OptionsLayer INSTANCE = new OptionsLayer();

	public static OptionsLayer getInstance() {
		return INSTANCE;
	}

	// Animates the layer to slide in from the top.
	IUpdateHandler SlideIn = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if (OptionsLayer.getInstance().getY() > ResourceManager
					.getInstance().cameraHeight) {
				OptionsLayer.getInstance().setPosition(
						OptionsLayer.getInstance().getX(),
						Math.max(OptionsLayer.getInstance().getY()
								- (3600 * (pSecondsElapsed)),
								ResourceManager.getInstance().cameraHeight));
			} else {
				OptionsLayer.getInstance().unregisterUpdateHandler(this);
			}
		}

		@Override
		public void reset() {
		}
	};

	// Animates the layer to slide out through the top and tell the SceneManager
	// to hide it when it is off-screen;
	IUpdateHandler SlideOut = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			if (OptionsLayer.getInstance().getY() < ResourceManager
					.getInstance().cameraHeight / 2f + 480f) {
				OptionsLayer
						.getInstance()
						.setPosition(
								OptionsLayer.getInstance().getX(),
								Math.min(
										OptionsLayer.getInstance().getY()
												+ (3600 * (pSecondsElapsed)),
										ResourceManager.getInstance().cameraHeight / 2f + 480f));
			} else {
				OptionsLayer.getInstance().unregisterUpdateHandler(this);
				SceneManager.getInstance().hideLayer();
			}
		}

		@Override
		public void reset() {
		}
	};

	// /* This entity modifier is defined as the 'transition-in' modifier
	// * which will move an Entity/screen into the camera-view */
	// private final ParallelEntityModifier mMoveInModifier = new
	// ParallelEntityModifier(
	// new MoveXModifier(3, 800, 0), new RotationModifier(3, 0, 360),
	// new ScaleModifier(3, 0, 1));
	//
	// /* This entity modifier is defined as the 'transition-out' modifier
	// * which will move an Entity/screen out of the camera-view */
	// private final ParallelEntityModifier mMoveOutModifier = new
	// ParallelEntityModifier(
	// new MoveXModifier(3, 0, -480), new RotationModifier(3, 360, 0),
	// new ScaleModifier(3, 1, 0));

	@Override
	public void onLoadLayer() {
		// Create and attach a background that hides the Layer when touched.
		final float backgroundWidth = 760f, backgroundHeight = 440f;
		final float backgroundX = (800 / 2) - (760 / 2), backgroundY = (480 / 2)
				- (440 / 2);

		Rectangle smth = new Rectangle(backgroundX, backgroundY,
				backgroundWidth, backgroundHeight,
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager()) {
//			@Override
//			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
//					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
//				if (pSceneTouchEvent.isActionUp()
//						&& pTouchAreaLocalX < this.getWidth()
//						&& pTouchAreaLocalX > 0
//						&& pTouchAreaLocalY < this.getHeight()
//						&& pTouchAreaLocalY > 0) {
//					ResourceManager.clickSound.play();
//					onHideLayer();
//				}
//				return true;
//			}
		};
		smth.setColor(0f, 0f, 0f, 0.85f);
		this.attachChild(smth);
//		this.registerTouchArea(smth);

		// Create the OptionsLayerTitle text for the Layer.
		Text OptionsLayerTitle = new Text(0, 0,
				ResourceManager.fontDefault32BoldWhite, "OPTIONS",
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		OptionsLayerTitle.setPosition(
				800 / 2 - OptionsLayerTitle.getWidth() / 2, 400);
		this.attachChild(OptionsLayerTitle);

		// Let the player know how to get out of the blank Options Layer
//		Text OptionsLayerSubTitle = new Text(0, 0,
//				ResourceManager.fontDefault32Bold, "Tap to return",
//				ResourceManager.getInstance().engine
//						.getVertexBufferObjectManager());
//		OptionsLayerSubTitle.setScale(0.75f);
//		OptionsLayerSubTitle.setPosition(0f, 0);
//		this.attachChild(OptionsLayerSubTitle);

		// Setup the HUD Buttons and Button Texts.
		// Take note of what happens when the buttons are clicked.
		ButtonSprite MainMenuButton = new ButtonSprite(0f, 0f,
				ResourceManager.buttonTiledTextureRegion.getTextureRegion(0),
				ResourceManager.buttonTiledTextureRegion.getTextureRegion(1),
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		MainMenuButton.setScale(
				1 / ResourceManager.getInstance().cameraScaleFactorX,
				1 / ResourceManager.getInstance().cameraScaleFactorY);
		MainMenuButton.setPosition(ResourceManager.getInstance().cameraWidth/2- MainMenuButton.getWidth()/2, 
				200);
		MainMenuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// Play the click sound and show the Main Menu.
				ResourceManager.clickSound.play();
				SceneManager.getInstance().showMainMenu();
			}
		});

		Text MainMenuButtonText = new Text(10, 10,
				ResourceManager.fontDefault32Bold, "MENU",
				ResourceManager.getInstance().engine
						.getVertexBufferObjectManager());
		MainMenuButton.attachChild(MainMenuButtonText);
		this.attachChild(MainMenuButton);
		this.registerTouchArea(MainMenuButton);

//		ButtonSprite OptionsButton = new ButtonSprite(0f, 0f,
//				ResourceManager.buttonTiledTextureRegion.getTextureRegion(0),
//				ResourceManager.buttonTiledTextureRegion.getTextureRegion(1),
//				ResourceManager.getInstance().engine
//						.getVertexBufferObjectManager());
//		OptionsButton.setScale(
//				1 / ResourceManager.getInstance().cameraScaleFactorX,
//				1 / ResourceManager.getInstance().cameraScaleFactorY);
//		OptionsButton.setPosition(ResourceManager.getInstance().cameraWidth/2- OptionsButton.getWidth()/2, 300);
//		OptionsButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(ButtonSprite pButtonSprite,
//					float pTouchAreaLocalX, float pTouchAreaLocalY) {
//				// Play the click sound and show the Options Layer.
//				ResourceManager.clickSound.play();
//				SceneManager.getInstance().showOptionsLayer(true);
//			}
//		});
//
//		Text OptionsButtonText = new Text(0, 0,
//				ResourceManager.fontDefault32Bold, "OPTIONS",
//				ResourceManager.getInstance().engine
//						.getVertexBufferObjectManager());
//		OptionsButtonText.setPosition(10, 10);
//		OptionsButton.attachChild(OptionsButtonText);
//		this.attachChild(OptionsButton);
//		this.registerTouchArea(OptionsButton);

		this.setPosition(0, 0);
	}

	@Override
	public void onShowLayer() {
		// this.registerUpdateHandler(SlideIn);
	}

	@Override
	public void onHideLayer() {
		// this.registerUpdateHandler(SlideOut);
		OptionsLayer.getInstance().unregisterUpdateHandler(this);
		SceneManager.getInstance().hideLayer();
	}

	@Override
	public void onUnloadLayer() {
	}
}