import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Test;

import com.t07m.mctransactions.receipt.BOP;
import com.t07m.mctransactions.receipt.ReceiptFormatter;

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

public class RecieptFormatterTest {

	@Test
	public void test() {
		BOP bop = new BOP("RCP00152021092102183200935000001049DT.bop", new String[] {
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
				"<@BoldOn>    22074-15350-92121-02184-00104-9    <@BoldOff>",
				"",
				"",
				"",
				"     McDonald's Restaurant #22074      ",
				"",
				"           3925 N 107TH AVE            ",
				"",
				"           AVONDALE,AZ 85323           ",
				"",
				"          TEL# 623  877  6011          <@BoldOff>",
				"",
				"",
				"",
				"<@ReverseOn><@BoldOn><@DoubleCharOn>  **** REFUND **** ",
				"",
				"<@DoubleCharOff><@BoldOff><@ReverseOff>",
				"",
				"KS# 15              09/21/2021 02:18 AM",
				"",
				"                               Order 35",
				"",
				"",
				"",
				"1 Qtr Cheese Meal                  9.64",
				"",
				"  1 M Coke                             ",
				"",
				"",
				"",
				"Subtotal                           9.64",
				"",
				"  Tax                              0.85",
				"",
				"Total Refund                      10.49",
				"",
				"",
				"",
				"Change                             0.00",
				"",
				"",
				"",
				"Authorized",
				"",
				"Signature:_________________________",
				"",
				"",
				"",
				"Address",
				"",
				"___________________________________",
				"",
				"",
				"",
				"Phone #",
				"",
				"___________________________________",
				"",
				"",
				"",
				"Reason",
				"",
				"___________________________________",
				"",
				"",
				"",
				"",
				"",
				"          feedback@sdrest.com          ",
				"",
				" Compliments or Concerns 800-755-7128  "
				
		});
		JFrame frame = new JFrame();
	    frame.setSize(236, 306);
	    frame.add(new JLabel(new ImageIcon(ReceiptFormatter.BOPToImage(bop))));
	    frame.setVisible(true);
	    frame.pack();
	    try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
