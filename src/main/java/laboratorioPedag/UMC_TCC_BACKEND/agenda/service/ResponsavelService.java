package laboratorioPedag.UMC_TCC_BACKEND.agenda.service;

import laboratorioPedag.UMC_TCC_BACKEND.agenda.dal.ResponsavelRepository;
import laboratorioPedag.UMC_TCC_BACKEND.agenda.model.Responsavel;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static java.util.Optional.ofNullable;

@Service
public class ResponsavelService {

    public ResponsavelRepository responsavelRepository;

    public ResponsavelService(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
    }

    public Responsavel updateAgenda(Responsavel newResponsavel) {
        Responsavel responsavel = responsavelRepository.findById(newResponsavel.getId()).orElse(null);

        ofNullable(newResponsavel.getNome()).ifPresent(responsavel :: setNome);
        ofNullable(newResponsavel.getEmail()).ifPresent(responsavel :: setEmail);
        ofNullable(newResponsavel.getRg()).ifPresent(responsavel :: setRg);

        return responsavelRepository.save(newResponsavel);
    }

    public static boolean validaEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
