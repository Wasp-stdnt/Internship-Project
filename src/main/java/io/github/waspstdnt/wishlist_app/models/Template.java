package io.github.waspstdnt.wishlist_app.models;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.*;
import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="template_fields", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> templateFields;
}
