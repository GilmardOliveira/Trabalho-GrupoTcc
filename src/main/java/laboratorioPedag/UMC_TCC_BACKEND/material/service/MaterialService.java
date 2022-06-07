package laboratorioPedag.UMC_TCC_BACKEND.material.service;
import laboratorioPedag.UMC_TCC_BACKEND.agenda.dto.QuantidadeMaterialUtilizadoDto;
import laboratorioPedag.UMC_TCC_BACKEND.material.dal.MaterialRepository;
import laboratorioPedag.UMC_TCC_BACKEND.material.model.Material;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
public class MaterialService {

    private MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository){
        this.materialRepository = materialRepository;
    }

    public Boolean verificaQuantidade(Long materialId) {
        Validate.notNull(materialId, "ID do material não pode ser nulo.");

        Double quantidade = materialRepository.findQuantidadeByMaterial(materialId);
        Validate.notNull(quantidade, "Material sem quantidade");

        Double quantidadeMinima = materialRepository.findQuantidadeMinimaByMaterial(materialId);
        Validate.notNull(quantidadeMinima, "Material sem quantidade minima");

        if (quantidade < quantidadeMinima) {
            return false;
        }

        return true;
    }
    public void darBaixaMaterialBaseadoNaAgenda(List<QuantidadeMaterialUtilizadoDto> quantidadeMaterialUtilizadoDto){

        for (QuantidadeMaterialUtilizadoDto dto : quantidadeMaterialUtilizadoDto) {
            Validate.notNull(dto.quantidadeUtilizada, "Quantidade utilizada deve ser informada");
            Validate.notNull(dto.materialId, "ID do material deve ser informado");

            Material material = materialRepository.findById(dto.materialId).orElse(null);
            Validate.notNull(material, "Material não encontrado");

            if (material.getCategoria().equals(Material.Categoria.CONSUMIVEL)) {
                material.setQuantidade(material.getQuantidade() - dto.quantidadeUtilizada);
            }

            materialRepository.save(material);
        }
    }

//    public void updateBaixaMaterial(Double quantidadeUtilizada, Long materialId){
//        Validate.notNull(quantidadeUtilizada, "Quantidade utilizada deve ser informada");
//        Validate.notNull(quantidadeUtilizada, "ID do material deve ser informado");
//
//        Material material = materialRepository.findById(materialId).orElse(null);
//        Validate.notNull(material, "Material não encontrado");
//
//        if(material.getCategoria().equals(Material.Categoria.CONSUMIVEL)) {
//            material.setQuantidade(material.getQuantidade() - quantidadeUtilizada);
//        }
//
//        materialRepository.save(material);
//    }

    public Material updateMaterial(Material newMaterial) {
        Material material = materialRepository.findById(newMaterial.getId()).orElse(null);

        ofNullable(newMaterial.getNome()).ifPresent(material :: setNome);
        ofNullable(newMaterial.getDescricao()).ifPresent(material::setDescricao);
        ofNullable(newMaterial.getQuantidade()).ifPresent(material::setQuantidade);
        ofNullable(newMaterial.getQuantidadeMinima()).ifPresent(material::setQuantidadeMinima);
        ofNullable(newMaterial.getDataLancamento()).ifPresent(material :: setDataLancamento);
        ofNullable(newMaterial.getClasse()).ifPresent(material :: setClasse);
        ofNullable(newMaterial.getCategoria()).ifPresent(material :: setCategoria);
        ofNullable(newMaterial.getEmbalagem()).ifPresent(material::setEmbalagem);

        return materialRepository.save(material);
    }

    public Material.Classe buildMaterialClasse(String classe){
        classe = classe.toUpperCase().trim();
        switch (classe) {
            case "CIENCIA":
                return Material.Classe.CIENCIA;
            case "DESENVOLVIMENTOCOGNITIVO":
                return Material.Classe.DESENVOLVIMENTO_COGNITIVO;
            case "EDUCACAOFISICA":
                return Material.Classe.EDUCACAO_FISICA;
            case "LINGUAPORTUGUESA":
                return Material.Classe.LINGUA_PORTUGUESA;
            case "MATEMATICA":
                return Material.Classe.MATEMATICA;
            case "NATUREZAESOCIEDADE ":
                return Material.Classe.NATUREZA_E_SOCIEDADE;
            case "OUTROS":
                return Material.Classe.OUTROS;
            default:
                log.error("Materia invalida.");
                return null;
        }
    }


}
