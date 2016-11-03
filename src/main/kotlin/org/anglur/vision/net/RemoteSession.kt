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

package org.anglur.vision.net

import io.netty.channel.Channel
import org.anglur.vision.guid.UID
import org.anglur.vision.util.extensions.rand
import org.anglur.vision.view.DesktopFrame
import org.anglur.vision.view.VisionGUI
import kotlin.concurrent.thread

class RemoteSession(val id: String, val password: String) {
	
	val address = UID.raw()
	
	lateinit var channel: Channel
	
	val secret = rand(Long.MIN_VALUE..Long.MAX_VALUE)
	
	private var startTime = System.currentTimeMillis()
	
	lateinit var desktopFrame: DesktopFrame
	
	var connected = false
	
	init {
		println("$id, $secret")
		Sessions += this
	}
	
	fun connect() {
		if (connected) return
		
		udp {
			println("Rite")
			connected = true
			
			desktopFrame = DesktopFrame.show()
			
			thread {
				var iterations: Int = 0
				var time: Long = 0
				while (desktopFrame.isShowing) {
					val stamp = System.currentTimeMillis()
					desktopFrame.display(VisionGUI.captureMode.capture())
					time += System.currentTimeMillis() - stamp
					
					if (iterations++ % 100 == 0) {
						println("Took " + time / iterations.toDouble() + "ms avg per frame (over 100 frames)")
					}
				}
			}
		}.bind(43594).await()
	}
	
	fun disconnect() {
		connected = false
		println("disconnected session")
		desktopFrame.closeModal()
	}
	
}

object Sessions {
	
	private val sessions = mutableMapOf<Long, RemoteSession>()
	private val secrets = mutableMapOf<String, Long>()
	
	operator fun get(secret: Long) = sessions[secret]
	
	operator fun get(id: String) = sessions[secrets[id]]!!
	
	infix operator fun plusAssign(session: RemoteSession) {
		sessions.put(session.secret, session)
		secrets.put(session.id, session.secret)
		
		println(secrets)
		println(sessions)
	}
	
	infix operator fun minusAssign(session: RemoteSession) {
		sessions.remove(session.secret)
		secrets.remove(session.id, session.secret)
	}
	
}