package com.jakewharton.mosaic.terminal

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jakewharton.mosaic.terminal.event.FocusEvent
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class TerminalParserCsiFocusEventTest : BaseTerminalParserTest() {
	@Test fun focusedTrue() = runTest {
		writer.writeHex("1b5b49")
		assertThat(reader.next()).isEqualTo(FocusEvent(focused = true))
	}

	@Test fun focusedFalse() = runTest {
		writer.writeHex("1b5b4f")
		assertThat(reader.next()).isEqualTo(FocusEvent(focused = false))
	}
}
