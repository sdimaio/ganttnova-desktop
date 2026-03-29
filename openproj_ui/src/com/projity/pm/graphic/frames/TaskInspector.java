/*
The contents of this file are subject to the Common Public Attribution License
Version 1.0 (the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at
http://www.projity.com/license . The License is based on the Mozilla Public
License Version 1.1 but Sections 14 and 15 have been added to cover use of
software over a computer network and provide for limited attribution for the
Original Developer. In addition, Exhibit A has been modified to be consistent
with Exhibit B.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
specific language governing rights and limitations under the License. The
Original Code is OpenProj. The Original Developer is the Initial Developer and
is Projity, Inc. All portions of the code written by Projity are Copyright (c)
2006, 2007. All Rights Reserved. Contributors Projity, Inc.

Alternatively, the contents of this file may be used under the terms of the
Projity End-User License Agreeement (the Projity License), in which case the
provisions of the Projity License are applicable instead of those above. If you
wish to allow use of your version of this file only under the terms of the
Projity License and not to allow others to use your version of this file under
the CPAL, indicate your decision by deleting the provisions above and replace
them with the notice and other provisions required by the Projity  License. If
you do not delete the provisions above, a recipient may use your version of this
file under either the CPAL or the Projity License.

[NOTE: The text of this license may differ slightly from the text of the notices
in Exhibits A and B of the license at http://www.projity.com/license. You should
use the latest text at http://www.projity.com/license for your modifications.
You may not remove this license text from the source files.]

Attribution Information: Attribution Copyright Notice: Copyright (c) 2006, 2007
Projity, Inc. Attribution Phrase (not exceeding 10 words): Powered by OpenProj,
an open source solution from Projity. Attribution URL: http://www.projity.com
Graphic Image as provided in the Covered Code as file:  openproj_logo.png with
alternatives listed on http://www.projity.com/logo

Display of Attribution Information is required in Larger Works which are defined
in the CPAL as a work which combines Covered Code or portions thereof with code
not governed by the terms of the CPAL. However, in addition to the other notice
obligations, all copies of the Covered Code in Executable and Source Code form
distributed must, as a form of attribution of the original author, include on
each user interface screen the "OpenProj" logo visible to all users.  The
OpenProj logo should be located horizontally aligned with the menu bar and left
justified on the top left of the screen adjacent to the File menu.  The logo
must be at least 100 x 25 pixels.  When users click on the "OpenProj" logo it
must direct them back to http://www.projity.com.
*/
package com.projity.pm.graphic.frames;

import java.text.MessageFormat;
import java.util.Iterator;

import com.projity.association.AssociationList;
import com.projity.configuration.Configuration;
import com.projity.datatype.DurationFormat;
import com.projity.field.Field;
import com.projity.pm.calendar.WorkCalendar;
import com.projity.pm.dependency.Dependency;
import com.projity.pm.dependency.DependencyType;
import com.projity.pm.scheduling.ConstraintType;
import com.projity.pm.task.Task;
import com.projity.pm.task.TaskSpecificFields;
import com.projity.strings.Messages;

final class TaskInspector {
	private TaskInspector() {
	}

