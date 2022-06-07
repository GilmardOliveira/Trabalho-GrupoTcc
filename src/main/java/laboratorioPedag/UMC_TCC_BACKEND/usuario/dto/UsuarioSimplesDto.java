package laboratorioPedag.UMC_TCC_BACKEND.usuario.dto;

import laboratorioPedag.UMC_TCC_BACKEND.usuario.model.Usuario;
import lombok.Data;

@Data
public class UsuarioSimplesDto {
    private Long id;
    private String nome;
    private Usuario.Acesso acesso;
    private Usuario.Status status;
    private Long rgm;
    private String email;

    public UsuarioSimplesDto(Usuario usuario){
        this.id = usuario.getId();
        this.acesso = usuario.getAcesso();
        this.nome = usuario.getNome();
        this.status = usuario.getStatus();
        this.rgm = usuario.getRgm();
        this.email = usuario.getEmail();
    }
}
