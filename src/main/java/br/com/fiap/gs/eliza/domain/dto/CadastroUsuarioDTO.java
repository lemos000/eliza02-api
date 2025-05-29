package br.com.fiap.gs.eliza.domain.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CadastroUsuarioDTO {
    @NotBlank(message = "Nome obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String nome;

    @NotBlank(message = "Email obrigatório")
    @Email(message = "Formato de email inválido")
    @Size(max = 100, message = "Email deve ter até 100 caracteres")
    private String email;

    @NotBlank(message = "Senha obrigatória")
    @Size(min = 6, max = 50, message = "Senha deve ter entre 6 e 50 caracteres")
    private String senha;
}
