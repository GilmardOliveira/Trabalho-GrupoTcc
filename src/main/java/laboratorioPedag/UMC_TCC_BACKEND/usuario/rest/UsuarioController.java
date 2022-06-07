package laboratorioPedag.UMC_TCC_BACKEND.usuario.rest;

import laboratorioPedag.UMC_TCC_BACKEND.usuario.dal.UsuarioRepository;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.dto.LoginDto;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.dto.UsuarioSimplesDto;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.model.Usuario;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.service.UsuarioService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;
    @Autowired
    private JavaMailSender javaSender;
    public UsuarioController(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/getAll")
    public List<Usuario> getAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

    @GetMapping("/getAll/{status}")
    public List<Usuario> getAll(@PathVariable String status) throws Exception {

        Usuario.Status realStatus = usuarioService.buildStatus(status);
        List<Usuario> usuarios = usuarioRepository.findAllByStatus(realStatus);

        return usuarios;
    }

    @GetMapping("/getByName/{name}")
    public List<Usuario> getName(@PathVariable String name) {
        List<Usuario> usuarios = usuarioRepository.findAllByNomeContains(name);
        return usuarios;
    }

    @GetMapping("/getByRgm/{rgm}")
    public List<Usuario> getByRgm(@PathVariable String rgm) {
        double RGM = Double.parseDouble(rgm);
        List<Usuario> usuarios = usuarioRepository.findByRgm(RGM);
        return usuarios;
    }

    @PostMapping("/saveOrUpdate")
    public Usuario saveOrUpdate(@RequestBody Usuario newUsuario) {
        Validate.notNull(newUsuario, "O objeto do usuario não pode ser nulo");

        if (newUsuario.getId() == null) {
            usuarioRepository.save(newUsuario);
            return newUsuario;
        }

        return usuarioService.updateUsuario(newUsuario);
    }

    @GetMapping(value = "/{usuarioId}")
    public Usuario get(@PathVariable("usuarioId") Long usuarioId) {
        Validate.notNull(usuarioId, "Id do usuario não pode ser nulo");
        return usuarioRepository.findById(usuarioId).orElse(null);
    }

    @PostMapping("/delete")
    public Usuario deleteUser(@RequestBody Usuario userDelete) {
        Validate.notNull(userDelete, "O objeto de usuario não pode ser nulo");
        return usuarioService.deleteUsuario(userDelete);
    }

    @PostMapping("/authenticate")
    public UsuarioSimplesDto authenticate(@RequestBody LoginDto login) throws Exception {
        Validate.notNull(login.getEmail(), "Email não pode ser nulo");
        Validate.notNull(login.getSenha(), "Senha não pode ser nula");
        UsuarioSimplesDto usuario = usuarioService.authenticate(login.getEmail(), login.getSenha());
        if (usuario == null) {
            throw new Exception("Usuario não encontrado");
        }
        return usuario;
    }

    @GetMapping(value = "/byAccess/{acesso}/{status}")
    public List<Usuario> getUsuariosByAcesso(@PathVariable String acesso, @PathVariable String status) {
        Usuario.Acesso acessoAux = usuarioService.buildAcesso(acesso);
        Validate.notNull(acessoAux, "Acesso invalido");
        Usuario.Status statusAux = usuarioService.buildStatus(status);
        Validate.notNull(statusAux, "Status invalido");
        return usuarioRepository.findAllByAcessoAndStatus(acessoAux, statusAux);
    }

    @PostMapping("/sendMail")
    public String sendMail(@RequestParam String email){ /*throws AddressException, MessagingException, IOException*/
        Random random = new Random();
        int a;
        String[] specialCaracters = {"'","!","@","#","$","%","&","*",".",",","/","-","_","+"};
        String[] numericCaracters = {"0","1","2","3","4","5","6","7","8","9"};
        String[] caracters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r"
                            ,"s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J"
                            ,"K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String newPassword = "";

        for ( int i = 0; i < 7; i++){
            a = random.nextInt(caracters.length);
            newPassword += caracters[a];
        }
        a = random.nextInt(specialCaracters.length);
        newPassword += specialCaracters[a];
        a = random.nextInt(numericCaracters.length);
        newPassword += numericCaracters[a];

        Usuario user = usuarioRepository.findByEmail(email);
        user.setSenha(newPassword);//(new BCryptPasswordEncoder().encode(RandomStringUtils.random(15)));
        usuarioService.updateUsuario(user);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Alteração de senha");
        message.setText("Olá " + user.getNome() + ", você solicitou uma alteração de senha. \n\nAqui está sua nova senha de acesso: \n\n"
                + newPassword + "\n\nSe Não foi você que solicitou, Favor, Entre em contato com algum coordenador da UMC.");
        message.setTo(email);
        message.setFrom("UMC");

        try {
            javaSender.send(message);
            return "Email enviado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar email.";
        }
    }
}
