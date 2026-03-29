package com.projity.pm.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.projity.datatype.Duration;
import com.projity.datatype.TimeUnit;
import com.projity.init.Init;
import com.projity.options.ScheduleOption;
import com.projity.pm.assignment.Assignment;
import com.projity.pm.scheduling.SchedulingType;
import com.projity.session.CreateOptions;
import com.projity.util.Environment;

public class NormalTaskEffortTest {
	@BeforeClass
	public static void initializeRuntime() {
		LogManager.getLogManager().reset();
		Logger.getLogger("").setLevel(Level.WARNING);
		Environment.setStandAlone(true);
		Init.initialize();
	}

	@Test
	public void setEffortStoresUnderlyingWorkInPersonDays() {
		NormalTask task = newTask();

		task.setEffort(Duration.getInstance(5, TimeUnit.DAYS), null);

		assertEquals(5.0D, Duration.getValue(Duration.setAsDays(task.getWork(null))), 0.0001D);
		assertEquals(5.0D, Duration.getValue(task.getEffort(null)), 0.0001D);
		assertFalse(task.isEstimated());
		assertFalse(Duration.isEstimated(task.getDuration()));
		assertFalse(task.isReadOnlyEffort(null));
	}

	@Test
	public void effortAliasAlwaysReturnsDaysEvenWhenWorkDisplayUsesHours() {
		NormalTask task = newTask();
		int previousWorkUnit = ScheduleOption.getInstance().getWorkUnit();

		try {
			ScheduleOption.getInstance().setEffortDisplay(TimeUnit.HOURS);
			task.setEffort(Duration.getInstance(2, TimeUnit.DAYS), null);

			assertEquals(TimeUnit.DAYS, Duration.getType(task.getEffort(null)));
			assertEquals(2.0D, Duration.getValue(task.getEffort(null)), 0.0001D);
		} finally {
			ScheduleOption.getInstance().setEffortDisplay(previousWorkUnit);
		}
	}

	@Test
	public void fixedWorkSchedulingStillRecalculatesDurationFromEffort() {
		NormalTask task = newTask();

		task.setSchedulingType(SchedulingType.FIXED_WORK);
		task.setEffort(Duration.getInstance(3, TimeUnit.DAYS), null);

		assertEquals(SchedulingType.FIXED_WORK, task.getSchedulingType());
		assertEquals(3.0D, Duration.getValue(Duration.setAsDays(task.getWork(null))), 0.0001D);
		assertEquals(3.0D, Duration.getValue(Duration.setAsDays(task.getDuration())), 0.0001D);
		assertFalse(Duration.isEstimated(task.getDuration()));
	}

	@Test
	public void reducingAssignmentUnitsExpandsDurationWithoutLeavingEstimatedMarker() {
		NormalTask task = newTask();
		task.setSchedulingType(SchedulingType.FIXED_WORK);
		task.setEffort(Duration.getInstance(3, TimeUnit.DAYS), null);

		Assignment assignment = (Assignment) task.getAssignments().getFirst();
		assignment.adjustRemainingUnits(0.5D, 1.0D, true, false);

		assertEquals(6.0D, Duration.getValue(Duration.setAsDays(task.getDuration())), 0.0001D);
		assertEquals(3.0D, Duration.getValue(Duration.setAsDays(task.getWork(null))), 0.0001D);
		assertFalse(task.isEstimated());
		assertFalse(Duration.isEstimated(task.getDuration()));
	}

	private NormalTask newTask() {
		CreateOptions options = new CreateOptions();
		options.setLocal(true);
		options.setName("Effort test " + System.nanoTime());
		options.setAddResources(false);
		options.setVerify(false);
		options.setSync(false);

		Project project = ProjectFactory.getInstance().createProject(options);
		return project.newNormalTaskInstance(false);
	}
}
