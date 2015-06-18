package com.xdgames.adventube.scene;

import org.andengine.audio.music.Music;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.xdgames.adventube.base.BaseScene;
import com.xdgames.adventube.constants.Action;
import com.xdgames.adventube.constants.EnemySensors;
import com.xdgames.adventube.constants.GameConstants;
import com.xdgames.adventube.dynamics.Bullet;
import com.xdgames.adventube.dynamics.Enemy;
import com.xdgames.adventube.dynamics.Player;
import com.xdgames.adventube.manager.ResourcesManager;
import com.xdgames.adventube.manager.SceneManager;
import com.xdgames.adventube.manager.SceneManager.SceneType;

public class GameScene extends BaseScene {

	private Player player;
	private TMXTiledMap mTMXTiledMap;
	private HUD gameHUD;
	private PhysicsWorld physicsWorld;
	private boolean stopManagedUpdate = false;
	public static String levelName;
	private Music musica;

	private int score = 0;
	private Text scoreText;

	public GameScene(String levelName) {
		GameScene.levelName = levelName;
		System.out.println(levelName + " parameter \n objets="
				+ GameScene.levelName);
	}

	@Override
	public void createScene() {
		createBackground();
		createHUD();
		createPhysics();
		loadLevel();

		// attachChild(new DebugRenderer(physicsWorld,vbom));

	}

	private void loadLevel() {
		try {
			TMXLoader tmxLoader = new TMXLoader(
					ResourcesManager.getInstance().activity.getAssets(),
					engine.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, vbom,
					new ITMXTilePropertiesListener() {
						@Override
						public void onTMXTileWithPropertiesCreated(
								final TMXTiledMap pTMXTiledMap,
								final TMXLayer pTMXLayer,
								final TMXTile pTMXTile,
								final TMXProperties<TMXTileProperty> pTMXTileProperties) {

						}
					});

			// this.mTMXTiledMap = tmxLoader.loadFromAsset("worlds/world" + j +
			// ".tmx");
			// miTest
			// test5
			// MarioTest
			// this.mTMXTiledMap = tmxLoader.loadFromAsset("worlds/test5.tmx");
			System.out.println(levelName + " createScene");
			if (levelName != null) {
				this.mTMXTiledMap = tmxLoader.loadFromAsset("worlds/"
						+ levelName);
			} else {
				this.mTMXTiledMap = tmxLoader.loadFromAsset("worlds/test5.tmx");
				System.out.println("error ln is null again");
			}

		} catch (Exception e) {
			System.out.println("Error!!!: " + e);
			Debug.e(e);
		}

		final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0,
				0.01f, 0.5f);

		for (int i = 0; i < this.mTMXTiledMap.getTMXLayers().size(); i++) {
			TMXLayer layer = this.mTMXTiledMap.getTMXLayers().get(i);
			this.attachChild(layer);
		}

