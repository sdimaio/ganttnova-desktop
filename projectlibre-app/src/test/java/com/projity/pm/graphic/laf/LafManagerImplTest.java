package com.projity.pm.graphic.laf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LafManagerImplTest {
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(LafManagerImpl.class);

	@Before
	@After
	public void clearLookAndFeelPreference() throws BackingStoreException {
		PREFERENCES.clear();
	}

	@Test
	public void availableLookAndFeelOptionsExposeSafeFallbacks() {
		LafManagerImpl lafManager = new LafManagerImpl(null);

		assertEquals("auto", lafManager.getAvailableLookAndFeelIds()[0]);
		assertTrue(Arrays.asList(lafManager.getAvailableLookAndFeelIds()).contains("system"));
		assertTrue(Arrays.asList(lafManager.getAvailableLookAndFeelIds()).contains("metal"));
	}

	@Test
	public void preferredLookAndFeelSelectionIsPersistedAndValidated() {
		LafManagerImpl lafManager = new LafManagerImpl(null);

		assertEquals("auto", lafManager.getPreferredLookAndFeelId());
		assertTrue(lafManager.setPreferredLookAndFeel("system"));
		assertEquals("system", lafManager.getPreferredLookAndFeelId());
		assertFalse(lafManager.setPreferredLookAndFeel("system"));
		assertFalse(lafManager.setPreferredLookAndFeel("does-not-exist"));
		assertEquals("system", lafManager.getPreferredLookAndFeelId());
	}
}
