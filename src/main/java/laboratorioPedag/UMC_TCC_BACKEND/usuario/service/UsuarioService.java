package laboratorioPedag.UMC_TCC_BACKEND.usuario.service;

import laboratorioPedag.UMC_TCC_BACKEND.usuario.dal.UsuarioRepository;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.dto.UsuarioSimplesDto;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.model.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.poi.hssf.record.common.FtrHeader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
public class UsuarioService {

    UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){ this.usuarioRepository = usuarioRepository; }

    public Usuario updateUsuario(@RequestBody Usuario newUsuario) {
        Usuario usuario = usuarioRepository.findById(newUsuario.getId()).orElse(null);

        ofNullable(newUsuario.getNome()).ifPresent(usuario :: setNome);
        ofNullable(newUsuario.getAcesso()).ifPresent(usuario :: setAcesso);
        ofNullable(newUsuario.getEmail()).ifPresent(usuario :: setEmail);
        ofNullable(newUsuario.getRgm()).ifPresent(usuario :: setRgm);
        ofNullable(newUsuario.getSenha()).ifPresent(usuario :: setSenha);
        ofNullable(newUsuario.getStatus()).ifPresent(usuario :: setStatus);

        return usuarioRepository.save(usuario);
    }

    public Usuario deleteUsuario(@RequestBody Usuario userDel){
        Usuario usuario = usuarioRepository.findById(userDel.getId()).orElse(null);

        usuario.setStatus(Usuario.Status.INACTIVE);

        return usuarioRepository.save(usuario);
    }

    public UsuarioSimplesDto authenticate(String email, String senha) throws Exception {
        if (usuarioRepository.findByEmail(email) == null) {
            throw new Exception("Usuario não encontrado");
        }

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (!usuario.getSenha().equals(senha)) {
            throw new Exception("Senha incorreta");
        }

        UsuarioSimplesDto usuarioSimples = new UsuarioSimplesDto(usuario);
        return usuarioSimples;
    }

    public Usuario.Acesso buildAcesso(String acesso){
        Validate.notNull(acesso, "Acesso não pode ser nulo.");
        acesso = acesso.trim().toUpperCase();
        if (acesso.equals("COORDENADOR")){
            return Usuario.Acesso.COORDENADOR;
        }else if(acesso.equals("MONITOR")){
            return Usuario.Acesso.MONITOR;
        }else if(acesso.equals("PROFESSOR")){
            return Usuario.Acesso.PROFESSOR;
        }else{
            return null;
        }
    }

    public Usuario.Status buildStatus(String status){
        Validate.notNull(status, "Status não pode ser nulo.");
        status = status.trim().toUpperCase();
        if (status.equals("ACTIVE")){
            return Usuario.Status.ACTIVE;
        }else if(status.equals("INACTIVE")){
            return Usuario.Status.INACTIVE;
        }else{
            return null;
        }
    }
}
