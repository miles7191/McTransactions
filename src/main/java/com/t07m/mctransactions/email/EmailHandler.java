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
package com.t07m.mctransactions.email;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.t07m.mctransactions.McTransactions;
import com.t07m.mctransactions.alert.Alert;
import com.t07m.mctransactions.config.McConfig.UserConfig;
import com.t07m.mctransactions.receipt.BOPParser;
import com.t07m.mctransactions.receipt.ReceiptFormatter;
import com.t07m.mctransactions.transaction.Transaction;

public class EmailHandler {
	
	private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");
	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	private static final Logger logger = LoggerFactory.getLogger(EmailHandler.class);

	private final EmailClient client;
	private final McTransactions app;

	public EmailHandler(EmailClient client, McTransactions app) {
		this.client = client;
		this.app = app;
	}

	public boolean emailNewAlert(Transaction transaction, Alert alert, String ksName) {
		ArrayList<UserConfig> users = new ArrayList<UserConfig>();
		for(UserConfig user : app.getConfig().getUsers()) {
			if(user.isEnabled()) {
				for(int alertID : user.getAlerts()) {
					if(alertID == alert.getConfig().getID()) {
						logger.debug("Found user for alert: " + user.getEmail());
						users.add(user);
						break;
					}
				}
			}
		}
		if(users.size() == 0)
			return true;
		String messageText = app.getConfig().getEmailTemplate()
				.replace("{NSN}", app.getConfig().getNSN())
				.replace("{KS}", ksName)
				.replace("{ALERT}", alert.getConfig().getFriendlyName())
				.replace("{AMOUNT}", currencyFormat.format(BOPParser.getTotal(transaction.getBop())))
				.replace("{SAVINGS}", currencyFormat.format(Math.max(BOPParser.parseTotalSavings(transaction.getBop()),0)))
				.replace("{TIME}", BOPParser.getDateTime(transaction.getBop()).format(timeFormat))
				.replace("{DATE}", BOPParser.getDateTime(transaction.getBop()).format(dateFormat))
				.replace("{CLIENT}", McTransactions.getIdentity());
		ArrayList<File> tempFiles = new ArrayList<File>();
		BufferedImage receipt = ReceiptFormatter.BOPToImage(transaction.getBop());
		if(receipt != null) {
			File file = null;
				try {
				file = new File(transaction.getBop().getName().replace(".bop", "") + tempFiles.size() + ".png");
				ImageIO.write(receipt, "png", file);
				tempFiles.add(file);
			}catch(IOException e) {
				e.printStackTrace();
				if(file != null)
					file.delete();
			}
		}
		for(BufferedImage image : transaction.getImages()) {
			File file = null;
			try {
				file = new File(transaction.getBop().getName().replace(".bop", "") + tempFiles.size() + ".png");
				ImageIO.write(image, "png", file);
				tempFiles.add(file);
			}catch(IOException e) {
				if(file != null)
					file.delete();
			}
		}
		try {
			MimeMessage message = client.createMessage();
			message.setFrom(new InternetAddress("Apps.T07M@gmail.com", app.getConfig().getAppName()));
			message.setSubject("Transaction Alert {NSN}".replace("{NSN}", app.getConfig().getNSN()));
			Multipart multipart = new MimeMultipart();
			for(File file : tempFiles) {
				try {
					MimeBodyPart attachmentPart = new MimeBodyPart();
					attachmentPart.attachFile(file);
					multipart.addBodyPart(attachmentPart);
				} catch (IOException | MessagingException e) {
					e.printStackTrace();
				}
			}
			MimeBodyPart textPart = new MimeBodyPart();
			multipart.addBodyPart(textPart);
			message.setContent(multipart);
			boolean success = false;
			for(UserConfig user : users) {
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
				textPart.setContent(messageText.replace("{USER}", user.getFriendlyName()), "text/html");
				if(client.sendMessage(message)) {
					success = true;
				}
			}
			for(File file : tempFiles) {
				file.delete();
			}
			return success;
		} catch (UnsupportedEncodingException | MessagingException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	public boolean emailMessage(String text, String... recipients) {
		try {
			logger.debug("Building New Email");
			MimeMessage message = client.createMessage();
			message.setFrom(new InternetAddress("Apps.T07M@gmail.com", app.getConfig().getAppName()));
			for(String recipient : recipients) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			}
			message.setSubject("Transaction Alert {NSN}".replace("{NSN}", app.getConfig().getNSN()));
			message.setText(text);
			return client.sendMessage(message);
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
