package com.jakewharton.mosaic.terminal

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jakewharton.mosaic.terminal.event.SystemThemeEvent
import com.jakewharton.mosaic.terminal.event.UnknownEvent
import kotlin.test.Test

class TerminalParserCsiSystemThemeEventTest : BaseTerminalParserTest() {
	@Test fun dark() {
		writer.writeHex("1b5b3f3939373b316e")
		assertThat(parser.next()).isEqualTo(SystemThemeEvent(isDark = true))
	}

	@Test fun light() {
		writer.writeHex("1b5b3f3939373b326e")
		assertThat(parser.next()).isEqualTo(SystemThemeEvent(isDark = false))
	}

	@Test fun missingP2() {
		writer.writeHex("1b5b3f3939373b6e")
		assertThat(parser.next()).isEqualTo(
			UnknownEvent("1b5b3f3939373b6e".hexToByteArray()),
		)
	}

	@Test fun unknownP2() {
		writer.writeHex("1b5b3f3939373b346e")
		assertThat(parser.next()).isEqualTo(
			UnknownEvent("1b5b3f3939373b346e".hexToByteArray()),
		)
	}

	@Test fun nonDigitP2() {
		writer.writeHex("1b5b3f3939373b2b6e")
		assertThat(parser.next()).isEqualTo(
			UnknownEvent("1b5b3f3939373b2b6e".hexToByteArray()),
		)
	}

	@Test fun tooLongP2() {
		writer.writeHex("1b5b3f3939373b31316e")
		assertThat(parser.next()).isEqualTo(
			UnknownEvent("1b5b3f3939373b31316e".hexToByteArray()),
		)
	}
}
