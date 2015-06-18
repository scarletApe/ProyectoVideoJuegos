package com.xdgames.adventube.UserData;

import android.content.Context;
import android.content.SharedPreferences;

public class UserData {

	private static UserData INSTANCE;

	// Include a 'filename' for our shared preferences
	private static final String PREFS_NAME = "GAME_USERDATA";

	/*
	 * These keys will tell the shared preferences editor which data we're
	 * trying to access
	 */

	// private static final String UNLOCKED_LEVEL_KEY = "unlockedLevels";
	private static final String SOUND_KEY = "soundKey";

	/*
	 * Create our shared preferences object & editor which will be used to save
	 * and load data
	 */
	private SharedPreferences mSettings;
	private SharedPreferences.Editor mEditor;

	// keep track of our max unlocked level
	// private int mUnlockedLevels;

	// keep track of whether or not sound is enabled
	private boolean mSoundEnabled;

	// keep track of which channels or levels have score
	private static final String GENDER = "gender";
	private boolean mIsBoy;

	private static final String LVL_1_KEY = "lvl_1_key";
	private int mLvl_1_score;

	private static final String LVL_2_KEY = "lvl_2_key";
	private int mLvl_2_score;

	private static final String LVL_3_KEY = "lvl_3_key";
	private int mLvl_3_score;

	private static final String LVL_4_KEY = "lvl_4_key";
	private int mLvl_4_score;

	private static final String LVL_5_KEY = "lvl_5_key";
	private int mLvl_5_score;

	UserData() {
		// The constructor is of no use to us
	}

	public synchronized static UserData getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UserData();
		}
		return INSTANCE;
	}

	public synchronized void init(Context pContext) {
		if (mSettings == null) {
			/*
			 * Retrieve our shared preference file, or if it's not yet created
			 * (first application execution) then create it now
			 */
			mSettings = pContext.getSharedPreferences(PREFS_NAME,
					Context.MODE_PRIVATE);

			/*
			 * Define the editor, used to store data to our preference file
			 */
			mEditor = mSettings.edit();

			/*
			 * Retrieve our current unlocked levels. if the UNLOCKED_LEVEL_KEY
			 * does not currently exist in our shared preferences, we'll create
			 * the data to unlock level 1 by default
			 */
			// mUnlockedLevels = mSettings.getInt(UNLOCKED_LEVEL_KEY, 1);
			mIsBoy = mSettings.getBoolean(GENDER, true);

			mLvl_1_score = mSettings.getInt(LVL_1_KEY, 0);
			mLvl_2_score = mSettings.getInt(LVL_2_KEY, 0);
			mLvl_3_score = mSettings.getInt(LVL_3_KEY, 0);
			mLvl_4_score = mSettings.getInt(LVL_4_KEY, 0);
			mLvl_5_score = mSettings.getInt(LVL_5_KEY, 0);

			/*
			 * Same idea as above, except we'll set the sound boolean to true if
			 * the setting does not currently exist
			 */
			mSoundEnabled = mSettings.getBoolean(SOUND_KEY, true);
		}
	}

	/* retrieve the max unlocked level value */
	public synchronized int getLevelScore(int i) {
		switch (i) {
		case 1:
			return mLvl_1_score;
		case 2:
			return mLvl_2_score;
		case 3:
			return mLvl_3_score;
		case 4:
			return mLvl_4_score;
		case 5:
			return mLvl_5_score;
		}
		return 0;
	}

	/* retrieve the boolean defining whether sound is muted or not */
	public synchronized boolean isSoundMuted() {
		return mSoundEnabled;
	}

	/*
	 * This method provides a means to increase the max unlocked level by a
	 * value of 1. unlockNextLevel would be called if a player defeats the
	 * current maximum unlocked level
	 */
	public synchronized void savetLevelScore(int level, int score) {
		// Increase the max level by 1
		// mUnlockedLevels++;

		/*
		 * Edit our shared preferences unlockedLevels key, setting its value our
		 * new mUnlockedLevels value
		 */
		// mEditor.putInt(UNLOCKED_LEVEL_KEY, mUnlockedLevels);
		switch (level) {
		case 1:
			mEditor.putInt(LVL_1_KEY, score);
			break;
		case 2:
			mEditor.putInt(LVL_2_KEY, score);
			break;
		case 3:
			mEditor.putInt(LVL_3_KEY, score);
			break;
		case 4:
			mEditor.putInt(LVL_4_KEY, score);
			break;
		case 5:
			mEditor.putInt(LVL_5_KEY, score);
			break;
		}

		/*
		 * commit() must be called by the editor in order to save changes made
		 * to the shared preference data
		 */
		mEditor.commit();
	}
	
	public synchronized boolean isBoy(){
		return mIsBoy;
	}
	
	public synchronized void setGender(boolean isBoy){
		mEditor.putBoolean(GENDER, isBoy);
		mEditor.commit();
	}

	/*
	 * The setSoundMuted method uses the same idea for storing new data into the
	 * shared preferences. First, we overwrite the mSoundEnabled boolean, use
	 * the putBoolean method to store the data, and finally commit the data to
	 * the shared preferences
	 */
	public synchronized void setSoundMuted(final boolean pEnableSound) {
		mSoundEnabled = pEnableSound;
		mEditor.putBoolean(SOUND_KEY, mSoundEnabled);
		mEditor.commit();
	}
}