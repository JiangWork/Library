package library.database.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class DaoCommandUtils {

	/**
	 * concatenate the DaoCommands into a single DaoCommand.
	 * The new DaoCommand's return is the number of DaoCommands are successfully executed. 
	 * @param commands
	 * @return the new DaoCommand
	 */
	public DaoCommand<Integer> concatenate(final DaoCommand<?>... commands) {
		return new DaoCommand<Integer>(){
			
			@Override
			public Integer execute(JdbcTemplate jdbcTemplate) {
				int success = 0;
				for (int i = 0; i < commands.length; ++i) {
					commands[i].execute(jdbcTemplate);
					++success;
				}
				return success;
			}
			
		};
	}
	

	public static class ArgsBuilder
	{
		private List<Object> args;

		public ArgsBuilder() {
			this.args = new ArrayList<Object>();
		}
		public ArgsBuilder(Object arg) {
			this();
			this.args.add(arg);
		}
		
		public ArgsBuilder addArg(Object arg) {
			this.args.add(arg);
			return this;
		}
		
		public ArgsBuilder addArgs(Object... args) {
			for (int i = 0; i < args.length; ++i) {
				this.args.add(args[i]);
			}
			return this;
		}
		
		public Object[] build() {
			return this.args.toArray(new Object[this.args.size()]);
		}
	}
}
