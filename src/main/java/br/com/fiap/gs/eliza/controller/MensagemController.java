package br.com.fiap.gs.eliza.controller;

import br.com.fiap.gs.eliza.auth.JwtUtil;
import br.com.fiap.gs.eliza.domain.dto.HistoricoMensagemDTO;
import br.com.fiap.gs.eliza.domain.dto.MensagemDTO;
import br.com.fiap.gs.eliza.domain.dto.RespostaBotDTO;
import br.com.fiap.gs.eliza.domain.entity.Mensagem;
import br.com.fiap.gs.eliza.domain.entity.Usuario;
import br.com.fiap.gs.eliza.service.MensagemService;
import br.com.fiap.gs.eliza.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/chat")
@SecurityRequirement(name = "bearerAuth")
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
        Usuario user = usuarioService.buscarPorEmail(email).orElse(null);
        System.out.println("Usuário do token: " + user.getNome());
        return user;
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

        Page<Mensagem> mensagens = mensagemService.buscarHistoricoPorUsuario(usuario, pageable);
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
    public ResponseEntity<String> atualizar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody MensagemDTO mensagem
    ) {
        Usuario usuario = getUsuarioFromToken(authHeader);
        if (usuario == null) {
            return ResponseEntity.unprocessableEntity().build();
        }
        Optional<Mensagem> novaMensagem = mensagemService.alterarMensagem(id, usuario, mensagem);
        if (!novaMensagem.isPresent()) return ResponseEntity.status(404).body("Encontrou nada");
        return ResponseEntity.ok("Mensagem alterada para: " + novaMensagem.get().getTextoUsuario());



    }
}
