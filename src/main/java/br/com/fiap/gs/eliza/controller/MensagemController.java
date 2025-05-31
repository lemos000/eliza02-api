package br.com.fiap.gs.eliza.controller;

import br.com.fiap.gs.eliza.auth.JwtUtil;
import br.com.fiap.gs.eliza.domain.dto.HistoricoMensagemDTO;
import br.com.fiap.gs.eliza.domain.dto.MensagemDTO;
import br.com.fiap.gs.eliza.domain.dto.RespostaBotDTO;
import br.com.fiap.gs.eliza.domain.entity.Mensagem;
import br.com.fiap.gs.eliza.domain.entity.Usuario;
import br.com.fiap.gs.eliza.service.MensagemService;
import br.com.fiap.gs.eliza.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Optional;


@RestController
@RequestMapping("/api/chat")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    private Usuario getUsuarioFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String jwt = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(jwt);
        return usuarioService.buscarPorEmail(email).orElse(null);
    }

    @PostMapping
    public ResponseEntity<RespostaBotDTO> enviarMensagem(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid MensagemDTO mensagem) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        if (usuario == null) return ResponseEntity.status(401).build();

        String respostaBot = mensagemService.processarResposta(usuario, mensagem.getTexto());
        mensagemService.salvarMensagem(usuario, mensagem.getTexto(), respostaBot);
        return ResponseEntity.ok(new RespostaBotDTO(respostaBot));
    }

    @GetMapping("/historico")
    public ResponseEntity<Page<HistoricoMensagemDTO>> buscarHistorico(
            @RequestHeader("Authorization") String authHeader,
            Pageable pageable) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        if (usuario == null) return ResponseEntity.status(401).build();

        Page<Mensagem> mensagens = mensagemService.buscarHistoricoPorUsuario(usuario.getId(), pageable);
        Page<HistoricoMensagemDTO> historico = mensagens.map(m -> new HistoricoMensagemDTO(
                m.getTextoUsuario(),
                m.getRespostaBot(),
                m.getDataHora()
        ));
        return ResponseEntity.ok(historico);
    }

    @ DeleteMapping("/mensagem/{id}/deletar")
    public ResponseEntity<?> deletar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id
    ) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        Boolean resposta = mensagemService.deletarMensagem(usuario, id);
        if (resposta)
            return ResponseEntity.ok("Deletado com sucesso");
        return ResponseEntity.status(403).body("Sem permissão para deletar essa mensagem");
    }


    @PutMapping("mensagem/{id}/update")
    public ResponseEntity<Mensagem> atualizar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody Mensagem mensagem
    ) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        Optional<Mensagem> novaMensagem = mensagemService.alterarMensagem(usuario, mensagem);
        if (novaMensagem != null && !novaMensagem.isPresent()) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(novaMensagem.get());



    }
}
