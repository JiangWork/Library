package com.klatencor.klara.future.parser;

import com.klatencor.klara.future.object.recipe.GeneralRecipeData;

public class CastorXmlParserTest {

	public static void main(String[] args) throws ParsingException {
		// TODO Auto-generated method stub
		CastorXmlParser<GeneralRecipeData> parser 
		= new CastorXmlParser<GeneralRecipeData>("general-recipe-data-mapping.xml");
		GeneralRecipeData data = parser.parse("generalRecipeData.xml");
		System.out.println(data.getDatabaseName());
	}

}
