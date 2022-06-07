package laboratorioPedag.UMC_TCC_BACKEND.usuario.dto;

import laboratorioPedag.UMC_TCC_BACKEND.usuario.model.Usuario;
import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String senha;

}
