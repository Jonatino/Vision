/*
 *     Vision - free remote desktop software built with Kotlin
 *     Copyright (C) 2016  Jonathan Beaudoin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.anglur.vision.util.extensions

import javafx.application.Platform

fun fxThread(start: Boolean = true, isDaemon: Boolean = false, contextClassLoader: ClassLoader? = null, name: String? = null, priority: Int = -1, block: () -> Unit): Thread {
	val thread = object : Thread() {
		override fun run() = Platform.runLater {
			block()
		}
	}
	
	if (isDaemon)
		thread.isDaemon = true
	if (priority > 0)
		thread.priority = priority
	if (name != null)
		thread.name = name
	if (contextClassLoader != null)
		thread.contextClassLoader = contextClassLoader
	if (start)
		thread.start()
	return thread
}