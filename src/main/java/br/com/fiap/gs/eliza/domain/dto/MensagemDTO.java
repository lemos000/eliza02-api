package br.com.fiap.gs.eliza.domain.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class MensagemDTO {
    @NotBlank(message = "Texto da mensagem obrigatório")
    @Size(min = 1, max = 1000, message = "Mensagem deve ter entre 1 e 1000 caracteres")
    private String texto;
}
