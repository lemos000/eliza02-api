package br.com.fiap.gs.eliza.domain.dto;


import lombok.Data;

@Data
public class LoginResponseDTO {
    private long id;
    private String token;
    private String userName;
    private String userEmail;

    public LoginResponseDTO(String token, String userName, String userEmail, long id) {
        this.id = id;
        this.token = token;
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
