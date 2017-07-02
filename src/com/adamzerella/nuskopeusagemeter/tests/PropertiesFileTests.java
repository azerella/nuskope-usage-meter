package com.adamzerella.nuskopeusagemeter.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.adamzerella.nuskopeusagemeter.PropertiesFile;
import com.adamzerella.nuskopeusagemeter.UsageMeterFrame;

public class PropertiesFileTests {
	PropertiesFile prop = new PropertiesFile(new UsageMeterFrame(200,300));

	@Test
	public void writeXPosition() {
		try {
			this.prop.writeXPosition();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	public void writeXPositionMissing() {
		PropertiesFile p = new PropertiesFile(null);

		try {
			p.writeXPosition();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	public void writeYPosition() {
		try {
			this.prop.writeYPosition();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	public void writeToken() {
		try {
			this.prop.writeToken("123");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

}
