package io.github.waspstdnt.wishlist_app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.waspstdnt.wishlist_app.models.Template;
import io.github.waspstdnt.wishlist_app.repositories.TemplateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataSeederConfig {

    private final TemplateRepository templateRepository;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ApplicationRunner seedTemplates(ObjectMapper objectMapper) {
        return args -> {
            if (templateRepository.count() == 0) {
                Template movies = Template.builder()
                        .title("Фільми")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "жанр": "string",
                                    "директор": "string",
                                    "рік випуску": "integer",
                                    "тривалість (хв)": "integer",
                                    "оцінка": "decimal",
                                    "переглянуто": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(movies);

                Template tvShows = Template.builder()
                        .title("Серіали")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "жанр": "string",
                                    "к-ть сезонів": "integer",
                                    "к-ть епізодів": "integer",
                                    "мережа": "string",
                                    "статус": "string",
                                    "в процесі перегляду": "boolean",
                                    "переглянуто": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(tvShows);

                Template books = Template.builder()
                        .title("Книги")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "автор": "string",
                                    "жанр": "string",
                                    "сторінки": "integer",
                                    "рік випуску": "integer",
                                    "прочитано": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(books);

                Template videoGames = Template.builder()
                        .title("Відео Ігри")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "платформа": "string",
                                    "жанр": "string",
                                    "розробник": "string",
                                    "рік випуску": "integer",
                                    "пройдено": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(videoGames);

                Template musicAlbums = Template.builder()
                        .title("Музичні Альбоми")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "виконавець": "string",
                                    "жанр": "string",
                                    "рік випуску": "integer",
                                    "к-ть пісень": "integer",
                                    "улюблений": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(musicAlbums);
            }
        };
    }
}
