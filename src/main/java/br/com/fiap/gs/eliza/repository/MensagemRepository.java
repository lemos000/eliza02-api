package br.com.fiap.gs.eliza.repository;

import br.com.fiap.gs.eliza.domain.entity.Mensagem;
import br.com.fiap.gs.eliza.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    public List<Mensagem> findAllByUsuario(Usuario usuario);

    List<Mensagem> findByUsuarioIdOrderByDataHoraAsc(Long usuarioId);

    List<Mensagem> findTop10ByUsuarioOrderByDataHoraDesc(Usuario usuario);
}
