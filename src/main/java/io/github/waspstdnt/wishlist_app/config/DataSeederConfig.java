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
                        .title("Movies")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "genre": "string",
                                    "director": "string",
                                    "release_year": "integer",
                                    "duration_minutes": "integer",
                                    "rating": "decimal",
                                    "watched": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(movies);

                Template tvShows = Template.builder()
                        .title("TV Shows")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "genre": "string",
                                    "seasons": "integer",
                                    "episodes": "integer",
                                    "network": "string",
                                    "status": "string",
                                    "watching": "boolean",
                                    "watched": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(tvShows);

                Template books = Template.builder()
                        .title("Books")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "author": "string",
                                    "genre": "string",
                                    "pages": "integer",
                                    "published_year": "integer",
                                    "read": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(books);

                Template videoGames = Template.builder()
                        .title("Video Games")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "platform": "string",
                                    "genre": "string",
                                    "developer": "string",
                                    "release_year": "integer",
                                    "completed": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(videoGames);

                Template musicAlbums = Template.builder()
                        .title("Music Albums")
                        .templateFields(objectMapper.readValue(
                                """
                                {
                                    "artist": "string",
                                    "genre": "string",
                                    "release_year": "integer",
                                    "track_count": "integer",
                                    "favorite": "boolean"
                                }
                                """, Map.class))
                        .build();
                templateRepository.save(musicAlbums);
            }
        };
    }
}
