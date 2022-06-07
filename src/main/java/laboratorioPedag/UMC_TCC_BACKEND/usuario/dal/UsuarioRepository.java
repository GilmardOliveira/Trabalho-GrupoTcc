package laboratorioPedag.UMC_TCC_BACKEND.usuario.dal;

import laboratorioPedag.UMC_TCC_BACKEND.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    List<Usuario> findAllByNomeContains(String name);
    List<Usuario> findByRgm(Double rgm);
    Usuario findByEmail(String email);
    Usuario findByNome(String nome);
    List<Usuario> findAllByAcesso(Usuario.Acesso acesso);
    List<Usuario> findAllByStatus(Usuario.Status status);
    List<Usuario> findAllByAcessoAndStatus(Usuario.Acesso acesso, Usuario.Status status);

}
