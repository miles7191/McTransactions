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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.t07m.mctransactions.McTransactions;
import com.t07m.mctransactions.config.McConfig.KeyStationConfig;
import com.t07m.mctransactions.net.SMBBopWatcher;
import com.t07m.mctransactions.receipt.BOP;
import com.t07m.mctransactions.receipt.ReceiptFormatter;

import lombok.Getter;
import lombok.Setter;

public class KeyStation {

	private @Getter @Setter KeyStationConfig config;
	
	private @Getter SMBBopWatcher smbWatcher;
	
	KeyStation(KeyStationConfig config) {
		this.config = config;
	}
	
	public boolean start(McTransactions app) {
		if(smbWatcher != null) {
			smbWatcher.cleanup();
		}
		smbWatcher = new SMBBopWatcher(app, config) {
			public void onNewBop(String name, InputStream res) {
				app.getTransactionHandler().processTransaction(config.getFriendlyName(), config.getCameras(), name, res);
			}
		};
		smbWatcher.init();
		app.registerService(smbWatcher);
		return false;
	}
	
	public boolean isRunning() {
		return smbWatcher != null && smbWatcher.isRunning();
	}
	
	public void cleanup() {
		if(smbWatcher != null) {
			smbWatcher = null;
		}
	}	
}