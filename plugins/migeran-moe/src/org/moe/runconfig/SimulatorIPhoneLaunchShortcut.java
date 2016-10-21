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

package org.moe.runconfig;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.moe.utils.SimCtl;

public class SimulatorIPhoneLaunchShortcut extends AbstractLaunchShortcut {

	public static final String SIMULATOR_NAME = "iPhone";
	public static final String SIMULATOR_CONFIGURATION_SUFFIX = " (" + SIMULATOR_NAME + ")";

	@Override
	protected void setTargetDevice(ILaunchConfigurationWorkingCopy workingCopy) {
		for (SimCtl.Device device : SimCtl.getDevices()) {
			workingCopy.setAttribute(ApplicationManager.RUN_ON_SIMULATOR_KEY, true);
			if (device.name.startsWith(getSimulatorName())) {
				workingCopy.setAttribute(ApplicationManager.SIMULATOR_UUID_KEY, device.udid);
				break;
			}
		}
	}

	protected String getSimulatorName() {
		return SIMULATOR_NAME;
	}

	@Override
	protected String getConfigurationSuffix() {
		return SIMULATOR_CONFIGURATION_SUFFIX;
	}

}