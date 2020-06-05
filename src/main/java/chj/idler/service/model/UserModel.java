package chj.idler.service.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserModel {
    private Integer id;
    private String username;
    private String password;
    private String telephone;
    private String email;
    private Byte status;
}
