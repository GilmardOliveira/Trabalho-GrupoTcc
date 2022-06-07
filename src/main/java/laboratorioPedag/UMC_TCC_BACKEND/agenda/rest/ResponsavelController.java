package laboratorioPedag.UMC_TCC_BACKEND.agenda.rest;

import laboratorioPedag.UMC_TCC_BACKEND.agenda.dal.ResponsavelRepository;
import laboratorioPedag.UMC_TCC_BACKEND.agenda.model.Responsavel;
import laboratorioPedag.UMC_TCC_BACKEND.agenda.service.ResponsavelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api/v1/responsavel")
public class ResponsavelController {

    public ResponsavelRepository responsavelRepository;
    public ResponsavelService responsavelService;

    public ResponsavelController(ResponsavelRepository responsavelRepository,
                                 ResponsavelService responsavelService) {
        this.responsavelRepository = responsavelRepository;
        this.responsavelService = responsavelService;
    }

    @GetMapping("/getAll")
    public List<Responsavel> getAll(){
        return responsavelRepository.findAll();
    }

    @GetMapping("/getByEmail/{email}")
    public Responsavel getByEmail(@PathVariable String email) throws Exception{
        if (!responsavelService.validaEmail(email)){
            throw new Exception("Email invalido");
        }

        Responsavel responsavel = responsavelRepository.findByEmail(email);

        if (responsavel == null){
            throw new Exception("Responsavel não encontrado");
        }
        return responsavel;
    }

    @GetMapping("/getByNome/{nome}")
    public Responsavel getByName(@PathVariable String nome) throws Exception{
        Responsavel responsavel = responsavelRepository.findByNome(nome);
        if(responsavel == null){
            throw new Exception("Responsavel não encontrado");
        }
        return responsavel;
    }

   @PostMapping
   public Responsavel saveOrUpdate(@RequestBody Responsavel newResponsavel){

        if (newResponsavel.getId() == null){
            responsavelRepository.save(newResponsavel);
            return newResponsavel;
        }

        return responsavelService.updateAgenda(newResponsavel);
   }
}
