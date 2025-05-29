package br.com.fiap.gs.eliza.service;

import br.com.fiap.gs.eliza.domain.entity.Usuario;
import br.com.fiap.gs.eliza.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @Transactional
    public Usuario cadastrarUsuario(String nome, String email, String senhaPura) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }
        Usuario usuario = Usuario.builder()
                .nome(nome)
                .email(email)
                .senhaHash(encoder.encode(senhaPura))
                .build();
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario autenticar(String email, String senhaPura) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        if (!encoder.matches(senhaPura, usuario.getSenhaHash())) {
            throw new IllegalArgumentException("Senha inválida");
        }
        return usuario;
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
