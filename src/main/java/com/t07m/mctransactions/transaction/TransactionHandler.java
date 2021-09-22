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
package com.t07m.mctransactions.transaction;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.t07m.mctransactions.McTransactions;
import com.t07m.mctransactions.camera.Camera;
import com.t07m.mctransactions.receipt.BOP;
import com.t07m.mctransactions.receipt.ReceiptFormatter;

public class TransactionHandler {

	private McTransactions app;
	
	public TransactionHandler(McTransactions app) {
		this.app = app;
	}
	
	public void processTransaction(String ksName, int[] cameras, String name, InputStream res) {
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		ExecutorService es = Executors.newCachedThreadPool();
		for(int cameraID : cameras) {
			Camera camera = app.getCameraManager().getCamera(cameraID);
			if(camera != null) {
				Thread t = new Thread() {
					public void run() {
						BufferedImage image = camera.getImage();
						if(image != null) {
							images.add(image);
						}
					}
				};
				es.submit(t);
			}
		}
		es.shutdown();
		Scanner scanner = new Scanner(res);
		ArrayList<String> lines = new ArrayList<String>();
		while(scanner.hasNextLine()) {
			lines.add(scanner.nextLine());
		}
		scanner.close();
		BOP bop = new BOP(name, lines.toArray(new String[lines.size()]));
		if(bop != null) {
			try {
				es.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {}
						submitTransaction(new Transaction(bop, images.toArray(new BufferedImage[images.size()])), ksName);
		}
	}
	
	private void submitTransaction(Transaction transaction, String ksName) {
		app.getAlertManager().processAlerts(transaction, ksName);
	}
	
}
