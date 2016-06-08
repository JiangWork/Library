package com.klatencor.klara.future.parser;

import com.klatencor.klara.future.object.recipe.GeneralRecipeData;
import com.klatencor.klara.future.object.recipe.ImageCalibration;
import com.klatencor.klara.future.object.recipe.LightCalibration;

public class CastorXmlParserTest {

	public static void main(String[] args) throws ParsingException {
		// TODO Auto-generated method stub
		imageCal();
	}
	
	
	public static void generalRecipeData() throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<GeneralRecipeData> parser 
		= new CastorXmlParser<GeneralRecipeData>("general-recipe-data-mapping.xml");
		GeneralRecipeData data = parser.parse("generalRecipeData.xml");
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

}
