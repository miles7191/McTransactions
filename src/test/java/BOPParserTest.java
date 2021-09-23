import org.junit.Test;

import com.t07m.mctransactions.receipt.BOP;
import com.t07m.mctransactions.receipt.BOPParser;

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

public class BOPParserTest {

	@Test
	public void test() {
		BOP bop = new BOP("RCP00152021092102183200935000001049DT.bop", new String[] {
				"",
				"",
				"<@BigCharOn>  1335<@BigCharOff>",
				"",
				"",
				"",
				"<@ReverseOn><@BoldOn><@DoubleCharOn>  **** PROMO **** <@DoubleCharOff><@BoldOff><@ReverseOff>",
				"",
				"",
				"",
				"<@BoldOn> Visit www.mcdvoice.com within 7 days  ",
				"",
				" and tell us about your visit. Rate us ",
				"",
				"     HIGHLY SATISFIED. Bring this      ",
				"",
				"       receipt and purchase any        ",
				"",
				"sandwich at regular price and receive a",
				"",
				"  second sandwich of equal or lesser   ",
				"",
				"             value FREE!!              ",
				"",
				"    Validation Code:______________     ",
				"",
				"  Expires 30 days after receipt date.  ",
				"",
				" Valid at participating US McDonald's. ",
				"",
				"             Survey Code:              ",
				"",
				"<@BoldOn>    23008-13350-92221-13223-00222-2    <@BoldOff>",
				"",
				"",
				"",
				"     McDonald's Restaurant #23008      ",
				"",
				"        719 E THUNDERBIRD ROAD         ",
				"",
				"           PHOENIX,AZ 85020            ",
				"",
				"          TEL# 602  866  8575          <@BoldOff>",
				"",
				"",
				"",
				"       Thank You Rewards Member!       ",
				"",
				"",
				"",
				"KS# 13              09/22/2021 01:22 PM",
				"",
				"<@BoldOn>Side1 <@BoldOff>                         Order 35",
				"",
				"",
				"",
				"Buy one Happy Meal, get one FREE Daily ",
				"",
				"  1 Happy Meal 6 Pc                4.99",
				"",
				"    1 BBQ Sauce                        ",
				"",
				"    1 Extra Kids Fry                   ",
				"",
				"    1 Surprise Toy                     ",
				"",
				"    1 Apple Juice                      ",
				"",
				"  1 Happy Meal 6 Pc           1P   0.00",
				"",
				"    1 Creamy Ranch Cup                 ",
				"",
				"    1 Extra Kids Fry                   ",
				"",
				"    1 Surprise Toy                     ",
				"",
				"    1 Apple Juice                      ",
				"",
				"1 Oreo McFlurry                    3.59",
				"",
				"1 Filet-O-Fish Ml-Lrg              8.89",
				"",
				"  1 S Vanilla Shake                1.99",
				"",
				"              <Drink Upcharge>",
				"",
				"1 L Sprite                         1.00",
				"",
				"",
				"",
				"Subtotal                          20.46",
				"",
				"  Tax                              1.76",
				"",
				"Take-Out Total                    22.22",
				"",
				"",
				"",
				"Cashless                          22.22",
				"",
				"Change                             0.00",
				"",
				"",
				"",
				"Total Savings                      4.99",
				"",
				"",
				"",
				"MER# 683525",
				"",
				"CARD ISSUER          ACCOUNT#",
				"",
				"Master SALE      ************5665",
				"",
				"TRANSACTION AMOUNT                22.22",
				"",
				"CHIP READ",
				"",
				"AUTHORIZATION CODE - 05829P",
				"",
				"SEQ# 075397",
				"",
				"AID: A0000000041010",
				"",
				"",
				"",
				"          feedback@sdrest.com          ",
				"",
				" Compliments or Concerns 800-755-7128  ",
				"",
				"",
				"",
				" Check your App to see points earned!  ",
				""				
		});
		System.out.println(BOPParser.getKS(bop));
		System.out.println(BOPParser.getDateTime(bop));
		System.out.println(BOPParser.getOrderID(bop));
		System.out.println(BOPParser.getTotal(bop));
		System.out.println(BOPParser.getLocation(bop));
		System.out.println(BOPParser.parseTransactionType(bop));
		System.out.println(BOPParser.parseSide(bop));
		System.out.println(BOPParser.parseSubtotal(bop));
		System.out.println(BOPParser.parseTax(bop));
		System.out.println(BOPParser.parseCashTendered(bop));
		System.out.println(BOPParser.parseChange(bop));
		System.out.println(BOPParser.parseCashless(bop));
		System.out.println(BOPParser.parseTotalSavings(bop));
	}

}
