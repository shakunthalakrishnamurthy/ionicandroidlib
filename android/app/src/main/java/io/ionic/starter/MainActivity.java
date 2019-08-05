/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package io.ionic.starter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewEngine;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.PluginEntry;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends Activity
{




    protected CordovaPreferences preferences;
    protected CordovaWebView appView;
    protected String launchUrl;
    protected ArrayList<PluginEntry> pluginEntries;
    protected CordovaInterfaceImpl cordovaInterface;
    public static String screenRender = "/details";
    private static Context context;




    public boolean setScreenRender(String screenname) {
        screenRender = screenname;
        return true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.screenRender=extras.getString("KEY");
            System.out.println("KEY:::::" + extras.getString("KEY"));
        }

        this.loadConfig();
        String logLevel = preferences.getString("loglevel", "ERROR");
        cordovaInterface = makeCordovaInterface();
        if (savedInstanceState != null) {
            cordovaInterface.restoreInstanceState(savedInstanceState);
        }

        //to implement async call here for intent received
        System.out.println("Received intent to render:::::::" + screenRender);


        class mATask extends AsyncTask<String, String, String>
        {

            protected void onPreExecute() {
                super.onPreExecute();
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    screenRender=extras.getString("ROOT");
                    System.out.println("root:::::" + extras.getString("ROOT"));
                }
            }
            @SuppressLint("WrongThread")
            @Override
            protected String doInBackground(String... params) {
                //Enter code for fetching value from remote server
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                loadUrl(launchUrl);//Enter code for parsing and create list view
            }
        };
        new mATask().execute("","","");
        //CompletableFuture.supplyAsync(this::getIntentExtras)
        //        .thenAccept(loadUrl(launchUrl));
    }
    // Author: Kok Seng, Chua
    // here are all the implementations of CordovaActivity to run the loadUrl

    private void getIntentExtras(){
        Bundle extrasMsg = getIntent().getExtras();
        if (extrasMsg != null) {
            this.screenRender=extrasMsg.getString("ROOT");
            System.out.println("root:::::" + extrasMsg.getString("ROOT"));
        }
    }


    @SuppressWarnings("deprecation")
    protected void loadConfig() {
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(this);
        preferences = parser.getPreferences();
        preferences.setPreferencesBundle(getIntent().getExtras());


        launchUrl = parser.getLaunchUrl();
        pluginEntries = parser.getPluginEntries();
        //Config.parser = parser;
    }


    public void loadUrl(String url) {
        if (appView == null) {
            init();
        }
        appView.loadUrlIntoView(url, true);
    }

    protected void init() {
        appView = makeWebView();
        createViews();
        if (!appView.isInitialized()) {
            System.out.println(pluginEntries);
            try {appView.init(cordovaInterface, pluginEntries, preferences);}
            catch (Exception e){}
        }
        cordovaInterface.onCordovaInit(appView.getPluginManager());
    }


    protected CordovaWebView makeWebView() {
        return new CordovaWebViewImpl(makeWebViewEngine());
    }


    protected CordovaWebViewEngine makeWebViewEngine() {
        return CordovaWebViewImpl.createEngine(this, preferences);
    }


    @SuppressWarnings({"deprecation", "ResourceType"})
    protected void createViews() {

        appView.getView().setId(100);
        appView.getView().setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        setContentView(appView.getView());

        if (preferences.contains("BackgroundColor")) {
            try {
                int backgroundColor = preferences.getInteger("BackgroundColor", Color.BLUE);
                // Background of activity:
                appView.getView().setBackgroundColor(backgroundColor);
            }
            catch (NumberFormatException e){
                e.printStackTrace();
            }
        }

        appView.getView().requestFocusFromTouch();
    }

    protected CordovaInterfaceImpl makeCordovaInterface() {
        return new CordovaInterfaceImpl(this) {
            @Override
            public Object onMessage(String id, Object data) {
                // Plumb this to CordovaActivity.onMessage for backwards compatibility
                //return KokSengActivityHandler.this.onMessage(id, data);
                return super.onMessage(id,data);
            }
        };
    }


}