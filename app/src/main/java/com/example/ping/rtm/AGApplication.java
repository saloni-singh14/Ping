package com.example.ping.rtm;

import android.app.Application;

public class AGApplication extends Application {
    private static AGApplication sInstance;
    private ChatManager mChatManager;

    public static AGApplication the() {
        return sInstance;
    }
    public AGApplication()
    {
        sInstance = this;

        mChatManager = new ChatManager(this);
        mChatManager.init();
        mChatManager.enableOfflineMessage(true);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mChatManager = new ChatManager(this);
        mChatManager.init();
        mChatManager.enableOfflineMessage(true);
    }

    public ChatManager getChatManager() {
        return mChatManager;
    }
}
