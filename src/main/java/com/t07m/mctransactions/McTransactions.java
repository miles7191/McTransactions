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
package com.t07m.mctransactions;

import java.awt.Frame;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javafaker.Faker;
import com.github.zafarkhaja.semver.Version;
import com.t07m.application.Application;
import com.t07m.console.remote.server.RemoteServer;
import com.t07m.console.swing.ConsoleWindow;
import com.t07m.mctransactions.alert.AlertManager;
import com.t07m.mctransactions.camera.CameraManager;
import com.t07m.mctransactions.command.WhoAmICommand;
import com.t07m.mctransactions.config.McConfig;
import com.t07m.mctransactions.email.EmailHandler;
import com.t07m.mctransactions.email.GmailClient;
import com.t07m.mctransactions.keystation.KeyStationManager;
import com.t07m.mctransactions.transaction.TransactionHandler;

import lombok.Getter;
import net.cubespace.Yamler.Config.InvalidConfigurationException;

public class McTransactions extends Application {

	public static final Version VERSION = Version.valueOf("0.0.1");
	
	private static Logger logger = LoggerFactory.getLogger(McTransactions.class);

	private static String identity;
	
	private @Getter McConfig config;
	private RemoteServer remoteConsole;
	
	private @Getter TransactionHandler transactionHandler;
	private @Getter KeyStationManager keyStationManager;
	private @Getter CameraManager cameraManager;
	private @Getter AlertManager alertManager;
	private @Getter EmailHandler emailHandler;
	
	public static void main(String[] args) {
		boolean gui = true;
		if(args.length > 0) {
			for(String arg : args) {
				if(arg.equalsIgnoreCase("-nogui")) {
					gui = false;
				}
			}
		}
		new McTransactions(gui).start();
	}
	
	public McTransactions(boolean gui) {
		super(gui, "McTransactions");
	}

	public void init() {
		this.config = new McConfig();
		try {
			this.config.init();
			this.config.save();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			System.err.println("Unable to load configuration file!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {}
			System.exit(-1);
		}
		logger.info("Launching Application - " + getIdentity() + " NSN:" + config.getNSN());
		remoteConsole = new RemoteServer(this.getConsole(), 13560);
		remoteConsole.init();
		remoteConsole.bind();
		this.getConsole().registerCommand(new WhoAmICommand(this));
		this.transactionHandler = new TransactionHandler(this);
		keyStationManager = new KeyStationManager(this);
		keyStationManager.init();
		cameraManager = new CameraManager(this);
		cameraManager.init();
		alertManager = new AlertManager(this);
		alertManager.init();
		emailHandler = new EmailHandler(new GmailClient(config.getGmailUser(), config.getGmailPassword()), this);
		if(this.getConsole() instanceof ConsoleWindow) {
			if(config.isAutoHide())
				((ConsoleWindow)(this.getConsole())).setState(Frame.ICONIFIED);
		}
	}
	
	public static String getIdentity() {
		if(identity == null) {
			Faker faker = new Faker(new Locale("en-US"));
			identity = faker.name().firstName() + " " + faker.name().lastName();
		}
		return identity;		
	}
	
	public void cleanup() {
		remoteConsole.unbind();
	}
	
}
