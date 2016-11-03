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

package org.anglur.vision.net.packet.out

import org.anglur.vision.net.packet.outgoingPacket


fun handshakeResponse(r: Response, key: Long) = outgoingPacket {
	buff.writeByte(r.ordinal)
	buff.writeLong(key)
}


enum class Response {
	
	ALLOW, DENY, INVALID_PASSWORD, TIMEOUT;
	
	companion object {
		
		val values = Response.values()
		
		operator fun get(i: Int) = values[i]
		
	}
	
}