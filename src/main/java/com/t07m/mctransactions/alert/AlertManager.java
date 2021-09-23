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
package com.t07m.mctransactions.alert;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.t07m.mctransactions.McTransactions;
import com.t07m.mctransactions.config.McConfig.AlertConfig;
import com.t07m.mctransactions.receipt.BOPParser;
import com.t07m.mctransactions.receipt.BOPParser.TransactionType;
import com.t07m.mctransactions.transaction.Transaction;

public class AlertManager {
	
	private static Logger logger = LoggerFactory.getLogger(AlertManager.class);
	
	private McTransactions app;
	
	private List<Alert> alerts;
	
	public AlertManager(McTransactions app) {
		this.app = app;
	}

	public void init() {
		alerts = new ArrayList<Alert>();
		for(AlertConfig config : app.getConfig().getAlerts()) {
			addAlert(config);
		}
	}
	
	private void addAlert(AlertConfig config) {
		synchronized(alerts) {
			for(Alert alert : alerts) {
				if(alert.getConfig().equals(config)) {
					return;
				}else if(alert.getConfig().getID() == config.getID()) {
					logger.warn("Attempted to load Alert with duplicate ID! " + config.getID());
					return;
				}
			}
			alerts.add(new Alert(config));
			logger.info("Loaded Alert: " + config.getID());
		}
	}
	
	public void processAlerts(Transaction transaction, String ksName) {
		TransactionType type = BOPParser.parseTransactionType(transaction.getBop());
		double value = BOPParser.getTotal(transaction.getBop());
		double savings = BOPParser.parseTotalSavings(transaction.getBop());
		ArrayList<Alert> active = new ArrayList<Alert>();
		synchronized(alerts) {
			for(Alert alert : alerts) {
				if(alert.shouldAlert(type, value, savings)) {
					active.add(alert);
				}
			}
		}
		for(Alert alert : active) {
			app.getEmailHandler().emailNewAlert(transaction, alert, ksName);
		}
	}
	
}