		for (final TMXObjectGroup group : this.mTMXTiledMap
				.getTMXObjectGroups()) {
			for (final TMXObject object : group.getTMXObjects()) {

				if (group.getName().equals("Unwalkable")) {
					Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight(), vbom);

					FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(
							0.0f, 0.0f, 1f, false,
							GameConstants.CATEGORYBIT_WALL,
							GameConstants.MASKBITS_WALL, (short) 0);

					PhysicsFactory.createBoxBody(physicsWorld, rect,
							BodyType.StaticBody, boxFixtureDef);

					rect.setVisible(false);

					final PhysicsHandler physicsHandler = new PhysicsHandler(
							rect);
					rect.registerUpdateHandler(physicsHandler);

					attachChild(rect);
				} else if (group.getName().equals("Enemies")) {

					Enemy enemy = new Enemy(object.getX(), object.getY(), vbom,
							camera, physicsWorld) {

						@Override
						public void onDie() {
							// TODO Auto-generated method stub

						}
					};

					attachChild(enemy);

				} else if (group.getName().equals("enemyunwalkable")) {

					Rectangle rect2 = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight(), vbom);

					FixtureDef boxFixtureDef2 = PhysicsFactory
							.createFixtureDef(0.0f, 0.0f, 1f, false,
									GameConstants.CATEGORYBIT_ENEMYWALL,
									GameConstants.MASKBITS_ENEMYWALL, (short) 0);

					Body enemyWall = PhysicsFactory.createBoxBody(physicsWorld,
							rect2, BodyType.StaticBody, boxFixtureDef2);

					enemyWall.setUserData("enemyunwalkable");

					rect2.setVisible(false);

					final PhysicsHandler physicsHandler2 = new PhysicsHandler(
							rect2);
					rect2.registerUpdateHandler(physicsHandler2);

					attachChild(rect2);
				} else if (group.getName().equals("platform1")) {

					// Sprite levelObject = new Sprite(object.getX(),
					// object.getY(), resourcesManager.platform1_region,
					// vbom);
					// PhysicsFactory.createBoxBody(physicsWorld, levelObject,
					// BodyType.StaticBody, FIXTURE_DEF).setUserData(
					// "platform1");
					// attachChild(levelObject);
				} else if (group.getName().equals("platform2")) {

					// Sprite levelObject = new Sprite(object.getX(),
					// object.getY(), resourcesManager.platform2_region,
					// vbom);
					// final Body body = PhysicsFactory.createBoxBody(
					// physicsWorld, levelObject, BodyType.StaticBody,
					// FIXTURE_DEF);
					// body.setUserData("platform2");
					// physicsWorld.registerPhysicsConnector(new
					// PhysicsConnector(
					// levelObject, body, true, false));
					// attachChild(levelObject);
				} else if (group.getName().equals("platform3")) {

					// Sprite levelObject = new Sprite(object.getX(),
					// object.getY(), resourcesManager.platform3_region,
					// vbom);
					// final Body body = PhysicsFactory.createBoxBody(
					// physicsWorld, levelObject, BodyType.StaticBody,
					// FIXTURE_DEF);
					// body.setUserData("platform3");
					// physicsWorld.registerPhysicsConnector(new
					// PhysicsConnector(
					// levelObject, body, true, false));
					// attachChild(levelObject);
				} else if (group.getName().equals("coin")) {

					Sprite levelObject = new Sprite(object.getX(),
							object.getY(), resourcesManager.coin_region, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);

							if (player.collidesWith(this)) {
								addToScore(10);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
							}
						}
					};
					levelObject.registerEntityModifier(new LoopEntityModifier(
							new ScaleModifier(1, 1, 1.3f)));
					attachChild(levelObject);
				} else if (group.getName().equals("exit_item")) {
					// TODO
					Sprite levelObject = new Sprite(object.getX(),
							object.getY(),
							resourcesManager.exit_item_region, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);

							if (player.collidesWith(this)) {
								// levelCompleteWindow.display(StarsCount.TWO,
								// GameScene.this, camera);
								// TODO
//								SceneManager
//										.getInstance()
//										.loadLevelPassedScene(
//												ResourcesManager.getInstance().engine,
//												score);
								SceneManager.getInstance().loadLevelPassedScene(engine, score);
//								this.setVisible(false);
//								this.setIgnoreUpdate(true);
							}
						}
					};
					levelObject.registerEntityModifier(new LoopEntityModifier(
							new ScaleModifier(1, 1, 1.3f)));
					attachChild(levelObject);
				}

			}
		}

		// mTMXTiledMap.getHeight();
		int h = mTMXTiledMap.getTMXLayers().get(0).getHeight();
		System.out.println("yDeath=" + h);
		Player.yDeath = h;

		player = new Player(200, 200, vbom, camera, physicsWorld) {// 200 400
			@Override
			public void onDie() {
				SceneManager.getInstance().loadGameScene(
						ResourcesManager.getInstance().engine, levelName);
			}

			@Override
			public void shoot() {
				float lc[] = this.convertLocalToSceneCoordinates(16, 32);
				Bullet bullet = new Bullet(
						((lastdirection == Action.MOVELEFT) ? (lc[Constants.VERTEX_INDEX_X] - 5)
								: (lc[Constants.VERTEX_INDEX_X] - 10)),
						this.getY() + this.getHeight() / 2, vbom, camera,
						physicsWorld,// + 5
						lastdirection) {

					@Override
					public void onDestroy() {

						final Bullet bullet = this;

						((BaseGameActivity) activity)
								.runOnUpdateThread(new Runnable() {

									@Override
									public void run() {
										physicsWorld
												.unregisterPhysicsConnector(physicsWorld
														.getPhysicsConnectorManager()
														.findPhysicsConnectorByShape(
																bullet));
										bullet.getBody().setActive(false);
										physicsWorld.destroyBody(bullet
												.getBody());
										GameScene.this.detachChild(bullet);
									}

								});

					}

				};

				GameScene.this.attachChild(bullet);

			}
		};

		if (levelName.equalsIgnoreCase("panteon.tmx")) {
			musica = ResourcesManager.getInstance().music_panteon;
		} else if (levelName.equalsIgnoreCase("metal.tmx")) {
			musica = ResourcesManager.getInstance().music_fabrica;
		} else if (levelName.equalsIgnoreCase("grass_palm.tmx")) {
			musica = ResourcesManager.getInstance().music_desierto;
		} else {
			musica = ResourcesManager.getInstance().music;
		}
		musica.play();
		musica.resume();
		musica.setLooping(true);

		attachChild(player);

	}

	private void createPhysics() {
		physicsWorld = new PhysicsWorld(new Vector2(0f,
				GameConstants.GRAVITY_EARTH), false);
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);
	}

	private void createHUD() {

		gameHUD = new HUD();

		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.setTouchAreaBindingOnActionMoveEnabled(true);

		final Sprite jumpButton = new Sprite(
				GameConstants.CAMERA_WIDTH - 50 - 10,
				GameConstants.CAMERA_HEIGHT - 50, 50, 50,
				ResourcesManager.getInstance().tiledTextureJump, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {

					player.jump();

				} else if (pSceneTouchEvent.isActionUp()) {

				}

				return true;
			};
		};

		final Sprite shootButton = new Sprite(
				GameConstants.CAMERA_WIDTH - 50 - 10,
				GameConstants.CAMERA_HEIGHT - 100 - 10, 50, 50,
				ResourcesManager.getInstance().tiledTextureshoot, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {

					player.shoot();

				} else if (pSceneTouchEvent.isActionUp()) {

				}

				return true;
			};
		};

		final Sprite leftArrowButton = new Sprite(10,
				GameConstants.CAMERA_HEIGHT - 50, 50, 50,
				ResourcesManager.getInstance().tiledTextureleftarrow, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {

					player.move(Action.MOVELEFT);

				} else if (pSceneTouchEvent.isActionUp()) {

					player.move(Action.STOP);

				}

				return true;
			};
		};

		final Sprite rightArrowButton = new Sprite(10 + 50 + 10,
				GameConstants.CAMERA_HEIGHT - 50, 50, 50,
				ResourcesManager.getInstance().tiledTexturerightarrow, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {

					player.move(Action.MOVERIGHT);

				} else if (pSceneTouchEvent.isActionUp()) {

					player.move(Action.STOP);

				}

				return true;
			};
		};
		gameHUD.attachChild(jumpButton);
		gameHUD.attachChild(shootButton);
		gameHUD.attachChild(leftArrowButton);
		gameHUD.attachChild(rightArrowButton);

		gameHUD.registerTouchArea(jumpButton);
		gameHUD.registerTouchArea(shootButton);
		gameHUD.registerTouchArea(leftArrowButton);
		gameHUD.registerTouchArea(rightArrowButton);

		scoreText = new Text(10, 10, ResourcesManager.getInstance().font_20px,
				"Score: 0123456789", new TextOptions(HorizontalAlign.LEFT),
				ResourcesManager.getInstance().engine
						.getVertexBufferObjectManager());
		scoreText.setText("Score: 0");
		gameHUD.attachChild(scoreText);

		camera.setHUD(gameHUD);
	}

	private void createBackground() {
		setBackground(new Background(0.18f, 0.74f, 0.98f));
	}

	@Override
	public void onBackKeyPressed() {
		// TODO
		// when the back is pressed it's supposed to show the options scene
		// SceneManager.getInstance().loadMenuScene(engine);
		SceneManager.getInstance().loadLevelSelector(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		this.stopManagedUpdate = true;
		camera.setHUD(null);
		camera.setChaseEntity(null);
		camera.setCenter(GameConstants.CAMERA_WIDTH / 2,
				GameConstants.CAMERA_HEIGHT / 2);
		// ResourcesManager.getInstance().music.stop();
		musica.stop();
		this.detachSelf();
		this.dispose();
	}

	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {
			public void beginContact(Contact contact) {

				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null
						&& x2.getBody().getUserData() != null) {

					if (x1.getBody().getUserData().equals("platform2")
							&& x2.getBody().getUserData().equals("playerFeet")) {
						engine.registerUpdateHandler(new TimerHandler(0.2f,
								new ITimerCallback() {
									public void onTimePassed(
											final TimerHandler pTimerHandler) {
										pTimerHandler.reset();
										engine.unregisterUpdateHandler(pTimerHandler);
										x1.getBody().setType(
												BodyType.DynamicBody);
									}
								}));
					}

					if (x1.getBody().getUserData().equals("platform3")
							&& x2.getBody().getUserData().equals("playerFeet")) {
						x1.getBody().setType(BodyType.DynamicBody);
					}
				}

				if (x2.getUserData() != null) {

					if (x2.getUserData().toString() == "playerFeet") {
						player.increaseFootContacts();
					}
				}

				if (x1.getUserData() != null) {

					if (x1.getUserData().toString() == "playerFeet") {
						player.increaseFootContacts();
					}
				}

				if (x1.getBody().getUserData() instanceof Bullet) {
					Bullet bu = (Bullet) x1.getBody().getUserData();
					bu.getBody().setUserData(null);
					bu.onDestroy();
				}
				if (x2.getBody().getUserData() instanceof Bullet) {
					Bullet bu = (Bullet) x2.getBody().getUserData();
					bu.getBody().setUserData(null);
					bu.onDestroy();
				}

				// On ground touch or right sensor touch continue moving to
				// left.
				if (x1.getUserData() instanceof EnemySensors
						&& x1.getBody().getUserData() instanceof Enemy) {
					if (x1.getUserData() == EnemySensors.BOTTOM
							|| x1.getUserData() == EnemySensors.RIGHT) {
						((Enemy) x1.getBody().getUserData())
								.move(Action.MOVELEFT);
					}
				}
				if (x2.getUserData() instanceof EnemySensors
						&& x2.getBody().getUserData() instanceof Enemy) {
					if (x2.getUserData() == EnemySensors.BOTTOM
							|| x2.getUserData() == EnemySensors.RIGHT) {
						((Enemy) x2.getBody().getUserData())
								.move(Action.MOVELEFT);
					}
				}

				// On left sensor touch continue moving to right.
				if (x1.getUserData() instanceof EnemySensors
						&& x1.getBody().getUserData() instanceof Enemy) {
					if (x1.getUserData() == EnemySensors.LEFT) {
						((Enemy) x1.getBody().getUserData())
								.move(Action.MOVERIGHT);
					}
				}
				if (x2.getUserData() instanceof EnemySensors
						&& x2.getBody().getUserData() instanceof Enemy) {
					if (x2.getUserData() == EnemySensors.LEFT) {
						((Enemy) x2.getBody().getUserData())
								.move(Action.MOVERIGHT);
					}
				}

			}

			public void endContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x2.getUserData() != null) {
					if (x2.getUserData().toString() == "playerFeet")
						player.decreaseFootContacts();
				}
				if (x1.getUserData() != null) {
					if (x1.getUserData().toString() == "playerFeet")
						player.decreaseFootContacts();
				}
			}

			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		};
		return contactListener;
	}

	// @Override
	// public void onManagedUpdate(final float pSecondsElapsed) {
	// super.onManagedUpdate(pSecondsElapsed);
	// if (this.stopManagedUpdate)
	// return;
	// physicsWorld.onUpdate(pSecondsElapsed);
	// Entity e = new Entity();
	//
	// int tilemapheight = this.mTMXTiledMap.getTileHeight()
	// * this.mTMXTiledMap.getTileRows(), mapW = this.mTMXTiledMap
	// .getTileWidth() * this.mTMXTiledMap.getTileColumns();
	//
	// float y = (tilemapheight - (GameConstants.CAMERA_HEIGHT / 2));
	//
	// if (player.getX() > (GameConstants.CAMERA_WIDTH / 2)
	// && player.getX() < (mapW - GameConstants.CAMERA_WIDTH / 2))
	// e.setPosition(player.getX(), y);
	// else if (player.getX() < (GameConstants.CAMERA_WIDTH / 2))
	// e.setPosition((GameConstants.CAMERA_WIDTH / 2), y);
	// else if (player.getX() > (mapW - GameConstants.CAMERA_WIDTH / 2))
	// e.setPosition((mapW - GameConstants.CAMERA_WIDTH / 2), y);
	//
	// camera.setChaseEntity(e);
	//
	// final MoveModifier modifier = new MoveModifier(30, e.getX(),
	// player.getX(), e.getY(), y) {
	// @Override
	// protected void onModifierFinished(IEntity pItem) {
	// super.onModifierFinished(pItem);
	// camera.setChaseEntity(null);
	// }
	// };
	//
	// e.registerEntityModifier(modifier);
	// }

	private void addToScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}

}
