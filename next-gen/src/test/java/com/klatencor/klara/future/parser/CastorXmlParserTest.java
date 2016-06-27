package com.klatencor.klara.future.parser;

import com.klatencor.klara.future.object.recipe.GeneralRecipeData;
import com.klatencor.klara.future.object.recipe.ImageCalibration;
import com.klatencor.klara.future.object.recipe.InspectionArea;
import com.klatencor.klara.future.object.recipe.LightCalibration;
import com.klatencor.klara.future.object.recipe.PlateAlignment;
import com.klatencor.klara.future.object.recipe.Recipe;
import com.klatencor.klara.future.object.recipe.StarLightCalibration;
import com.klatencor.klara.future.object.recipe.ZCalibration;

public class CastorXmlParserTest {

	public static void main(String[] args) throws ParsingException {
		// TODO Auto-generated method stub
		generalRecipeData();
	}
	
	
	public static void generalRecipeData() throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<GeneralRecipeData> parser 
		= new CastorXmlParser<GeneralRecipeData>("recipe-general-mapping.xml");
		GeneralRecipeData data = parser.parse("resources/generalRecipeData.xml");
		System.out.println(data.getDatabaseName());
		
	}
	
	public static void lightCal() throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<LightCalibration> parser 
		= new CastorXmlParser<LightCalibration>("lightcalibration-mapping.xml");
		LightCalibration data = parser.parse("lightCalibrationData.xml");
		System.out.println(data.getViewName());
	}
	
	public static void imageCal() throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<ImageCalibration> parser 
		= new CastorXmlParser<ImageCalibration>("imagecalibration-mapping.xml");
		ImageCalibration data = parser.parse("imageCalibrationData.xml");
		System.out.println(data.getMeasured());
	}
	
	public static void starLightCal() throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<StarLightCalibration> parser 
		= new CastorXmlParser<StarLightCalibration>("starlightcalibration-mapping.xml");
		StarLightCalibration data = parser.parse("starLightCalData.xml");
		System.out.println(data.getMethod());
	}
	
	public static void inspeactArea() throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<InspectionArea> parser 
		= new CastorXmlParser<InspectionArea>("inspection-area-mapping.xml");
		InspectionArea data = parser.parse("inspectionAreaData.xml");
		System.out.println(data.getAlgo());
	}
	
	public static void plateAlignment() throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<PlateAlignment> parser 
		= new CastorXmlParser<PlateAlignment>("plate-alignment-mapping.xml");
		PlateAlignment data = parser.parse("src/test/resources/plateAlignmentData.xml");
		System.out.println(data.getAlignmentPoints().get(1).getX());
	}
	
	public static void zcalibration() throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<ZCalibration> parser 
		= new CastorXmlParser<ZCalibration>("zcalibration-mapping.xml");
		ZCalibration data = parser.parse("src/test/resources/zcalibrationData.xml");
		System.out.println(data.getBorder().getBorderPoints().get(1).getX());
	}
	
	public static void recipe() throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<Recipe> parser 
		= new CastorXmlParser<Recipe>("recipe-mapping.xml");
		Recipe data = parser.parse("src/test/resources/recipeData.xml");
		System.out.println(data.getPlateAlignment().getAlignmentPointInfo().getLightMode());
	}

}
