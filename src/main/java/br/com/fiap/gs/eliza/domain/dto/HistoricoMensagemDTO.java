package br.com.fiap.gs.eliza.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistoricoMensagemDTO {
    private String textoUsuario;
    private String respostaBot;
    private LocalDateTime dataHora;

    public HistoricoMensagemDTO(String textoUsuario, String respostaBot, LocalDateTime dataHora) {
    }
}
