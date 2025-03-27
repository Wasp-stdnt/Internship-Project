package io.github.waspstdnt.wishlist_app.services.impl;

import io.github.waspstdnt.wishlist_app.exceptions.TemplateNotFoundException;
import io.github.waspstdnt.wishlist_app.models.Template;
import io.github.waspstdnt.wishlist_app.repositories.TemplateRepository;
import io.github.waspstdnt.wishlist_app.services.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Template getTemplateById(Long id) throws TemplateNotFoundException {
        Optional<Template> templateOpt = templateRepository.findById(id);
        if (templateOpt.isEmpty()) {
            throw new TemplateNotFoundException("Template not found with id: " + id);
        }
        return templateOpt.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Template getTemplateByTitle(String title) throws TemplateNotFoundException {
        Optional<Template> templateOpt = templateRepository.findByTitle(title);
        if (templateOpt.isEmpty()) {
            throw new TemplateNotFoundException("Template not found with title: " + title);
        }
        return templateOpt.get();
    }
}
