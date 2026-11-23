package DetectorPlagio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConexionSQLite {
    private static final String URL = "jdbc:sqlite:detector_plagio.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró la clase org.sqlite.JDBC", e);
        }
        return DriverManager.getConnection(URL);
    }

    public static void inicializarBaseDatos() {
        String crearTablaSQL = """
            CREATE TABLE IF NOT EXISTS textos_referencia (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                contenido TEXT NOT NULL
            );
            """;

        String contarSQL = "SELECT COUNT(*) FROM textos_referencia";
        String insertarSQL = "INSERT INTO textos_referencia (titulo, contenido) VALUES (?, ?)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(crearTablaSQL);

            try (ResultSet rs = stmt.executeQuery(contarSQL)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement ps = conn.prepareStatement(insertarSQL)) {
                        ps.setString(1, "Documento 1");
                        ps.setString(2, "Hola mi nombre es Juan");
                        ps.executeUpdate();

                        ps.setString(1, "Documento 2");
                        ps.setString(2, "Hola mi nombre es JESUS");
                        ps.executeUpdate();

                        ps.setString(1, "Documento 3");
                        ps.setString(2, "La programación orientada a objetos es un paradigma de programación.");
                        ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al inicializar SQLite: " + e.getMessage());
        }
    }
}