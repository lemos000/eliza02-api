package br.com.fiap.gs.eliza.domain.entity;

import br.com.fiap.gs.eliza.domain.entity.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "Usuário é obrigatório")
    private Usuario usuario;

    @Column(name = "resposta_bot", columnDefinition = "TEXT")
    @NotBlank(message = "A resposta do bot não pode ser vazia")
    @Size(max = 20000, message = "A resposta do bot não pode exceder 20.000 caracteres")
    private String respostaBot;

    @Column(name = "texto_usuario", columnDefinition = "TEXT")
    @NotBlank(message = "O texto do usuário não pode ser vazio")
    @Size(max = 10000, message = "O texto do usuário não pode exceder 10.000 caracteres")
    private String textoUsuario;

    @Builder.Default
    @NotNull(message = "A data/hora é obrigatória")
    private LocalDateTime dataHora = LocalDateTime.now();
}

