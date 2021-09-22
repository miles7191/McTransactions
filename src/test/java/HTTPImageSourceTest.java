import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Test;

import com.t07m.mctransactions.net.HTTPImageSource;

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

public class HTTPImageSourceTest {

	@Test
	public void test() {
		HTTPImageSource source = new HTTPImageSource("http://10.0.0.61/cgi-bin/camera?resolution=640", null, null);
		JFrame frame = new JFrame();
	    frame.setSize(236, 306);
	    frame.add(new JLabel(new ImageIcon(source.pullImage())));
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
