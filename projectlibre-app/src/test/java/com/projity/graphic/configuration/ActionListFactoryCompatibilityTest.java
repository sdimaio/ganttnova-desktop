package com.projity.graphic.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ActionListFactoryCompatibilityTest {
	@Test
	public void groovyBackedActionListsCompileOnModernJdks() throws Exception {
		ActionListFactory factory = new ActionListFactory();
		factory.setType("nodeModel");
		factory.setFormulaText("return \"Indent,Outdent,InsertTask,Delete,Copy,Cut,Paste,Expand,Collapse\";");

		ActionList actionList = factory.getActionList();

		assertNotNull(actionList);
		assertEquals("Indent,Outdent,InsertTask,Delete,Copy,Cut,Paste,Expand,Collapse", actionList.getList(null));
	}
}
