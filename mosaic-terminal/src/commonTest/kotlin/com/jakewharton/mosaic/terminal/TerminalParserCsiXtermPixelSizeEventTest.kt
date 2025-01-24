package com.jakewharton.mosaic.terminal

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jakewharton.mosaic.terminal.event.UnknownEvent
import com.jakewharton.mosaic.terminal.event.XtermPixelSizeEvent
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class TerminalParserCsiXtermPixelSizeEventTest : BaseTerminalParserTest() {
	@Test fun basic() = runTest {
		writer.writeHex("1b5b343b313b3274")
		assertThat(reader.next()).isEqualTo(XtermPixelSizeEvent(1, 2))
	}

	@Test fun emptyParameterFails() = runTest {
		writer.writeHex("1b5b343b3b3274")
		assertThat(reader.next()).isEqualTo(
			UnknownEvent("1b5b343b3b3274".hexToByteArray()),
		)
		writer.writeHex("1b5b343b313b74")
		assertThat(reader.next()).isEqualTo(
			UnknownEvent("1b5b343b313b74".hexToByteArray()),
		)
	}

	@Test fun nonDigitParameterFails() = runTest {
		writer.writeHex("1b5b343b223b3274")
		assertThat(reader.next()).isEqualTo(
			UnknownEvent("1b5b343b223b3274".hexToByteArray()),
		)
		writer.writeHex("1b5b343b313b2274")
		assertThat(reader.next()).isEqualTo(
			UnknownEvent("1b5b343b313b2274".hexToByteArray()),
		)
	}
}
