package br.com.fiap.gs.eliza.service;

import br.com.fiap.gs.eliza.domain.entity.Mensagem;
import br.com.fiap.gs.eliza.domain.entity.Usuario;
import br.com.fiap.gs.eliza.repository.MensagemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



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

    public List<Mensagem> buscarHistoricoPorUsuario(Long usuarioId) {
        return mensagemRepository.findByUsuarioIdOrderByDataHoraAsc(usuarioId);
    }


}
