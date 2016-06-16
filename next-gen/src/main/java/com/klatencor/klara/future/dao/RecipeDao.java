package com.klatencor.klara.future.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import com.klatencor.klara.future.object.recipe.Recipe;
import com.klatencor.klara.future.server.Server;

public class RecipeDao {	

	public void storeRecipe(Recipe recipe) {
		SQLExecutor executor = Server.getContext().getSqlExecutor();
		executor.doExecution(new DaoCommand<Void>() {

			@Override
			public Void run(JdbcTemplate jdbcTemplate) {
				jdbcTemplate.execute("delete xx fr");
				return null;
			}
			
		});
	}
}
