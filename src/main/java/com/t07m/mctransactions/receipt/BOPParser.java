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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

public class BOPParser {


	public static enum TransactionType{
		Order(null), 
		Promo("**** PROMO ****"), 
		Refund("**** REFUND ****"), 
		Activation("ACTIVATION RECEIPT"), 
		BillableSale("** BILLABLE SALE **"), 
		Overring("**** OVERRING ****"), 
		ManagerMeal("Manager meal discount"), 
		EmployeeMeal("Employee meal discount"),
		Grubhub("** GRUB HUB **"),
		DoorDash("** DOOR DASH **"),
		Uber("** UberEats SALE **"),
		Delivery("Delivery");

		private final @Getter String marker;

		TransactionType(String marker){
			this.marker = marker;
		}

		public static TransactionType defaultType() {
			return Order;
		}

	};

	private static final DateTimeFormatter nameParseFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	private static Logger logger = LoggerFactory.getLogger(BOPParser.class);

	public static int getKS(BOP bop) {
		if(validateBOPName(bop)) {
			try {
				return Integer.parseInt(bop.getName().substring(3, 7));
			}catch(NumberFormatException e) {
				logger.error(e.getMessage());
			}
		}
		return 0;
	}

	public static LocalDateTime getDateTime(BOP bop) {
		if(validateBOPName(bop)) {
			try {
				return LocalDateTime.parse(bop.getName().substring(7, 19), nameParseFormatter);
			}catch (DateTimeParseException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}

	public static int getOrderID(BOP bop) {
		if(validateBOPName(bop)) {
			try {
				return Integer.parseInt(bop.getName().substring(24, 26));
			}catch(NumberFormatException e) {
				logger.error(e.getMessage());
			}
		}
		return 0;
	}

	public static double getTotal(BOP bop) {
		if(validateBOPName(bop)) {
			try {
				return (Integer.parseInt(bop.getName().substring(26, 35)) / 100.0);
			}catch(NumberFormatException e) {
				logger.error(e.getMessage());
			}
		}
		return 0;
	}

	public static String getLocation(BOP bop) {
		if(validateBOPName(bop)) {
			return bop.getName().substring(35).replace(".bop", "");
		}
		return null;
	}

	public static TransactionType parseTransactionType(BOP bop) {
		List<String> lines = Arrays.asList(bop.getLines());
		ArrayList<String> markers = new ArrayList<String>();
		for(TransactionType tt : TransactionType.values()) {
			if(tt.getMarker() != null) {
				markers.add(tt.getMarker());
			}
		}
		int typeIndex = indexOf(lines, 0, new LineFilter() {
			public boolean accept(String line) {
				for(String marker : markers.toArray(new String[markers.size()])) {
					if(line.contains(marker))
						return true;
				}
				return false;
			}
		});
		if(typeIndex == -1)
			return TransactionType.defaultType();
		String line = lines.get(typeIndex);
		for(TransactionType tt : TransactionType.values()) {
			if(tt.getMarker() != null) {
				if(line.contains(tt.getMarker())) {
					if(tt == TransactionType.Grubhub ||
							tt == TransactionType.Uber ||
							tt == TransactionType.DoorDash)
						tt = TransactionType.Delivery;
					return tt;
				}
			}
		}
		return null;
	}

	public static int parseSide(BOP bop) {
		List<String> lines = Arrays.asList(bop.getLines());
		int sideIndex = indexOf(lines, 0, line -> line.contains("Side"));
		if(sideIndex != -1) {
			try {
				return Integer.parseInt(lines.get(sideIndex).substring(4,5).replace(" ", ""));
			}catch(NumberFormatException e) {}
		}
		return -1;
	}

	public static double parseSubtotal(BOP bop) {
		List<String> lines = Arrays.asList(bop.getLines());
		int subtotalIndex = indexOf(lines, 0, line -> line.contains("Subtotal"));
		if(subtotalIndex != -1) {
			try {
				return Double.parseDouble(lines.get(subtotalIndex).replace("Subtotal", ""));
			} catch (NumberFormatException e) {}
		}
		return -1;
	}

	public static double parseTax(BOP bop) {
		List<String> lines = Arrays.asList(bop.getLines());
		int taxIndex = indexOf(lines, 0, line -> line.contains("Tax"));
		if(taxIndex != -1) {
			try {
				return Double.parseDouble(lines.get(taxIndex).replace("Tax", ""));
			} catch (NumberFormatException e) {}
		}
		return -1;
	}

	public static double parseCashTendered(BOP bop) {
		List<String> lines = Arrays.asList(bop.getLines());
		int cashIndex = indexOf(lines, 0, line -> line.contains("Cash Tendered"));
		if(cashIndex != -1) {
			try {
				return Double.parseDouble(lines.get(cashIndex).replace("Cash Tendered", ""));
			} catch (NumberFormatException e) {}
		}
		return -1;
	}

	public static double parseGiftCard(BOP bop) {
		List<String> lines = Arrays.asList(bop.getLines());
		int giftcardIndex = indexOf(lines, 0, line -> line.contains("Gift Card"));
		if(giftcardIndex != -1) {
			try {
				return Double.parseDouble(lines.get(giftcardIndex).replace("Gift Card", ""));
			} catch (NumberFormatException e) {}
		}
		return -1;
	}

	public static double parseChange(BOP bop) {
		List<String> lines = Arrays.asList(bop.getLines());
		int changeIndex = indexOf(lines, 0, line -> line.contains("Change"));
		if(changeIndex != -1) {
			try {
				return Double.parseDouble(lines.get(changeIndex).replace("Change", ""));
			} catch (NumberFormatException e) {}
		}
		return -1;
	}

	public static double parseCashless(BOP bop) {
		List<String> lines = Arrays.asList(bop.getLines());
		int cashlessIndex = indexOf(lines, 0, line -> line.contains("Cashless"));
		if(cashlessIndex != -1) {
			try {
				return Double.parseDouble(lines.get(cashlessIndex).replace("Cashless", ""));
			} catch (NumberFormatException e) {}
		}
		return -1;
	}

	public static double parseTotalSavings(BOP bop) {
		List<String> lines = Arrays.asList(bop.getLines());
		int totalSavingsIndex = indexOf(lines, 0, line -> line.contains("Total Savings"));
		if(totalSavingsIndex != -1) {
			try {
				return Double.parseDouble(lines.get(totalSavingsIndex).replace("Total Savings", ""));
			} catch (NumberFormatException e) {}
		}
		return -1;
	}

	private static boolean validateBOPName(BOP bop) {
		return (bop.getName().startsWith("RCP") && bop.getName().length() >= 37);
	}

	private static int indexOf(List<String> list, int startIndex, LineFilter filter) {
		return indexOf(list, startIndex, filter, line -> false);
	}

	private static int indexOf(List<String> list, int startIndex, LineFilter filter, LineFilter abortFilter) {
		if (list == null) {
			return -1;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for(int i = startIndex; i < list.size(); i++) {
			String line = list.get(i);
			if(abortFilter == null && line == null) {
				return -1;
			}else if(abortFilter.accept(line)) {
				return -1;
			}else if(filter == null && line == null) {
				return i;
			}else if(filter.accept(line)) {
				return i;
			}
		}
		return -1;
	}

	interface LineFilter{
		public boolean accept(String line);
	}

}
