package com.projity.pm.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.projity.datatype.Duration;
import com.projity.datatype.TimeUnit;
import com.projity.init.Init;
import com.projity.pm.assignment.Assignment;
import com.projity.pm.resource.Resource;
import com.projity.pm.scheduling.SchedulingType;
import com.projity.session.CreateOptions;
import com.projity.util.Environment;

public class ResourceDefaultAllocationTest {
	@BeforeClass
	public static void initializeRuntime() {
		LogManager.getLogManager().reset();
		Logger.getLogger("").setLevel(Level.WARNING);
		Environment.setStandAlone(true);
		Init.initialize();
	}

	@Test
	public void resourceDefaultAllocationIsAppliedWhenUnitsAreOmitted() throws Exception {
		Project project = newProject();
		Resource resource = project.getResourcePool().newResourceInstance();
		resource.setName("me");
		resource.setDefaultAssignmentUnits(0.5D);

		NormalTask task = project.newNormalTaskInstance(false);
		task.setSchedulingType(SchedulingType.FIXED_WORK);
		task.setResourceNames("me");
		task.setEffort(Duration.getInstance(3, TimeUnit.DAYS), null);

		Assignment assignment = (Assignment) task.getAssignments().getFirst();
		assertEquals(0.5D, assignment.getUnits(), 0.0001D);
		assertTrue(task.getResourceNames().contains("me[50%]"));
		assertEquals(6.0D, Duration.getValue(Duration.setAsDays(task.getDuration())), 0.0001D);
		assertFalse(task.isEstimated());
		assertFalse(Duration.isEstimated(task.getDuration()));
	}

	@Test
	public void explicitAllocationOverridesResourceDefault() throws Exception {
		Project project = newProject();
		Resource resource = project.getResourcePool().newResourceInstance();
		resource.setName("me");
		resource.setDefaultAssignmentUnits(0.5D);

		NormalTask task = project.newNormalTaskInstance(false);
		task.setResourceNames("me[75%]");

		Assignment assignment = (Assignment) task.getAssignments().getFirst();
		assertEquals(0.75D, assignment.getUnits(), 0.0001D);
		assertTrue(task.getResourceNames().contains("me[75%]"));
	}

	private Project newProject() {
		CreateOptions options = new CreateOptions();
		options.setLocal(true);
		options.setName("Allocation test " + System.nanoTime());
		options.setAddResources(false);
		options.setVerify(false);
		options.setSync(false);

		return ProjectFactory.getInstance().createProject(options);
	}
}
