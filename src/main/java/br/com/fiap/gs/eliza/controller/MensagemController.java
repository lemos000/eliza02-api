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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<HistoricoMensagemDTO>> buscarHistorico(
            @RequestHeader("Authorization") String authHeader) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        if (usuario == null) return ResponseEntity.status(401).build();

        List<Mensagem> mensagens = mensagemService.buscarHistoricoPorUsuario(usuario.getId());
        List<HistoricoMensagemDTO> historico = mensagens.stream()
                .map(m -> new HistoricoMensagemDTO(
                        m.getTextoUsuario(),
                        m.getRespostaBot(),
                        m.getDataHora()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(historico);
    }
}
