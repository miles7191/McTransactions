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
package com.t07m.mctransactions.keystation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.t07m.application.Service;
import com.t07m.mctransactions.McTransactions;
import com.t07m.mctransactions.config.McConfig.KeyStationConfig;
import com.t07m.mctransactions.net.SMBBopWatcher;

public class KeyStationManager {

	private static Logger logger = LoggerFactory.getLogger(KeyStationManager.class);

	private McTransactions app;
	
	private List<KeyStation> keyStations;
	
	public KeyStationManager(McTransactions app) {
		this.app = app;
	}
	
	public void init() {
		logger.debug("Initializing KeyStationManager");
		keyStations = new ArrayList<KeyStation>();
		for(KeyStationConfig ksConfig : app.getConfig().getKeyStations()) {
			addKeyStation(ksConfig);
		}
	}

	private void addKeyStation(KeyStationConfig ksConfig) {
		synchronized(keyStations) {
			for(KeyStation ks : keyStations) {
				if(ks.getConfig().equals(ksConfig)) {
					return;
				}else if(ks.getConfig().getID() == ksConfig.getID()) {
					logger.warn("Attempted to load KS with duplicate ID! " + ksConfig.getID());
					return;
				}
			}
			KeyStation ks = new KeyStation(ksConfig);
			keyStations.add(ks);
			logger.info("Loaded KS: " + ksConfig.getFriendlyName());
			if(ks.getConfig().isEnabled()) {
				if(ks.getSmbWatcher() == null) {
					ks.start(app);
				}
			}
		}
	}
	
	private void cleanupKeyStation(KeyStation ks) {
		app.removeService(ks.getSmbWatcher());
	}

	public void cleanup() {
		synchronized(keyStations) {
			Iterator<KeyStation> itr = keyStations.iterator();
			while(itr.hasNext()) {
				KeyStation ks = itr.next();
				itr.remove();
				ks.cleanup();
			}
			SMBBopWatcher.shutdown();
		}
	}

}
