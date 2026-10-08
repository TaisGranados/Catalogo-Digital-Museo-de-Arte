package com.museo.catalogoarte.db;

import com.museo.catalogoarte.model.ObraDeArte;
import com.museo.catalogoarte.model.Sala;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObraDeArteDAO {

    private ObraDeArte leerObraDeResultSet(ResultSet rs) throws SQLException {
        ObraDeArte obra = new ObraDeArte();
        obra.setId(rs.getInt("id_obra"));
        obra.setTitulo(rs.getString("titulo"));
        obra.setArtista(rs.getString("artista"));
        obra.setTecnica(rs.getString("tecnica"));
        obra.setAnioCreacion(rs.getInt("ano_creacion"));
        obra.setDescripcion(rs.getString("descripcion"));
        obra.setRutaImagen(rs.getString("ruta_imagen"));
        obra.setRutaAudio(rs.getString("ruta_audio"));
        obra.setIdCategoria(rs.getInt("id_categoria"));
        obra.setIdSala(rs.getInt("id_sala"));
        return obra;
    }

    public List<ObraDeArte> obtenerTodas() {
        List<ObraDeArte> obras = new ArrayList<>();
        String sql = "SELECT * FROM obras_de_arte ORDER BY id_obra";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                obras.add(leerObraDeResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obras;
    }

    public List<ObraDeArte> buscarObras(String textoBusqueda, Sala salaFiltro) {
        List<ObraDeArte> obras = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM obras_de_arte WHERE 1=1");

        if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
            sql.append(" AND (LOWER(titulo) LIKE ? OR LOWER(artista) LIKE ?)");
        }
        if (salaFiltro != null) {
            sql.append(" AND id_sala = ?");
        }
        sql.append(" ORDER BY id_obra");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
                String busqueda = "%" + textoBusqueda.toLowerCase() + "%";
                stmt.setString(paramIndex++, busqueda);
                stmt.setString(paramIndex++, busqueda);
            }
            if (salaFiltro != null) {
                stmt.setInt(paramIndex++, salaFiltro.getId());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                obras.add(leerObraDeResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obras;
    }
    
    public void insertar(ObraDeArte obra) {
        String sql = "INSERT INTO obras_de_arte (titulo, artista, tecnica, ano_creacion, descripcion, ruta_imagen, ruta_audio, id_categoria, id_sala) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, obra.getTitulo());
            stmt.setString(2, obra.getArtista());
            stmt.setString(3, obra.getTecnica());
            stmt.setInt(4, obra.getAnioCreacion());
            stmt.setString(5, obra.getDescripcion());
            stmt.setString(6, obra.getRutaImagen());
            stmt.setString(7, obra.getRutaAudio());
            stmt.setInt(8, obra.getIdCategoria());
            stmt.setInt(9, obra.getIdSala());
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(ObraDeArte obra) {
        String sql = "UPDATE obras_de_arte SET titulo = ?, artista = ?, tecnica = ?, ano_creacion = ?, descripcion = ?, ruta_imagen = ?, ruta_audio = ?, id_categoria = ?, id_sala = ? WHERE id_obra = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, obra.getTitulo());
            stmt.setString(2, obra.getArtista());
            stmt.setString(3, obra.getTecnica());
            stmt.setInt(4, obra.getAnioCreacion());
            stmt.setString(5, obra.getDescripcion());
            stmt.setString(6, obra.getRutaImagen());
            stmt.setString(7, obra.getRutaAudio());
            stmt.setInt(8, obra.getIdCategoria());
            stmt.setInt(9, obra.getIdSala());
            stmt.setInt(10, obra.getId());
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int idObra) {
        String sql = "DELETE FROM obras_de_arte WHERE id_obra = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idObra);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}