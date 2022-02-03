package fr.nicolas.database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public class MySQL {
		private BasicDataSource connectionPool;

		public MySQL(BasicDataSource connectionPool) {
				this.connectionPool = connectionPool;
		}

		public Connection getConnection() throws SQLException {
				return connectionPool.getConnection();
		}

		public void createTables(){
				update("CREATE TABLE IF NOT EXISTS users (" +
						"`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
						"`uuid` VARCHAR(255) NOT NULL, " +
						"`nom` VARCHAR(255) DEFAULT NULL," +
						"`niveau` INT(11) DEFAULT 1, " +
						"`race` INT(11) DEFAULT NULL, " +
						"`classe` INT(11) DEFAULT NULL, " +
						"`stats` VARCHAR(4096) DEFAULT NULL, " +
						"`combat` VARCHAR(4096) DEFAULT NULL, " +
						"`armes` VARCHAR(4096) DEFAULT NULL, " +
						"`bourse` INT(11) DEFAULT NULL, "+
						"`hp` FLOAT DEFAULT NULL, " +
						"`defense` FLOAT DEFAULT NULL)");

				update("CREATE TABLE IF NOT EXISTS races (" +
						"`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
						"`nom` VARCHAR(255) NOT NULL, " +
						"`stats` VARCHAR(4096) DEFAULT NULL)");

				update("CREATE TABLE IF NOT EXISTS classes (" +
						"`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
						"`nom` VARCHAR(255) NOT NULL)");
		}


		public void update(String qry){
				try (Connection c = getConnection();
				     PreparedStatement s = c.prepareStatement(qry)) {
						s.executeUpdate();
				} catch(Exception e){
						e.printStackTrace();
				}
		}

		public Object query(String qry, Function<ResultSet, Object> consumer){
				try (Connection c = getConnection();
				     PreparedStatement s = c.prepareStatement(qry);
				     ResultSet rs = s.executeQuery()) {
						return consumer.apply(rs);
				} catch(SQLException e){
						throw new IllegalStateException(e.getMessage());
				}
		}

		public void query(String qry, Consumer<ResultSet> consumer){
				try (Connection c = getConnection();
				     PreparedStatement s = c.prepareStatement(qry);
				     ResultSet rs = s.executeQuery()) {
						consumer.accept(rs);
				} catch(SQLException e){
						throw new IllegalStateException(e.getMessage());
				}
		}
}
