package com.jakewharton.mosaic.terminal

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jakewharton.mosaic.terminal.event.KeyboardEvent
import com.jakewharton.mosaic.terminal.event.KeyboardEvent.Companion.ModifierCtrl
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class TerminalParserGroundKeyboardEventTest : BaseTerminalParserTest() {
	@Test fun graphic() = runTest {
		for (codepoint in 0x20..0x7f) {
			val hex = codepoint.toString(16)
			writer.writeHex(hex)
			assertThat(reader.next(), hex).isEqualTo(KeyboardEvent(codepoint))
		}
	}

	@Test fun ctrlShiftAt() = runTest {
		writer.writeHex("00")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('@'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlA() = runTest {
		writer.writeHex("01")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('a'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlB() = runTest {
		writer.writeHex("02")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('b'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlC() = runTest {
		writer.writeHex("03")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('c'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlD() = runTest {
		writer.writeHex("04")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('d'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlE() = runTest {
		writer.writeHex("05")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('e'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlF() = runTest {
		writer.writeHex("06")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('f'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlG() = runTest {
		writer.writeHex("07")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('g'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlH() = runTest {
		writer.writeHex("08")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(0x7f))
	}

	@Test fun ctrlI() = runTest {
		writer.writeHex("09")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(0x09))
	}

	@Test fun ctrlJ() = runTest {
		writer.writeHex("0a")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(0x0d))
	}

	@Test fun ctrlK() = runTest {
		writer.writeHex("0b")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('k'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlL() = runTest {
		writer.writeHex("0c")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('l'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlM() = runTest {
		writer.writeHex("0d")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(0x0d))
	}

	@Test fun ctrlN() = runTest {
		writer.writeHex("0e")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('n'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlO() = runTest {
		writer.writeHex("0f")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('o'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlP() = runTest {
		writer.writeHex("10")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('p'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlQ() = runTest {
		writer.writeHex("11")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('q'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlR() = runTest {
		writer.writeHex("12")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('r'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlS() = runTest {
		writer.writeHex("13")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('s'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlT() = runTest {
		writer.writeHex("14")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('t'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlU() = runTest {
		writer.writeHex("15")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('u'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlV() = runTest {
		writer.writeHex("16")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('v'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlW() = runTest {
		writer.writeHex("17")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('w'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlX() = runTest {
		writer.writeHex("18")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('x'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlY() = runTest {
		writer.writeHex("19")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('y'.code, modifiers = ModifierCtrl))
	}

	@Test fun ctrlZ() = runTest {
		writer.writeHex("1a")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('z'.code, modifiers = ModifierCtrl))
	}

	@Test fun bareEscape() = runTest {
		writer.writeHex("1b")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(0x1b))
	}

	@Test fun hex1c() = runTest {
		writer.writeHex("1c")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(0x1c))
	}

	@Test fun hex1d() = runTest {
		writer.writeHex("1d")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(0x1d))
	}

	@Test fun hex1e() = runTest {
		writer.writeHex("1e")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(0x1e))
	}

	@Test fun hex1f() = runTest {
		writer.writeHex("1f")
		assertThat(reader.next()).isEqualTo(KeyboardEvent(0x1f))
	}

	@Test fun utf8TwoBytes() = runTest {
		writer.writeHex("cea9")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('Ω'.code))
	}

	@Test fun utf8ThreeBytes() = runTest {
		writer.writeHex("e28988")
		assertThat(reader.next()).isEqualTo(KeyboardEvent('≈'.code))
	}
}
