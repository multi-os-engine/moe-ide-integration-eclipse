/*
 * Copyright (C) 2016 Migeran
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.moe.utils.logger;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.resources.ResourcesPlugin;

public class LoggerFactory {

	/**
	 * Configured flag.
	 */
	private static boolean isConfigured;

	public static Logger getLogger(Class<?> clazz) {
		configure();
		return Logger.getLogger(clazz);
	}

	private synchronized static void configure() {
		if (isConfigured) {
			return;
		}
		Properties p = new Properties();
		p.setProperty("log4j.rootLogger", "DEBUG, Console, R");
		p.setProperty("log4j.appender.Console", "org.apache.log4j.ConsoleAppender");
		p.setProperty("log4j.appender.Console.layout", "org.apache.log4j.PatternLayout");
		p.setProperty("log4j.appender.Console.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n");
		p.setProperty("log4j.appender.R", "org.apache.log4j.RollingFileAppender");
		p.setProperty("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
		p.setProperty("log4j.appender.R.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n");
		p.setProperty("log4j.appender.R.File",
				ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + "/.metadata/.log");
		PropertyConfigurator.configure(p);
		isConfigured = true;
	}

}
