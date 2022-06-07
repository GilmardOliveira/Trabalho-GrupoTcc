package laboratorioPedag.UMC_TCC_BACKEND.agenda.service;

import laboratorioPedag.UMC_TCC_BACKEND.agenda.dal.AgendaRepository;
import laboratorioPedag.UMC_TCC_BACKEND.agenda.dto.AgendaDto;
import laboratorioPedag.UMC_TCC_BACKEND.agenda.model.Agenda;
import laboratorioPedag.UMC_TCC_BACKEND.material.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;


import java.util.Calendar;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
public class AgendaService {

    AgendaRepository agendaRepository;
    MaterialService materialService;

    public AgendaService(AgendaRepository agendaRepository, MaterialService materialService){
        this.agendaRepository = agendaRepository;
        this.materialService = materialService;
    }

    public Agenda updateAgenda(AgendaDto newAgenda) throws Exception {
        Agenda agenda = agendaRepository.findById(newAgenda.id).orElse(null);

        if (!newAgenda.materiais.equals(agenda.getMateriais())){
            throw new Exception("Não é permitido alteração de materiais");
        }

        ofNullable(newAgenda.coordenator).ifPresent(agenda :: setCoordenator);
        ofNullable(newAgenda.monitor).ifPresent(agenda :: setMonitor);
        ofNullable(newAgenda.professor).ifPresent(agenda :: setProfessor);
        ofNullable(newAgenda.escola).ifPresent(agenda::setEscola);
        ofNullable(newAgenda.criancas).ifPresent(agenda :: setCriancas);
        ofNullable(newAgenda.tipoEnsino).ifPresent(agenda::setTipoEnsino);
        ofNullable(newAgenda.resposaveis).ifPresent(agenda::setResposaveis);
        ofNullable(newAgenda.data).ifPresent(agenda::setData);
        ofNullable(newAgenda.status).ifPresent(agenda::setStatus);
        ofNullable(newAgenda.descricao).ifPresent(agenda::setDescricao);

        return agendaRepository.save(agenda);
    }

    public Agenda.Status buildStatus(String status) throws Exception {
        Validate.notNull(status, "Status deve ser informado");

        if (status.toUpperCase().equals("ACTIVE")){
            return Agenda.Status.ACTIVE;
        }else if(status.toUpperCase().equals("INACTIVE")){
            return Agenda.Status.INACTIVE;
        }else{
            throw new Exception("STATUS INVALIDO");
        }
    }

}
