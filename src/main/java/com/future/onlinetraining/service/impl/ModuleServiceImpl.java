package com.future.onlinetraining.service.impl;

import com.future.onlinetraining.dto.DeleteModuleCategoryDTO;
import com.future.onlinetraining.dto.UpdateModuleCategoryDTO;
import com.future.onlinetraining.dto.UpdateModuleDTO;
import com.future.onlinetraining.entity.Module;
import com.future.onlinetraining.entity.ModuleCategory;
import com.future.onlinetraining.entity.ModuleRating;
import com.future.onlinetraining.entity.projection.ModuleData;
import com.future.onlinetraining.entity.projection.ModuleDetailData;
import com.future.onlinetraining.repository.ModuleCategoryRepository;
import com.future.onlinetraining.repository.ModuleRatingRepository;
import com.future.onlinetraining.repository.ModuleRepository;
import com.future.onlinetraining.service.ModuleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service("moduleService")
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    ModuleRatingRepository moduleRatingRepository;
    @Autowired
    ModuleCategoryRepository moduleCategoryRepository;

//    public Page<ModuleData> getAll(Pageable pageable) {
//        return moduleRepository.getAllModule(pageable);
//    }

    public Page<ModuleRating> getRatings(int id, Pageable pageable) {
        return moduleRatingRepository.findAllByModuleId(id, pageable);
    }

    public Page<ModuleData> getAllBySearchTerm(
            Pageable pageable, String name, String category, Boolean hasExam) {
        return moduleRepository.getAllBySearhTerm(pageable, name, category, hasExam);
    }

    public Page<ModuleCategory> getAllModuleCategory(Pageable pageable) {
        return moduleCategoryRepository.findAll(pageable);
    }

    public ModuleCategory addModuleCategory(ModuleCategory moduleCategory) {
        ModuleCategory category = moduleCategoryRepository.findByName(moduleCategory.getName());
        if (category != null)
            return null;

        return moduleCategoryRepository.save(moduleCategory);
    }

    public ModuleCategory updateModuleCategory(UpdateModuleCategoryDTO updateModuleCategoryDTO) {
        ModuleCategory category = moduleCategoryRepository.findByName(updateModuleCategoryDTO.getModuleCategory().getName());
        if (category == null)
            return null;

        category.setName(updateModuleCategoryDTO.getNewCategoryName());
        return moduleCategoryRepository.save(category);
    }

    public boolean deleteModuleCategory(DeleteModuleCategoryDTO deleteModuleCategoryDTO) {
        ModuleCategory category = moduleCategoryRepository.findByName(deleteModuleCategoryDTO.getName());
        if (category == null)
            return false;

        moduleCategoryRepository.delete(category);
        return true;
    }

    public Module getOne(Integer id) {
        Module module;
        try {
            module = moduleRepository.getOne(id);
        }catch (Exception e) {
            module = null;
        }
        return module;
    }

    public Module editModule(Integer id, UpdateModuleDTO updateModuleDTO) {
        ModuleCategory category = moduleCategoryRepository.findByName(updateModuleDTO.getModuleCategory());

        if (category == null)
            return null;

        Module module = moduleRepository.getOne(id);

        if (module == null)
            return null;

        module.setModuleCategory(category);
        module.setDescription(updateModuleDTO.getDescription());
        module.setName(updateModuleDTO.getName());
        module.setStatus(updateModuleDTO.getStatus());
        module.setTimePerSession(updateModuleDTO.getTimePerSession());
        module.setMaterialDescription(updateModuleDTO.getMaterialDescription());
        module.setVersion(module.getVersion() + 1);

        return moduleRepository.save(module);
    }

    public boolean deleteModule(Integer id) {
        Module module = moduleRepository.getOne(id);
        System.out.println(module.getId());
        if (module == null)
            return false;

        moduleRepository.deleteById(module.getId());
        return true;
    }

    public ModuleDetailData getModuleDetail(Integer id) {
        return moduleRepository.find(id);
    }
}
