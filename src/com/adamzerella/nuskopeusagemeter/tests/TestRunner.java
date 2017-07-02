package com.adamzerella.nuskopeusagemeter.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	DataNodeTests.class,
	PropertiesFileTests.class,
	UITests.class,
})

public class TestRunner {}
