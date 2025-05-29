package br.com.fiap.gs.eliza.repository;

import br.com.fiap.gs.eliza.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public boolean existsByEmail(String email);

    public Optional<Usuario> findByEmail(String email);
}