	static String buildReport(Task task) {
		StringBuilder report = new StringBuilder();

		appendSection(report, Messages.getString("TaskInspectorDialog.SectionIdentity")); //$NON-NLS-1$
		appendField(report, "Field.name", task); //$NON-NLS-1$
		appendField(report, "Field.wbs", task); //$NON-NLS-1$

		appendBlankLine(report);
		appendSection(report, Messages.getString("TaskInspectorDialog.SectionSummary")); //$NON-NLS-1$
		appendField(report, "Field.start", task); //$NON-NLS-1$
		appendField(report, "Field.finish", task); //$NON-NLS-1$
		appendField(report, "Field.duration", task); //$NON-NLS-1$
		appendField(report, "Field.effort", task); //$NON-NLS-1$
		appendField(report, "Field.work", task); //$NON-NLS-1$
		appendNamedValue(report, getFieldName("Field.resourceNames"), formatResourceNames(task)); //$NON-NLS-1$
		appendField(report, "Field.taskType", task); //$NON-NLS-1$
		appendField(report, "Field.effortDriven", task); //$NON-NLS-1$
		appendNamedValue(report, getFieldName("Field.taskCalendar"), formatTaskCalendar(task)); //$NON-NLS-1$
		appendField(report, "Field.constraintType", task); //$NON-NLS-1$
		appendField(report, "Field.constraintDate", task); //$NON-NLS-1$
		appendField(report, "Field.deadline", task); //$NON-NLS-1$
		appendField(report, "Field.levelingDelay", task); //$NON-NLS-1$
		appendField(report, "Field.totalSlack", task); //$NON-NLS-1$
		appendField(report, "Field.freeSlack", task); //$NON-NLS-1$

		appendBlankLine(report);
		appendSection(report, Messages.getString("TaskInspectorDialog.SectionDependencies")); //$NON-NLS-1$
		appendField(report, "Field.predecessors", task); //$NON-NLS-1$
		appendField(report, "Field.successors", task); //$NON-NLS-1$
		appendDependencyList(report, Messages.getString("Text.predecessor"), task.getPredecessorList(), true); //$NON-NLS-1$
		appendDependencyList(report, Messages.getString("Text.successor"), task.getSuccessorList(), false); //$NON-NLS-1$

		appendBlankLine(report);
		appendSection(report, Messages.getString("TaskInspectorDialog.SectionFactors")); //$NON-NLS-1$
		appendFactors(report, task);

		return report.toString().trim();
	}

