package com.museo.catalogoarte.db;

import com.museo.catalogoarte.model.Sala; // Necesitarás crear esta clase
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {
    public List<Sala> obtenerTodas() {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM salas ORDER BY id_sala";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Sala sala = new Sala();
                sala.setId(rs.getInt("id_sala"));
                sala.setNombre(rs.getString("nombre_sala"));
                salas.add(sala);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salas;
    }
}