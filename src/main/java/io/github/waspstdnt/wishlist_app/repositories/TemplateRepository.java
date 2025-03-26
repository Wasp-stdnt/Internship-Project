package io.github.waspstdnt.wishlist_app.repositories;

import io.github.waspstdnt.wishlist_app.models.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByTitle(String title);
}
