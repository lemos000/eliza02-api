package br.com.fiap.gs.eliza.domain.dto;


import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String userName;
    private String userEmail;

    public LoginResponseDTO(String token, String userName, String userEmail) {
        this.token = token;
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
