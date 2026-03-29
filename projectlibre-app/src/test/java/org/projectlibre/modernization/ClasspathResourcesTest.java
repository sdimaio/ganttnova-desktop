package org.projectlibre.modernization;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

public class ClasspathResourcesTest {
	@Test
	public void bundledLicenseResourcesAreAvailableOnClasspath() {
		assertResourceExists("license/index.html");
		assertResourceExists("license/index_html_0.gif");
		assertResourceExists("license/third-party/index.html");
	}

	private static void assertResourceExists(String path) {
		try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
			assertNotNull("Missing classpath resource: " + path, stream);
		} catch (Exception e) {
			throw new AssertionError("Unable to read classpath resource: " + path, e);
		}
	}
}
