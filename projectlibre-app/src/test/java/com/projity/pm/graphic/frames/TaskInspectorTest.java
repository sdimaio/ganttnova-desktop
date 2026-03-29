package com.projity.pm.graphic.frames;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.projity.datatype.Duration;
import com.projity.datatype.TimeUnit;
import com.projity.init.Init;
import com.projity.pm.dependency.DependencyService;
import com.projity.pm.dependency.DependencyType;
import com.projity.pm.resource.Resource;
import com.projity.pm.scheduling.ConstraintType;
import com.projity.pm.scheduling.SchedulingType;
import com.projity.pm.task.NormalTask;
import com.projity.pm.task.Project;
import com.projity.pm.task.ProjectFactory;
import com.projity.session.CreateOptions;
import com.projity.strings.Messages;
import com.projity.util.DateTime;
import com.projity.util.Environment;

public class TaskInspectorTest {
	@BeforeClass
	public static void initializeRuntime() {
		LogManager.getLogManager().reset();
		Logger.getLogger("").setLevel(Level.WARNING);
		Environment.setStandAlone(true);
		Init.initialize();
	}

	@Test
	public void reportIncludesCurrentSchedulingFactors() throws Exception {
		Project project = newProject();
		Resource resource = project.getResourcePool().newResourceInstance();
		resource.setName("me");
		resource.setDefaultAssignmentUnits(0.5D);

		NormalTask predecessor = project.newNormalTaskInstance(false);
		predecessor.setName("Analysis");

		NormalTask task = project.newNormalTaskInstance(false);
		task.setName("Implementation");
		task.setSchedulingType(SchedulingType.FIXED_WORK);
		task.setConstraintType(ConstraintType.SNET);
		task.setConstraintDate(DateTime.midnightToday());
		task.setResourceNames("me");
		task.setEffort(Duration.getInstance(3, TimeUnit.DAYS), null);
		task.setLevelingDelay(Duration.getInstance(1, TimeUnit.DAYS));
		DependencyService.getInstance().newDependency(predecessor, task, DependencyType.FS, 0L, this);

		String report = TaskInspector.buildReport(task);

		assertTrue(report.contains(Messages.getString("TaskInspectorDialog.SectionFactors")));
		assertTrue(report.contains("Implementation"));
		assertTrue(report.contains("Analysis"));
		assertTrue(report.contains("me[50%]"));
		assertTrue(report.contains(Messages.getString("TaskInspectorDialog.FactorPredecessors")));
		assertTrue(report.contains(Messages.getString("TaskInspectorDialog.FactorConstraint")));
		assertTrue(report.contains(Messages.getString("TaskInspectorDialog.FactorResources")));
		assertTrue(report.contains(Messages.getString("TaskInspectorDialog.FactorLeveling")));
	}

	@Test
	public void reportFallsBackToNoDriversWhenTaskIsUnconstrained() {
		Project project = newProject();
		NormalTask task = project.newNormalTaskInstance(false);
		task.setName("Backlog");

		String report = TaskInspector.buildReport(task);

		assertTrue(report.contains("Backlog"));
		assertTrue(report.contains(Messages.getString("TaskInspectorDialog.FactorNoDrivers")));
	}

	private Project newProject() {
		CreateOptions options = new CreateOptions();
		options.setLocal(true);
		options.setName("Task inspector test " + System.nanoTime());
		options.setAddResources(false);
		options.setVerify(false);
		options.setSync(false);

		return ProjectFactory.getInstance().createProject(options);
	}
}
