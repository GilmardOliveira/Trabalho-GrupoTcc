package laboratorioPedag.UMC_TCC_BACKEND.material.rest;

import laboratorioPedag.UMC_TCC_BACKEND.material.dal.MaterialRepository;
import laboratorioPedag.UMC_TCC_BACKEND.material.model.Material;
import laboratorioPedag.UMC_TCC_BACKEND.material.service.MaterialService;
import org.apache.commons.lang3.Validate;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/v1/material")
public class MaterialController {

    private MaterialRepository materialRepository;
    private MaterialService materialService;

    public MaterialController(MaterialRepository materialRepository, MaterialService materialService) {
        this.materialRepository = materialRepository;
        this.materialService = materialService;
    }

    @GetMapping("/getAll")
    public List<Material> getAll() {
        List<Material> agendas = materialRepository.findAll();
        return agendas;
    }

    @GetMapping("getAll/{status}")
    public List<Material> getAllByStatus(@PathVariable String status) throws Exception {

        if (status.toUpperCase().equals("ATIVO")) {
            return materialRepository.findAllByStatus(Material.Status.ATIVO);
        } else if (status.toUpperCase().equals("DELETADO")) {
            return materialRepository.findAllByStatus(Material.Status.DELETADO);
        } else {
            throw new Exception("Status invalido");
        }

    }

    @PostMapping("/change-status/{status}/{id}")
    public void changeStatus(@PathVariable String status, @PathVariable Long id) throws Exception {
        Material material = materialRepository.findById(id).orElse(null);
        Validate.notNull(material, "Material não encontrado");

        if (status.toUpperCase().equals("ATIVO")) {
            material.setStatus(Material.Status.ATIVO);
            materialRepository.save(material);
        } else if (status.toUpperCase().equals("DELETADO")) {
            material.setStatus(Material.Status.DELETADO);
            materialRepository.save(material);
        } else {
            throw new Exception("Status invalido");
        }

    }

    @PostMapping
    public Material saveOrUpdate(@RequestBody Material newMaterial) {
        Validate.notNull(newMaterial, "O objeto do material não pode ser nulo");

        if (newMaterial.getId() == null) {
            newMaterial.setDataLancamento(System.currentTimeMillis());
            materialRepository.save(newMaterial);
            return newMaterial;
        }

        return materialService.updateMaterial(newMaterial);
    }

    @GetMapping("/{id}")
    public Material get(@PathVariable Long id) {
        Validate.notNull(id, "Id do material não pode ser nulo");
        return materialRepository.findById(id).orElse(null);
    }

    @GetMapping("/getByName/{materialNome}")
    public List<Material> getByName(@PathVariable String materialNome) {
        Validate.notNull(materialNome, "nome do não pode ser nulo");
        return materialRepository.findAllByNomeContains(materialNome);
    }

    @GetMapping("/getByClasse/{classe}")
    public List<Material> getByClasse(@PathVariable String classe) throws Exception {
        Material.Classe enumClasse = materialService.buildMaterialClasse(classe);
        if (enumClasse == null) {
            throw new Exception("Classe não encontrada");
        }
        return materialRepository.findAllByClasse(enumClasse);
    }

}
