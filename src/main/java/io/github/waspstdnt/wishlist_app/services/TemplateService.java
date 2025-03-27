package io.github.waspstdnt.wishlist_app.services;

import io.github.waspstdnt.wishlist_app.exceptions.TemplateNotFoundException;
import io.github.waspstdnt.wishlist_app.models.Template;

import java.util.List;

public interface TemplateService {

    List<Template> getAllTemplates();

    Template getTemplateById(Long id) throws TemplateNotFoundException;

    Template getTemplateByTitle(String title) throws TemplateNotFoundException;
}
