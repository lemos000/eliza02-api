package br.com.fiap.gs.eliza.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class HistoricoMensagemDTO {
    private long id;
    private String textoUsuario;
    private String respostaBot;
    private LocalDateTime dataHora;

    public HistoricoMensagemDTO(long id, String textoUsuario, String respostaBot, LocalDateTime dataHora) {
        this.id = id;
        this.textoUsuario = textoUsuario;
        this.respostaBot = respostaBot;
        this.dataHora = dataHora;
    }
}
