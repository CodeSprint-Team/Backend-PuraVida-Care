package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "article_photo")
public class ArticlePhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_photo_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "support_product_post_id", nullable = false)
    @NotNull
    private SupportProductPost supportProductPost;

    @Column(name = "photo_url", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String photoUrl;
}
