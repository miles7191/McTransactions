/*
 * Copyright (C) 2021 Matthew Rosato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.t07m.mctransactions.camera;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.t07m.application.Service;
import com.t07m.mctransactions.McTransactions;
import com.t07m.mctransactions.config.McConfig.CameraConfig;

public class CameraManager{

	private static Logger logger = LoggerFactory.getLogger(CameraManager.class);
	
	private McTransactions app;
	
	private List<Camera> cameras;

	public CameraManager(McTransactions app) {
		this.app = app;
	}
		
	public void init() {
		cameras = new ArrayList<Camera>();
		for(CameraConfig config : app.getConfig().getCameras()) {
			addCamera(config);
		}
	}
	
	private void addCamera(CameraConfig config) {
		synchronized(cameras){
			for(Camera camera : cameras) {
				if(camera.getConfig().equals(config)) {
					return;
				}else if(camera.getConfig().getID() == config.getID()) {
					logger.warn("Attempted to load Camera with duplicate ID! " + config.getID());
					return;
				}
			}
			cameras.add(new Camera(config));
			logger.info("Loaded Camera: " + config.getFriendlyName());
		}
	}
	
	public Camera getCamera(int id) {
		synchronized(cameras) {
			for(Camera camera : cameras) {
				if(camera.getConfig().getID() == id)
					return camera;
			}
		}
		return null;
	}
}
