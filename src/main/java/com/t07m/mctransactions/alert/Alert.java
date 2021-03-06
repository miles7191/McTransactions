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

import com.t07m.mctransactions.config.McConfig.AlertConfig;
import com.t07m.mctransactions.receipt.BOPParser.TransactionType;

import lombok.Getter;

public class Alert {

	private @Getter AlertConfig config;
	
	Alert(AlertConfig config){
		this.config = config;
	}
	
	public boolean shouldAlert(TransactionType type, double value, double savings) {
		return config.isEnabled() 
				&& config.getTransactionType().equalsIgnoreCase(type.toString())
				&& (config.getMinimumValue() == -1 || value >= config.getMinimumValue()) 
				&& (config.getMaximumValue() == -1 || value <= config.getMaximumValue())
				&& (config.getMinimumSavings() == -1 || savings >= config.getMinimumSavings()) 
				&& (config.getMaximumSavings() == -1 || savings <= config.getMaximumSavings());
	}	
}
