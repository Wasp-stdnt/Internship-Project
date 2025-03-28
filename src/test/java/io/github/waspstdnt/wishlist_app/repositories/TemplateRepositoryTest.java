//package io.github.waspstdnt.wishlist_app.repositories;
//
//import io.github.waspstdnt.wishlist_app.config.DataSeederConfig;
//import io.github.waspstdnt.wishlist_app.models.Template;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ActiveProfiles("test")
//@DataJpaTest
//@Import(DataSeederConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class TemplateRepositoryTest {
//
//    @Autowired
//    private TemplateRepository templateRepository;
//
//    @Test
//    public void testTemplatesAreSeeded() {
//        long count = templateRepository.count();
//        assertThat(count).isGreaterThan(0);
//    }
//
//    @Test
//    public void testFindTemplateByName() {
//        Optional<Template> templateOpt = templateRepository.findByTitle("Movies");
//        assertThat(templateOpt).isPresent();
//        Template template = templateOpt.get();
//        assertThat(template.getTemplateFields().toString()).contains("genre");
//    }
//}
