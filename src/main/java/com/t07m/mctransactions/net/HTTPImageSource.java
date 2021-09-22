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
package com.t07m.mctransactions.net;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.imageio.ImageIO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HTTPImageSource {

	private final String source, username, password;

	public BufferedImage pullImage() {
		HttpURLConnection connection = null;
		try {
			if(username != null && password != null) {
				Authenticator.setDefault (new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication (username, password.toCharArray());
					}
				});
			}
			connection = (HttpURLConnection) new URL(source).openConnection();
			connection.connect();
			BufferedImage image = ImageIO.read(connection.getInputStream());
			connection.disconnect();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
