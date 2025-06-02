package br.com.fiap.gs.eliza.controller;

import br.com.fiap.gs.eliza.auth.JwtUtil;
import br.com.fiap.gs.eliza.domain.dto.CadastroUsuarioDTO;
import br.com.fiap.gs.eliza.domain.dto.JwtResponseDTO;
import br.com.fiap.gs.eliza.domain.dto.LoginDTO;
import br.com.fiap.gs.eliza.domain.entity.Usuario;
import br.com.fiap.gs.eliza.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody @Valid CadastroUsuarioDTO dto) {
        try {
            usuarioService.cadastrarUsuario(dto.getNome(), dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body("E-mail já cadastrado ou inválido.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO dto) {
        try {
            Usuario usuario = usuarioService.autenticar(dto.getEmail(), dto.getSenha());
            String token = jwtUtil.gerarToken(usuario);
            return ResponseEntity.ok(new JwtResponseDTO(token));
        } catch (IllegalArgumentException e) {
            // Retorna Forbidden se login inválido
            return ResponseEntity.status(403).body("Email ou senha inválidos.");
        }
    }

}
