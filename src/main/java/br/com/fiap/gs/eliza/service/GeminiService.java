package br.com.fiap.gs.eliza.service;

import br.com.fiap.gs.eliza.repository.MensagemRepository;
import com.google.genai.Client;
import com.google.genai.types.Candidate;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public String processarResposta(String usuarioName, String mensagem) {
        try {
            String mensagemCompleta = CONTEXTO_SISTEMA + "\n\nNome do usuário: " + usuarioName + "\n\nMensagem do usuário: " + mensagem;

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.0-flash",
                    mensagemCompleta,
                    null
            );
            if (response.candidates().isPresent()) {
                Candidate candidate = response.candidates().get().get(0);
                if (candidate.content().isPresent()) {
                    var parts = candidate.content().get().parts();
                    if (parts.isPresent() && !parts.get().isEmpty()) {
                        var texto = parts.get().get(0).text();
                        if (texto.isPresent()) {
                            return texto.get(); // em tese deve retornar apenas o texto resposta do gemini
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
