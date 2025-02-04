package com.jakewharton.mosaic.terminal

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jakewharton.mosaic.terminal.event.KeyboardEvent
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.Down
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.End
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.F1
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.F2
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.F3
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.F4
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.Home
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.Left
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.Right
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.Up
import com.jakewharton.mosaic.terminal.event.OperatingStatusResponseEvent
import com.jakewharton.mosaic.terminal.event.UnknownEvent
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class TerminalParserSs3KeyboardEventTest : BaseTerminalParserTest() {
	@Test fun up() = runTest {
		writer.writeHex("1b4f41")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(Up))
	}

	@Test fun down() = runTest {
		writer.writeHex("1b4f42")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(Down))
	}

	@Test fun right() = runTest {
		writer.writeHex("1b4f43")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(Right))
	}

	@Test fun left() = runTest {
		writer.writeHex("1b4f44")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(Left))
	}

	@Test fun end() = runTest {
		writer.writeHex("1b4f46")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(End))
	}

	@Test fun home() = runTest {
		writer.writeHex("1b4f48")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(Home))
	}

	@Test fun f1() = runTest {
		writer.writeHex("1b4f50")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(F1))
	}

	@Test fun f2() = runTest {
		writer.writeHex("1b4f51")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(F2))
	}

	@Test fun f3() = runTest {
		writer.writeHex("1b4f52")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(F3))
	}

	@Test fun f4() = runTest {
		writer.writeHex("1b4f53")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(F4))
	}

	@Test fun invalidKey() = runTest {
		writer.writeHex("1b4f75")
		assertThat(reader.next()).isEqualTo(
			UnknownEvent("1b4f75".hexToByteArray()),
		)
	}

	@Test fun keyIsEscapeDoesNotConsumeEscape() = runTest {
		writer.writeHex("1b4f1b5b306e")
		assertThat(reader.next()).isEqualTo(
			UnknownEvent("1b4f".hexToByteArray()),
		)
		assertThat(reader.next()).isEqualTo(OperatingStatusResponseEvent(ok = true))
	}
}
