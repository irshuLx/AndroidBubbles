/*
 Copyright 2016 Rodrigo Deleu Lopes Pontes

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.rodrigopontes.androidbubblesexample;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import com.rodrigopontes.androidbubbles.BubblesManager;

public class ScreenOrientationService extends Service {

	BubblesManager bubblesManager;

	public ScreenOrientationService() {}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// If running Android M, ask for drawing permission if necessary
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if(Settings.canDrawOverlays(getApplicationContext())) {
				if(BubblesManager.exists()) {
					bubblesManager = BubblesManager.getManager();
				} else {
					bubblesManager = BubblesManager.create(getApplicationContext());
				}
				IntentFilter intentFilter = new IntentFilter();
				intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
				registerReceiver(new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent intent) {
						if(intent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
							bubblesManager.updateConfiguration();
						}
					}
				}, intentFilter);
			} else {
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				stopSelf();
			}
		} else {
			if(BubblesManager.exists()) {
				bubblesManager = BubblesManager.getManager();
			} else {
				bubblesManager = BubblesManager.create(getApplicationContext());
			}
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					if(intent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
						bubblesManager.updateConfiguration();
					}
				}
			}, intentFilter);
		}
	}
}