	private static void appendFactors(StringBuilder report, Task task) {
		boolean appended = false;
		boolean hasExternalDriver = false;

		if (task.isSummary()) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorSummaryTask"), getFieldText("Field.duration", task)); //$NON-NLS-1$ //$NON-NLS-2$
			appended = true;
		}
		if (task.isMilestone()) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorMilestone"), getFieldText("Field.finish", task)); //$NON-NLS-1$ //$NON-NLS-2$
			appended = true;
		}
		if (isEstimated(task)) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorEstimated"), getFieldText("Field.duration", task)); //$NON-NLS-1$ //$NON-NLS-2$
			appended = true;
		}
		if (!task.getPredecessorList().isEmpty()) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorPredecessors"), getFieldText("Field.predecessors", task)); //$NON-NLS-1$ //$NON-NLS-2$
			appended = true;
			hasExternalDriver = true;
		}
		if (hasExplicitConstraint(task)) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorConstraint"), getFieldText("Field.constraintType", task) + " / " + getFieldText("Field.constraintDate", task)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			appended = true;
			hasExternalDriver = true;
		}
		if (hasTaskCalendarOverride(task)) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorCalendarOverride"), formatTaskCalendar(task)); //$NON-NLS-1$
			appended = true;
			hasExternalDriver = true;
		}
		if (hasAssignedResources(task)) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorResources"), formatResourceNames(task)); //$NON-NLS-1$
			appended = true;
		}
		if (task.getLevelingDelay() != 0L) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorLeveling"), getFieldText("Field.levelingDelay", task)); //$NON-NLS-1$ //$NON-NLS-2$
			appended = true;
			hasExternalDriver = true;
		}
		if (task.getDeadline() != 0L) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorDeadline"), getFieldText("Field.deadline", task)); //$NON-NLS-1$ //$NON-NLS-2$
			appended = true;
			hasExternalDriver = true;
		}
		if (task.getTotalSlack() < 0L) {
			appendFactor(report, Messages.getString("TaskInspectorDialog.FactorNegativeSlack"), getFieldText("Field.totalSlack", task)); //$NON-NLS-1$ //$NON-NLS-2$
			appended = true;
		}
		if (!hasExternalDriver) {
			report.append("- ").append(Messages.getString("TaskInspectorDialog.FactorNoDrivers")).append('\n'); //$NON-NLS-1$ //$NON-NLS-2$
			appended = true;
		}
	}

	private static void appendDependencyList(StringBuilder report, String relationLabel, AssociationList dependencyList, boolean predecessor) {
		Iterator i = dependencyList.iterator();
		while (i.hasNext()) {
			Dependency dependency = (Dependency)i.next();
			report.append("- ")
				.append(capitalize(relationLabel))
				.append(": ")
				.append(describeDependency(dependency, predecessor))
				.append('\n');
		}
	}

	private static String describeDependency(Dependency dependency, boolean predecessor) {
		Task relatedTask = (Task)(predecessor ? dependency.getPredecessor() : dependency.getSuccessor());
		StringBuilder description = new StringBuilder();
		description.append(relatedTask == null ? Messages.getString("TaskInspectorDialog.None") : relatedTask.getName()); //$NON-NLS-1$
		description.append(" ["); //$NON-NLS-1$
		description.append(DependencyType.toLongString(dependency.getDependencyType()));
		if (dependency.getLag() != 0L) {
			description.append(", "); //$NON-NLS-1$
			description.append(DurationFormat.format(dependency.getLag()));
		}
		description.append(']');
		return description.toString();
	}

	private static void appendSection(StringBuilder report, String title) {
		report.append(title).append('\n');
	}

	private static void appendBlankLine(StringBuilder report) {
		if (report.length() != 0) {
			report.append('\n');
		}
	}

	private static void appendField(StringBuilder report, String fieldId, Object object) {
		appendNamedValue(report, getFieldName(fieldId), getFieldText(fieldId, object));
	}

	private static void appendNamedValue(StringBuilder report, String label, String value) {
		report.append(label).append(": ").append(normalize(value)).append('\n'); //$NON-NLS-1$
	}

	private static void appendFactor(StringBuilder report, String factor, String detail) {
		report.append("- ").append(factor); //$NON-NLS-1$
		String normalized = normalize(detail);
		if (!Messages.getString("TaskInspectorDialog.None").equals(normalized)) { //$NON-NLS-1$
			report.append(": ").append(normalized); //$NON-NLS-1$
		}
		report.append('\n');
	}

	private static String getFieldName(String fieldId) {
		Field field = Configuration.getFieldFromId(fieldId);
		return field == null ? fieldId : field.getName();
	}

	private static String getFieldText(String fieldId, Object object) {
		Field field = Configuration.getFieldFromId(fieldId);
		return field == null ? Messages.getString("TaskInspectorDialog.None") : field.getText(object, null); //$NON-NLS-1$
	}

	private static boolean hasExplicitConstraint(Task task) {
		return task.getConstraintType() != ConstraintType.ASAP && task.getConstraintType() != ConstraintType.ALAP;
	}

	private static boolean hasTaskCalendarOverride(Task task) {
		if (!(task instanceof TaskSpecificFields)) {
			return false;
		}
		return ((TaskSpecificFields)task).getTaskCalendar() != null;
	}

	private static boolean isEstimated(Task task) {
		if (!(task instanceof TaskSpecificFields)) {
			return false;
		}
		return ((TaskSpecificFields)task).isEstimated();
	}

	private static boolean hasAssignedResources(Task task) {
		String resourceNames = formatResourceNames(task);
		return !Messages.getString("TaskInspectorDialog.None").equals(resourceNames); //$NON-NLS-1$
	}

	private static String formatTaskCalendar(Task task) {
		if (task instanceof TaskSpecificFields) {
			WorkCalendar taskCalendar = ((TaskSpecificFields)task).getTaskCalendar();
			if (taskCalendar != null) {
				return taskCalendar.getName();
			}
		}
		if (task.getProject() != null && task.getProject().getEffectiveWorkCalendar() != null) {
			return MessageFormat.format(Messages.getString("TaskInspectorDialog.ProjectCalendar"), new Object[] { task.getProject().getEffectiveWorkCalendar().getName() }); //$NON-NLS-1$
		}
		return Messages.getString("TaskInspectorDialog.None"); //$NON-NLS-1$
	}

	private static String formatResourceNames(Task task) {
		return normalize(getFieldText("Field.resourceNames", task)); //$NON-NLS-1$
	}

	private static String normalize(String value) {
		if (value == null) {
			return Messages.getString("TaskInspectorDialog.None"); //$NON-NLS-1$
		}
		String trimmed = value.trim();
		if (trimmed.length() == 0 || Field.NOT_APPLICABLE.equals(trimmed)) {
			return Messages.getString("TaskInspectorDialog.None"); //$NON-NLS-1$
		}
		return trimmed;
	}

	private static String capitalize(String value) {
		if (value == null || value.length() == 0) {
			return Messages.getString("TaskInspectorDialog.None"); //$NON-NLS-1$
		}
		return Character.toUpperCase(value.charAt(0)) + value.substring(1);
	}
}
