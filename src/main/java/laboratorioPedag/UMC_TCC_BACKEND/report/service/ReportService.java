package laboratorioPedag.UMC_TCC_BACKEND.report.service;

import laboratorioPedag.UMC_TCC_BACKEND.agenda.dal.AgendaRepository;
import laboratorioPedag.UMC_TCC_BACKEND.agenda.dal.ResponsavelRepository;
import laboratorioPedag.UMC_TCC_BACKEND.agenda.model.Agenda;
import laboratorioPedag.UMC_TCC_BACKEND.agenda.model.Responsavel;
import laboratorioPedag.UMC_TCC_BACKEND.material.dal.MaterialRepository;
import laboratorioPedag.UMC_TCC_BACKEND.material.model.Material;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.dal.UsuarioRepository;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.model.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReportService {

    private MaterialRepository materialRepository;
    private UsuarioRepository usuarioRepository;
    private AgendaRepository agendaRepository;
    private ResponsavelRepository responsavelRepository;

    public ReportService(MaterialRepository materialRepository, UsuarioRepository usuarioRepository,
                         AgendaRepository agendaRepository, ResponsavelRepository responsavelRepository) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        this.agendaRepository = agendaRepository;
        this.responsavelRepository = responsavelRepository;
    }

    public Workbook modificadorDeReport(String type, Long startDate, Long endDate) throws Exception {
        if (type.trim().toLowerCase().equals("agenda")) {
            Workbook workbook = agendaReport(startDate, endDate);
            return workbook;
        } else if (type.trim().toLowerCase().equals("usuario")) {
            Workbook workbook = usuarioReport();
            return workbook;
        } else if (type.trim().toLowerCase().equals("material")) {
            Workbook workbook = materialReport();
            return workbook;
        } else {
            throw new Exception("Tipo de report invalido");
        }
    }

    private Workbook materialReport() {
        List<Material> materiais = materialRepository.findAll(Sort.by("nome"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        int rowNum = 0;
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row rowTop = sheet.createRow(rowNum);
        rowTop.createCell(0).setCellValue("Nome: ");
        rowTop.createCell(1).setCellValue("Quantidade: ");
        rowTop.createCell(2).setCellValue("Quantidade minima: ");
        rowTop.createCell(3).setCellValue("Data de lançamento: ");
        rowTop.createCell(4).setCellValue("Classe: ");
        rowTop.createCell(5).setCellValue("Categoria: ");
        rowTop.createCell(6).setCellValue("Embalagem: ");
        rowTop.createCell(7).setCellValue("Descrição: ");

        rowNum++;

        for (Material material : materiais) {
            Row row = sheet.createRow(rowNum);

            Date dataLancamento = material.getDataLancamento() != null ? new Date(material.getDataLancamento()) : null;
            row.createCell(0).setCellValue(material.getNome() != null ? material.getNome() : "");
            row.createCell(1).setCellValue(material.getQuantidade());
            row.createCell(2).setCellValue(material.getQuantidadeMinima());
            row.createCell(3).setCellValue(material.getDataLancamento() != null ? simpleDateFormat.format(dataLancamento) : "");
            row.createCell(4).setCellValue(material.getClasse() != null ? material.getClasse().toString() : "");
            row.createCell(5).setCellValue(material.getCategoria() != null ? material.getCategoria().toString() : "");
            row.createCell(6).setCellValue(material.getEmbalagem() != null ? material.getEmbalagem().toString() : "");
            row.createCell(7).setCellValue(material.getDescricao() != null ? material.getDescricao() : "");

            rowNum++;
        }
        return workbook;
    }

    private Workbook usuarioReport() {
        List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("nome"));

        int rowNum = 0;
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row rowTop = sheet.createRow(rowNum);
        rowTop.createCell(0).setCellValue("Nome: ");
        rowTop.createCell(1).setCellValue("RGM: ");
        rowTop.createCell(2).setCellValue("Email: ");
        rowTop.createCell(3).setCellValue("Status: ");
        rowTop.createCell(4).setCellValue("Nível de Acesso: ");

        rowNum++;

        for (Usuario usuario : usuarios){
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(usuario.getNome() != null ? usuario.getNome() : "");
            row.createCell(1).setCellValue(usuario.getRgm() != null ? usuario.getRgm().toString() : "");
            row.createCell(2).setCellValue(usuario.getEmail() != null ? usuario.getEmail() : "");
            row.createCell(3).setCellValue(usuario.getStatus() != null ? usuario.getStatus().toString() : "");
            row.createCell(4).setCellValue(usuario.getAcesso() != null ? usuario.getAcesso().toString() : "");
            rowNum++;
        }
        return workbook;
    }


    public Workbook agendaReport(Long startDate, Long endDate) {
        Validate.notNull(startDate, "Data de inicio não pode ser nula");
        Validate.notNull(endDate, "Data de termino não pode ser nula");

        List<Agenda> agendaList = agendaRepository.findAllByDateRange(startDate, endDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        int rowNum = 0;
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row rowTop = sheet.createRow(rowNum);
        rowTop.createCell(0).setCellValue("Data do agendamento: ");
        rowTop.createCell(1).setCellValue("Quantidade de crianças: ");
        rowTop.createCell(2).setCellValue("Coordenador responsável: ");
        rowTop.createCell(3).setCellValue("Professor responsável: ");
        rowTop.createCell(4).setCellValue("Monitor responsável: ");
        rowTop.createCell(5).setCellValue("Escola agendada: ");
        rowTop.createCell(6).setCellValue("Tipo de ensino: ");
        rowTop.createCell(7).setCellValue("Responsáveis da escola:");
        rowTop.createCell(8).setCellValue("Descrição da atividade: ");
        rowTop.createCell(9).setCellValue("Materiais usados: ");

        rowNum++;

        for (Agenda agenda : agendaList) {
            Row row = sheet.createRow(rowNum);

            Date dataAgenda = agenda.getData() != null ? new Date(agenda.getData()) : null;
            row.createCell(0).setCellValue(agenda.getData() != null ? simpleDateFormat.format(dataAgenda) : "");
            row.createCell(1).setCellValue(agenda.getCriancas() != null ? agenda.getCriancas().toString() : "");
            row.createCell(2).setCellValue(agenda.getCoordenator() != null ? agenda.getCoordenator().getNome() : "");
            row.createCell(3).setCellValue(agenda.getProfessor() != null ? agenda.getProfessor().getNome() : "");
            row.createCell(4).setCellValue(agenda.getMonitor() != null ? agenda.getMonitor().getNome() : "");
            row.createCell(5).setCellValue(agenda.getEscola() != null ? agenda.getEscola() : "");
            row.createCell(6).setCellValue(agenda.getTipoEnsino() != null ? agenda.getTipoEnsino().toString() : "");
            row.createCell(7).setCellValue(agenda.getResposaveis() != null ? listaToString(agenda, "responsavel") : " ");
            row.createCell(8).setCellValue(agenda.getDescricao() != null ? agenda.getDescricao() : "");
            row.createCell(9).setCellValue(agenda.getMateriais() != null ? listaToString(agenda, "material") : " ");

            rowNum++;
        }

        return workbook;
    }

    public String listaToString(Agenda agenda, String objeto) {
        String lista = "";
        if (objeto.equals("responsavel")) {
            if (agenda.getResposaveis() == null) {
                log.error("Agenda sem responsáveis cadastrados");
                return "";
            }
            for (Responsavel responsavel : agenda.getResposaveis()) {
                lista = lista + responsavel.getNome() + ",";
            }
            return lista;
        }else if(objeto.equals("material")){
            if (agenda.getMateriais() == null) {
                log.error("Agenda sem materiais cadastrados");
                return "";
            }
            for (Material material : agenda.getMateriais()) {
                lista = lista + material.getNome() + ","; //Atualizar o banco para funcionar
            }
            return lista;
        }else {
            log.error("tipo inexistente");
            return null;
        }
    }
}
