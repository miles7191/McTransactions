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

import java.awt.image.BufferedImage;

import com.t07m.mctransactions.config.McConfig.CameraConfig;
import com.t07m.mctransactions.net.HTTPImageSource;

import lombok.Getter;
import lombok.Setter;

public class Camera {
	
	private @Getter @Setter CameraConfig config;
	
	private HTTPImageSource source;

	Camera(CameraConfig config){
		this.config = config;
	}
	
	private HTTPImageSource getSource() {
		if(source == null) {
			source = new HTTPImageSource(config.getUrl(), config.getUsername(), config.getPassword());
		}
		return source;
	}
	
	public BufferedImage getImage() {
		if(config.isEnabled()) {
			return getSource().pullImage();
		}
		return null;
	}
	
}
