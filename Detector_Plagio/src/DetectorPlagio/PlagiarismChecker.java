package DetectorPlagio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PlagiarismChecker {
    private Map<String, String> database;

    public PlagiarismChecker() {
        database = new HashMap<>();
        ConexionSQLite.inicializarBaseDatos();
        cargarDesdeSQLite();
    }

    public boolean cargarDesdeSQLite() {
        database.clear();
        String sql = "SELECT titulo, contenido FROM textos_referencia";

        try (Connection conn = ConexionSQLite.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String contenido = rs.getString("contenido");
                database.put(titulo, contenido);
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al leer desde SQLite: " + e.getMessage());
            return false;
        }
    }

    public boolean loadFiles(String[] paths) {
        for (String path : paths) {
            String content = readFileContent(path);
            database.put(path, content);
        }
        return true;
    }

    public ResultChecker verifyPlagiarism(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ResultChecker(false, null);
        }
        for (Map.Entry<String, String> entry : database.entrySet()) {
            if (containsPlagiarism(entry.getValue(), text)) {
                return new ResultChecker(true, entry.getKey() + " -> " + entry.getValue());
            }
        }
        return new ResultChecker(false, null);
    }

    private boolean containsPlagiarism(String content, String text) {
        return content != null && content.toLowerCase().contains(text.trim().toLowerCase());
    }

    private String readFileContent(String path) {
        return path;
    }
}