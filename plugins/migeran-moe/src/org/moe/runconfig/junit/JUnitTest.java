/*
 * Copyright (C) 2009 The Android Open Source Project
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

package org.moe.runconfig.junit;

import org.eclipse.jdt.internal.junit.runner.ITestIdentifier;
import org.eclipse.jdt.internal.junit.runner.ITestReference;
import org.eclipse.jdt.internal.junit.runner.TestExecution;

@SuppressWarnings("restriction")
public abstract class JUnitTest implements ITestReference, ITestIdentifier {

	public ITestIdentifier getIdentifier() {
		return this;
	}

	public void run(TestExecution execution) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ITestIdentifier) {
			ITestIdentifier testid = (ITestIdentifier) obj;
			return getName().equals(testid.getName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

}