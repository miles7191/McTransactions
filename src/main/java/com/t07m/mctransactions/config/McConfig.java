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
package com.t07m.mctransactions.config;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.cubespace.Yamler.Config.YamlConfig;

public class McConfig extends YamlConfig {

	private @Getter @Setter String appName = "McTransactions";
	private @Getter @Setter String NSN = "00000";
	private @Getter @Setter UserConfig[] Users = new UserConfig[] {
			new UserConfig(0, false, "User", "example@gmail.com", new int[] {0})
	};
	private @Getter @Setter AlertConfig[] Alerts = new AlertConfig[] {
			new AlertConfig(0, false, "Refund Alert", "Refund", 1.0, -1, -1, -1)
	};
	private @Getter @Setter KeyStationConfig[] KeyStations = new KeyStationConfig[] {
			new KeyStationConfig(0, false, "127.0.0.1", "", "", "", "", "", new int[] {})
	};
	private @Getter @Setter CameraConfig[] Cameras = new CameraConfig[] {
			new CameraConfig(0, false, "http://192.168.0.101/cgi-bin/camera", "admin", "12345")
	};
	private @Getter @Setter String GmailUser = "";
	private @Getter @Setter String GmailPassword = "";
	private @Getter @Setter String EmailTemplate = "<div style=\"font-family:Verdana,serif\" bgcolor=\"#FFFFFF\"><table width=\"100%\" bgcolor=\"#FFFFFF\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><td align=\"center\"><table width=\"95%\" cellpadding=\"10\" cellspacing=\"0\" style=\"font-family:Verdana,serif;margin-top:10px\"><tbody><tr style=\"background:-webkit-linear-gradient(#008dc4,#008dc4);background:-moz-linear-gradient(#008dc4,#008dc4);background:linear-gradient(#008dc4,#008dc4)\"><td style=\"border-radius:8px 8px 0 0;font-family:Verdana,serif;font-size:18px\" colspan=\"2\"><h1 border=\"0\" align=\"left\" style=\"display:inline-block;color:#fff;margin:2;margin-left:10;font-size:1.25em\">Transaction Alert {NSN}</h1></td></tr><tr><td colspan=\"2\" bgcolor=q\"#FFFFFF\" style=\"background-color:#fff;border:1px solid #e3e4dc\"><table><tbody><tr><td valign=\"top\" style=\"font-family:Verdana,serif;color:#000000;font-size:14px\"><p>Hi {USER},<br><br>{KS} has triggered a {ALERT} alert with a total of {AMOUNT} and a total savings of {SAVINGS} at {TIME} on {DATE}.<br>Please review the attachments for further details.<br><br>Regards,<br>{CLIENT}</p></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></div></div>";
	
	public McConfig() {
		CONFIG_HEADER = new String[]{"McTransactions Configuration Data"};
		CONFIG_FILE = new File("config.yml");
	}

	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class KeyStationConfig extends YamlConfig{
		private @Getter @Setter int ID;
		private @Getter @Setter boolean Enabled;
		private @Getter @Setter String IP;
		private @Getter @Setter String Domain;
		private @Getter @Setter String Username;
		private @Getter @Setter String Password;
		private @Getter @Setter String ReprintPath;
		private @Getter @Setter String FriendlyName;
		private @Getter @Setter int[] Cameras;
	}	

	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class CameraConfig extends YamlConfig{
		private @Getter @Setter int ID;
		private @Getter @Setter boolean Enabled;
		private @Getter @Setter String url;
		private @Getter @Setter String username;
		private @Getter @Setter String password;
	}

	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class AlertConfig extends YamlConfig{
		private @Getter @Setter int ID;
		private @Getter @Setter boolean Enabled;
		private @Getter @Setter String FriendlyName;
		private @Getter @Setter String TransactionType;
		private @Getter @Setter double MinimumValue;
		private @Getter @Setter double MaximumValue;
		private @Getter @Setter double MinimumSavings;
		private @Getter @Setter double MaximumSavings;
	}

	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class UserConfig extends YamlConfig{
		private @Getter @Setter int ID;
		private @Getter @Setter boolean Enabled;
		private @Getter @Setter String FriendlyName;
		private @Getter @Setter String email;
		private @Getter @Setter int[] Alerts;
	}
}
