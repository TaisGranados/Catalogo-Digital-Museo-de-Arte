package com.museo.catalogoarte.db;

import com.museo.catalogoarte.model.Usuario;
import com.museo.catalogoarte.util.PasswordManager; // <-- IMPORT AÑADIDO
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario buscarPorEmail(String email) {
        // ... (este método se queda igual)
        String sql = "SELECT * FROM usuarios WHERE correo_electronico = ?";
        Usuario usuario = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreoElectronico(rs.getString("correo_electronico"));
                usuario.setContrasenaHash(rs.getString("contrasena_hash"));
                usuario.setRol(rs.getString("rol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public List<Usuario> obtenerTodos() {
        // ... (este método se queda igual)
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreoElectronico(rs.getString("correo_electronico"));
                usuario.setRol(rs.getString("rol"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void eliminar(int idUsuario) {
        // ... (este método se queda igual)
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Este es el método que usa PasswordManager
    public void insertar(Usuario usuario, String contrasenaPlana) {
        String contrasenaHash = PasswordManager.hashPassword(contrasenaPlana);
        String sql = "INSERT INTO usuarios (nombre, correo_electronico, contrasena_hash, rol) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreoElectronico());
            stmt.setString(3, contrasenaHash);
            stmt.setString(4, usuario.getRol());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar(Usuario usuario) {
    String sql = "UPDATE usuarios SET nombre = ?, correo_electronico = ?, rol = ? WHERE id_usuario = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, usuario.getNombre());
        stmt.setString(2, usuario.getCorreoElectronico());
        stmt.setString(3, usuario.getRol());
        stmt.setInt(4, usuario.getId());

        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    public void actualizarContrasena(String email, String nuevoHash) {
    String sql = "UPDATE usuarios SET contrasena_hash = ? WHERE correo_electronico = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, nuevoHash);
        stmt.setString(2, email);
        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}