package br.com.fiap.gs.eliza.domain.entity;

import br.com.fiap.gs.eliza.domain.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Lob
    @Column(nullable = false)
    private String textoUsuario;

    @Lob
    @Column(nullable = false)
    private String respostaBot;

    @Builder.Default
    private LocalDateTime dataHora = LocalDateTime.now();
}

