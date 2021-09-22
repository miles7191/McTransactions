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
package com.t07m.mctransactions.receipt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JEditorPane;

import joptsimple.internal.Strings;

public class ReceiptFormatter {

	private static final String style = "html{margin:0}body{margin:0}h1{font-size:60px;margin:0;margin-top:0;margin-bottom:0;font-family:Courier}p{font-size:12px;margin-top:0;margin-bottom:-13;font-family:Courier}";
	
	public static BufferedImage BOPToImage(BOP bop) {
		int width = 400;
		int height = calcHeight(bop.getLines());
		String[] data = bop.getLines().clone();
		addParagraphs(data);
		replace(data, "<@BoldOn>", "<b>");
		replace(data, "<@BoldOff>", "</b>");
		replace(data, "<@BigCharOn>", "<h1>");
		replace(data, "<@BigCharOff>", "</h1>");
		replace(data, "<@ReverseOn>", "");
		replace(data, "<@ReverseOff>", "");
		replace(data, "<@DoubleCharOn>", "");
		replace(data, "<@DoubleCharOff>", "");
		replace(data, " ", "&nbsp;");
		String html = "<html><head><style>" + style + "</style></head><body>" + Strings.join(data, "") + "</body></html>";
		BufferedImage image = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(width, height);
		
		Graphics2D graphics = (Graphics2D) image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

		JEditorPane jep = new JEditorPane("text/html", html);
		jep.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		jep.setSize(width, height);
		jep.print(graphics);
		
		return image;
	}

	private static int calcHeight(String[] lines) {
		double height = 145;
		for(String s : lines) {
			if(s.length() == 0) {
				height += 5;
			}else {
				height += 15;
			}
		}
		return (int) height;
	}

	private static void addParagraphs(String[] lines) {
		for(int i = 0; i < lines.length; i++) {
			lines[i] = "<p>" + lines[i] + "</p>";
		}
	}

	private static void replace(String[] lines, String s1, String s2) {
		for(int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].replace(s1, s2);
		}
	}

}
