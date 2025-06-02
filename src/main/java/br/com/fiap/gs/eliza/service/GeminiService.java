package br.com.fiap.gs.eliza.service;

import br.com.fiap.gs.eliza.domain.entity.Mensagem;
import br.com.fiap.gs.eliza.domain.entity.Usuario;
import br.com.fiap.gs.eliza.repository.MensagemRepository;
import com.google.genai.Client;
import com.google.genai.types.Candidate;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class GeminiService {

    private final Client client;
    private final MensagemRepository mensagemRepository;

    private static final String CONTEXTO_SISTEMA =
            "Seu nome é Eliza. Você é uma psicóloga virtual, empática e atenciosa, especializada em apoio psicológico a pessoas em situações de vulnerabilidade ou crise. Converse sempre de forma acolhedora e sem usar termos técnicos.";

    public GeminiService(MensagemRepository mensagemRepository) {
        this.client = new Client();
        this.mensagemRepository = mensagemRepository;
    }

    public String montarPromptComHistorico(Usuario usuario, String mensagemAtual, int qtdHistorico) {
        List<Mensagem> historico = mensagemRepository.findTop10ByUsuarioOrderByDataHoraDesc(usuario);
        Collections.reverse(historico);

        StringBuilder prompt = new StringBuilder();
        prompt.append(CONTEXTO_SISTEMA).append("\n\n");
        for (Mensagem msg : historico) {
            prompt.append("Usuário: ").append(msg.getTextoUsuario()).append("\n");
            prompt.append("Eliza: ").append(msg.getRespostaBot()).append("\n");
        }
        prompt.append("Usuário: ").append(mensagemAtual).append("\n");
        prompt.append("Eliza:");
        return prompt.toString();
    }

    public String processarResposta(String usuarioName, String mensagem) {
        try {
            String mensagemCompleta = CONTEXTO_SISTEMA + "\n\nNome do usuário: " + usuarioName + "\n\nMensagem do usuário: " + mensagem;

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.0-flash",
                    mensagemCompleta,
                    null
            );

            // Pega o texto da primeira Candidate (isso é o texto da IA)
            if (response.candidates().isPresent()) {
                Candidate candidate = response.candidates().get().get(0);
                if (candidate.content().isPresent()) {
                    var parts = candidate.content().get().parts();
                    if (parts.isPresent() && !parts.get().isEmpty()) {
                        var texto = parts.get().get(0).text();
                        if (texto.isPresent()) {
                            return texto.get(); // <-- Este é só o texto da IA
                        }
                    }
                }
            }
            return "Não foi possível obter resposta da IA.";
        } catch (Exception e) {
            log.error("Erro ao processar mensagem na Gemini: {}", e.getMessage(), e);
            return "Desculpe, não consegui responder no momento. Por favor, tente novamente.";
        }
    }
}
