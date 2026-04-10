package com.cenfotec.backendcodesprint.logic.SupportProduct.Service;

import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Repository.SupportProductCatalogRepository;
import com.cenfotec.backendcodesprint.logic.Cloudinary.CloudinaryService;
import com.cenfotec.backendcodesprint.logic.Model.SupportProductCatalog;
import com.cenfotec.backendcodesprint.logic.Model.SupportProductPost;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.CreateArticleOfferRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.CreateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.UpdateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Response.SupportProductPostResponseDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.Repository.SupportProductPostRepository;
import com.cenfotec.backendcodesprint.logic.Model.ArticleOffer;
import com.cenfotec.backendcodesprint.logic.Model.OfferStatus;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Response.ArticleOfferResponseDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.Repository.ArticleOfferRepository;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupportProductService {

    private final SupportProductPostRepository postRepository;
    private final SupportProductCatalogRepository catalogRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final ArticleOfferRepository articleOfferRepository;

    @Transactional
    public void createPost(CreateSupportProductPostRequestDTO dto, MultipartFile image) {

        validateCreate(dto);

        SupportProductCatalog catalog = catalogRepository.findById(dto.getSupportProductCatalogId())
                .orElseThrow(() -> new RuntimeException("Categoría no válida"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no válido"));

        SupportProductPost post = new SupportProductPost();
        post.setUser(user);
        post.setSupportProductCatalog(catalog);
        post.setTitle(dto.getTitle().trim());
        post.setArticleDescription(dto.getDescription().trim());
        post.setConditionState(dto.getCondition().toUpperCase());
        post.setSalePrice(dto.getSalePrice());
        post.setOriginalPrice(dto.getOriginalPrice() != null ? dto.getOriginalPrice() : dto.getSalePrice());
        post.setAcceptOffers(Boolean.TRUE.equals(dto.getAcceptsOffers()));
        post.setPublicationState("ACTIVE");
        post.setLocationLatitude(dto.getLocationLat());
        post.setLocationLongitude(dto.getLocationLng());
        post.setLocationText(dto.getLocationText());
        post.setUsageTimeText(dto.getUsageTimeText());

        if (image != null && !image.isEmpty()) {
            Map<String, String> uploadResult = cloudinaryService.upload(image);
            post.setImageUrl(uploadResult.get("url"));
            post.setImagePath(uploadResult.get("path"));
        }

        postRepository.save(post);
    }

    public List<SupportProductPostResponseDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public SupportProductPostResponseDTO getPostById(Long id) {
        SupportProductPost post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada: " + id));

        return mapToResponseDTO(post);
    }

    @Transactional
    public SupportProductPostResponseDTO updatePost(Long id, UpdateSupportProductPostRequestDTO dto) {
        SupportProductPost post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada: " + id));

        if (dto.getSupportProductCatalogId() != null) {
            SupportProductCatalog catalog = catalogRepository.findById(dto.getSupportProductCatalogId())
                    .orElseThrow(() -> new RuntimeException("Categoría no válida"));
            post.setSupportProductCatalog(catalog);
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            post.setTitle(dto.getTitle().trim());
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            post.setArticleDescription(dto.getDescription().trim());
        }

        if (dto.getCondition() != null && !dto.getCondition().isBlank()) {
            String condition = dto.getCondition().toUpperCase();
            if (!condition.equals("NEW") && !condition.equals("USED")) {
                throw new RuntimeException("La condición debe ser NEW o USED");
            }
            post.setConditionState(condition);
        }

        if (dto.getSalePrice() != null) {
            if (dto.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Precio debe ser mayor a 0");
            }
            post.setSalePrice(dto.getSalePrice());
        }

        if (dto.getOriginalPrice() != null) {
            post.setOriginalPrice(dto.getOriginalPrice());
        }

        if (dto.getAcceptsOffers() != null) {
            post.setAcceptOffers(dto.getAcceptsOffers());
        }

        if (dto.getLocationLat() != null) {
            post.setLocationLatitude(dto.getLocationLat());
        }

        if (dto.getLocationLng() != null) {
            post.setLocationLongitude(dto.getLocationLng());
        }

        if (dto.getLocationText() != null) {
            post.setLocationText(dto.getLocationText());
        }

        if (dto.getUsageTimeText() != null) {
            post.setUsageTimeText(dto.getUsageTimeText());
        }

        if (dto.getPublicationState() != null && !dto.getPublicationState().isBlank()) {
            post.setPublicationState(dto.getPublicationState().toUpperCase());
        }

        SupportProductPost updated = postRepository.save(post);
        return mapToResponseDTO(updated);
    }

    @Transactional
    public void deletePost(Long id) {
        SupportProductPost post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada: " + id));

        postRepository.delete(post);
    }

    private void validateCreate(CreateSupportProductPostRequestDTO dto) {
        if (dto.getUserId() == null) {
            throw new RuntimeException("El usuario es requerido");
        }

        if (dto.getSupportProductCatalogId() == null) {
            throw new RuntimeException("La categoría es requerida");
        }

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new RuntimeException("Complete los campos requeridos");
        }

        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new RuntimeException("Complete los campos requeridos");
        }

        if (dto.getCondition() == null || dto.getCondition().isBlank()) {
            throw new RuntimeException("Complete los campos requeridos");
        }

        String condition = dto.getCondition().toUpperCase();
        if (!condition.equals("NEW") && !condition.equals("USED")) {
            throw new RuntimeException("La condición debe ser NEW o USED");
        }

        if (dto.getSalePrice() == null || dto.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Precio debe ser mayor a 0");
        }

        if (dto.getLocationLat() == null || dto.getLocationLng() == null) {
            throw new RuntimeException("La ubicación es requerida");
        }
    }
    @Transactional
    public ArticleOfferResponseDTO createOffer(CreateArticleOfferRequestDTO dto) {

        if (dto.getSupportProductPostId() == null) {
            throw new RuntimeException("La publicación es requerida");
        }

        if (dto.getBuyerUserId() == null) {
            throw new RuntimeException("El comprador es requerido");
        }

        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("La oferta debe ser mayor a 0");
        }

        SupportProductPost post = postRepository.findById(dto.getSupportProductPostId())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        User buyer = userRepository.findById(dto.getBuyerUserId())
                .orElseThrow(() -> new RuntimeException("Usuario comprador no encontrado"));

        if (!Boolean.TRUE.equals(post.getAcceptOffers())) {
            throw new RuntimeException("Esta publicación no acepta ofertas");
        }

        if (!"ACTIVE".equalsIgnoreCase(post.getPublicationState())) {
            throw new RuntimeException("La publicación no está disponible para recibir ofertas");
        }

        if (post.getUser() != null && post.getUser().getId().equals(buyer.getId())) {
            throw new RuntimeException("No puedes hacer una oferta sobre tu propia publicación");
        }

        ArticleOffer offer = new ArticleOffer();
        offer.setSupportProductPost(post);
        offer.setBuyerUser(buyer);
        offer.setAmount(dto.getAmount());
        offer.setMessage(dto.getMessage());
        offer.setOfferState(OfferStatus.PENDING);

        ArticleOffer saved = articleOfferRepository.save(offer);

        return mapOfferToResponseDTO(saved);
    }

    private SupportProductPostResponseDTO mapToResponseDTO(SupportProductPost post) {
        SupportProductPostResponseDTO dto = new SupportProductPostResponseDTO();

        dto.setId(post.getId());

        dto.setUserId(post.getUser() != null ? post.getUser().getId() : null);
        dto.setUserName(post.getUser() != null ? post.getUser().getUserName() : null);
        dto.setUserLastName(post.getUser() != null ? post.getUser().getLastName() : null);
        dto.setUserEmail(post.getUser() != null ? post.getUser().getEmail() : null);

        dto.setSupportProductCatalogId(post.getSupportProductCatalog() != null ? post.getSupportProductCatalog().getId() : null);
        dto.setSupportProductCatalogName(post.getSupportProductCatalog() != null ? post.getSupportProductCatalog().getBaseName() : null);

        dto.setTitle(post.getTitle());
        dto.setDescription(post.getArticleDescription());
        dto.setCondition(post.getConditionState());
        dto.setSalePrice(post.getSalePrice());
        dto.setOriginalPrice(post.getOriginalPrice());
        dto.setAcceptsOffers(post.getAcceptOffers());
        dto.setPublicationState(post.getPublicationState());

        dto.setLocationLat(post.getLocationLatitude());
        dto.setLocationLng(post.getLocationLongitude());
        dto.setLocationText(post.getLocationText());
        dto.setUsageTimeText(post.getUsageTimeText());
        dto.setImageUrl(post.getImageUrl());
        dto.setImagePath(post.getImagePath());

        dto.setCreated(post.getCreated());
        dto.setUpdated(post.getUpdated());

        return dto;
    }

    private ArticleOfferResponseDTO mapOfferToResponseDTO(ArticleOffer offer) {
        ArticleOfferResponseDTO dto = new ArticleOfferResponseDTO();

        SupportProductPost post = offer.getSupportProductPost();

        dto.setId(offer.getId());

        dto.setSupportProductPostId(
                post != null ? post.getId() : null
        );

        dto.setSupportProductTitle(
                post != null ? post.getTitle() : null
        );

        dto.setSupportProductImageUrl(
                post != null ? post.getImageUrl() : null
        );

        dto.setSellerUserId(
                post != null && post.getUser() != null ? post.getUser().getId() : null
        );

        dto.setSellerName(
                post != null && post.getUser() != null ? post.getUser().getUserName() : null
        );

        // comprador
        dto.setBuyerUserId(
                offer.getBuyerUser() != null ? offer.getBuyerUser().getId() : null
        );

        dto.setBuyerUserName(
                offer.getBuyerUser() != null ? offer.getBuyerUser().getUserName() : null
        );

        dto.setAmount(offer.getAmount());
        dto.setMessage(offer.getMessage());
        dto.setOfferState(offer.getOfferState());

        dto.setCreated(offer.getCreated());
        dto.setUpdated(offer.getUpdated());

        return dto;
    }
    @Transactional
    public ArticleOfferResponseDTO acceptOffer(Long offerId, Long ownerUserId) {
        System.out.println("ENTRO AL ENTRO AL SERVICE");
        System.out.println("offerId = " + offerId);
        System.out.println("ownerUserId = " + ownerUserId);

        ArticleOffer offer = articleOfferRepository.findByIdAndSupportProductPostUserId(offerId, ownerUserId)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada o no pertenece a este usuario"));

        if (offer.getOfferState() != OfferStatus.PENDING) {
            throw new RuntimeException("Solo se pueden aceptar ofertas pendientes");
        }

        SupportProductPost post = offer.getSupportProductPost();

        if (!Boolean.TRUE.equals(post.getAcceptOffers())) {
            throw new RuntimeException("Esta publicación no acepta ofertas");
        }

        offer.setOfferState(OfferStatus.ACCEPTED);
        articleOfferRepository.save(offer);

        List<ArticleOffer> pendingOffers =
                articleOfferRepository.findBySupportProductPostIdAndOfferState(post.getId(), OfferStatus.PENDING);

        for (ArticleOffer otherOffer : pendingOffers) {
            if (!otherOffer.getId().equals(offer.getId())) {
                otherOffer.setOfferState(OfferStatus.REJECTED);
            }
        }

        articleOfferRepository.saveAll(pendingOffers);

        post.setPublicationState("RESERVED");
        post.setAcceptOffers(Boolean.FALSE);
        postRepository.save(post);

        return mapOfferToResponseDTO(offer);
    }
    @Transactional
    public ArticleOfferResponseDTO rejectOffer(Long offerId, Long ownerUserId) {

        ArticleOffer offer = articleOfferRepository.findByIdAndSupportProductPostUserId(offerId, ownerUserId)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada o no pertenece a este usuario"));

        if (offer.getOfferState() != OfferStatus.PENDING) {
            throw new RuntimeException("Solo se pueden rechazar ofertas pendientes");
        }

        offer.setOfferState(OfferStatus.REJECTED);
        articleOfferRepository.save(offer);

        return mapOfferToResponseDTO(offer);
    }


    @Transactional(readOnly = true)
    public List<ArticleOfferResponseDTO> getOffersByPostId(Long postId) {
        return articleOfferRepository.findBySupportProductPostIdOrderByCreatedDesc(postId)
                .stream()
                .map(this::mapOfferToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleOfferResponseDTO> getOffersReceivedByOwner(Long ownerUserId) {
        return articleOfferRepository.findBySupportProductPostUserIdOrderByCreatedDesc(ownerUserId)
                .stream()
                .map(this::mapOfferToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleOfferResponseDTO> getOffersMadeByBuyer(Long buyerUserId) {
        return articleOfferRepository.findByBuyerUserIdOrderByCreatedDesc(buyerUserId)
                .stream()
                .map(this::mapOfferToResponseDTO)
                .toList();
    }

}