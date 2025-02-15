package si.deisinger.persist;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Stream;

public class ParallelStream {

	public static void main(String[] args) throws Exception {
		ParallelStream streamReader = new ParallelStream();
		Connection c = streamReader.connect();
		resetDatabase(c);
		createDatabase(c);

		Path path = Paths.get("data/fo_random.txt");
		assert Files.exists(path);

		//subscribe to stream
		try (Stream<String> events = Files.lines(path)) {
			events.parallel().forEachOrdered(s -> save(c, s));
		}

		System.out.println(String.format("Min date_insert %s", getMinDate(c)));
		System.out.println(String.format("Max date_insert %s", getMaxDate(c)));

		c.close();
	}

	private static long save(Connection c, String request) {
		System.out.println((String.format("Saving %s", request)));
		String sql = "INSERT INTO EVENT (MATCH_ID,MARKET_ID,OUTCOME_ID,SPECIFIERS,DATE_INSERT,PROCESSED) "
				+ "VALUES (?, ?, ?, ?,current_timestamp, true);";

		try (PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			String[] requestSplit = request.split("\\|");
			pstmt.setString(1, requestSplit[0]);
			pstmt.setString(2, requestSplit[1]);
			pstmt.setString(3, requestSplit[2]);
			pstmt.setString(4, requestSplit.length == 3 ? "" : requestSplit[3]);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating event failed, no rows affected.");
			}

			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new SQLException("Creating event failed, no ID obtained.");
				}
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return -1;
		}
	}

	private static void createDatabase(Connection connection) throws SQLException {
		try (Statement stmt = connection.createStatement()) {
			String sql = "CREATE SEQUENCE event_id_seq;" +
					"create table EVENT" +
					"(ID int NOT NULL DEFAULT nextval('event_id_seq')," +
					"MATCH_ID varchar(255)," +
					"MARKET_ID varchar(255)," +
					"OUTCOME_ID varchar(255)," +
					"SPECIFIERS varchar(255)," +
					"DATE_INSERT timestamp," +
					"PROCESSED bool);" +
					"ALTER SEQUENCE event_id_seq OWNED BY EVENT.id;";
			stmt.executeUpdate(sql);
		}
	}

	private static void resetDatabase(Connection connection) throws SQLException {
		String sql = "DROP TABLE IF EXISTS event";

		try (Statement stmt = connection.createStatement()) {
			stmt.executeUpdate(sql);
		}
	}

	private static Timestamp getMinDate(Connection connection) throws SQLException {
		String sql = "SELECT MIN(date_insert) " +
				"FROM event;";

		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getTimestamp(1);
		}
	}

	private static Timestamp getMaxDate(Connection connection) throws SQLException {
		String sql = "SELECT MAX(date_insert) " +
				"FROM event;";

		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getTimestamp(1);
		}
	}

	/**
	 * Connect to the PostgreSQL database
	 *
	 * @return a Connection object
	 */
	public Connection connect() {
		final String url = "jdbc:postgresql://localhost:5432/postgres";
		final String user = "postgres";
		final String password = "postgres";

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to the PostgreSQL server successfully.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}
}
