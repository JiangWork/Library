package com.klatencor.klara.future.support;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.klatencor.klara.future.thrift.common.TSenseSlider;

public class JniRecipeOperatorTest {
	
	public static String recipe = "";

	@Test
	public void loadWriteRecipeTest() {
		System.out.println(">>>> loadWriteRecipeTest");
		RecipeOperator op = new RecipeOperator(recipe);		
		assertEquals(op.open(), true);
		assertEquals(op.renameRecipe("testRename"), true);
		assertEquals(op.write("/export/home/kla90/" + System.currentTimeMillis() + ".rcp"), true);
		try {
			op.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getPlateAlignmentImagesTest() {
		
	}
	
	@Test
	public void getSenseSlidersTest() {
		System.out.println(">>>> getSenseSlidersTest");
		RecipeOperator op = new RecipeOperator(recipe);		
		assertEquals(op.open(), true);
		List<TSenseSlider> ssList = op.getIaSenseSliders(1);
		Assert.assertNotNull(ssList);
		try {
			op.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getSenseSliderTest() {
		System.out.println(">>>> getSenseSliderTest");
		RecipeOperator op = new RecipeOperator(recipe);		
		assertEquals(op.open(), true);
		TSenseSlider ss = op.getCertainSenseSlider(1, 1);
		Assert.assertNotNull(ss);
		try {
			op.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("recipe path is needed.");
			System.exit(-1);
		}
		recipe = args[0];
		Result result = JUnitCore.runClasses(JniRecipeOperatorTest.class);
		for(Failure failure: result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
	}
}
