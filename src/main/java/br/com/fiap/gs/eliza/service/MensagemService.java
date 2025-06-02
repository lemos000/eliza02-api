package br.com.fiap.gs.eliza.service;

import br.com.fiap.gs.eliza.domain.dto.MensagemDTO;
import br.com.fiap.gs.eliza.domain.entity.Mensagem;
import br.com.fiap.gs.eliza.domain.entity.Usuario;
import br.com.fiap.gs.eliza.repository.MensagemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private GeminiService geminiService;

    public String processarResposta(Usuario usuario, String textoUser) {
        String respostaBot = geminiService.processarResposta(usuario.getNome(), textoUser);
        return respostaBot;
    }

    @Transactional
    public Mensagem salvarMensagem(Usuario usuario, String textoUser, String respostaBot) {
        Mensagem mensagem = new Mensagem().builder()
                .usuario(usuario)
                .respostaBot(respostaBot)
                .textoUsuario(textoUser)
                .dataHora(LocalDateTime.now())
                .build();
        return mensagemRepository.save(mensagem);


    }

    public Page<Mensagem> buscarHistoricoPorUsuario(Usuario usuario, Pageable pageable) {
        return mensagemRepository.findByUsuarioIdOrderByDataHoraAsc(usuario.getId(), pageable);
    }

    @Transactional
    public boolean deletarMensagem(Usuario usuario, Long mensagemId) {

        Optional<Mensagem> mensagem = mensagemRepository.findByIdAndUsuarioId(mensagemId, usuario.getId());
        if (mensagem.isPresent()){
            mensagemRepository.deleteByIdAndUsuarioId(mensagemId, usuario.getId());
            return true;

        } else {
            return false;
        }

    }
    public Optional<Mensagem> buscarMensagem(Usuario usuario, Long mensagemId) {
        Optional<Mensagem> mensagem = mensagemRepository.findByIdAndUsuarioId(mensagemId, usuario.getId());
        return mensagem;

    }

    @Transactional
    public Optional<Mensagem> alterarMensagem(Long id, Usuario usuario, MensagemDTO message) {
        Optional<Mensagem> mensagemASerAlterada = mensagemRepository.findByIdAndUsuarioId(id, usuario.getId());
        if (mensagemASerAlterada.isPresent()) {
            Mensagem mensagem = mensagemASerAlterada.get();
            mensagem.setTextoUsuario(message.getTexto());
            mensagem.setDataHora(LocalDateTime.now());
            return Optional.of(mensagemRepository.save(mensagem));
        }
        return Optional.empty();
    }



}
