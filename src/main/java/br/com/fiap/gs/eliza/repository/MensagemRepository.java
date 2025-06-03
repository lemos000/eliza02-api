package br.com.fiap.gs.eliza.repository;

import br.com.fiap.gs.eliza.domain.entity.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    Page<Mensagem> findByUsuarioIdOrderByDataHoraAsc(Long usuarioId, Pageable pageable);

    Optional<Mensagem> findByIdAndUsuarioId(Long mensagemId, Long usuarioId);
    void deleteByIdAndUsuarioId(Long mensagemId, Long usuarioId);
}
