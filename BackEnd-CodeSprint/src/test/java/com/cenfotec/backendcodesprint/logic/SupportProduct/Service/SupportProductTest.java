package com.cenfotec.backendcodesprint.logic.SupportProduct.Service;

import com.cenfotec.backendcodesprint.logic.Cloudinary.CloudinaryService;
import com.cenfotec.backendcodesprint.logic.Model.SupportProductCatalog;
import com.cenfotec.backendcodesprint.logic.Model.SupportProductPost;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.CreateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.UpdateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Response.SupportProductPostResponseDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.Repository.SupportProductCatalogRepository;
import com.cenfotec.backendcodesprint.logic.SupportProduct.Repository.SupportProductPostRepository;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class SupportProductServiceTest {

    @Mock
    private SupportProductPostRepository postRepository;

    @Mock
    private SupportProductCatalogRepository catalogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private SupportProductService supportProductService;

    private User user;
    private SupportProductCatalog catalog;
    private SupportProductPost post;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        catalog = new SupportProductCatalog();
        catalog.setId(2L);

        post = new SupportProductPost();
        post.setId(10L);
        post.setUser(user);
        post.setSupportProductCatalog(catalog);
        post.setPublicationState("ACTIVE");
    }

    // Crear Post exitoso

    @Test
    void createPost_ValidData_ShouldSavePost() {

        CreateSupportProductPostRequestDTO dto = new CreateSupportProductPostRequestDTO();
        dto.setUserId(1L);
        dto.setSupportProductCatalogId(2L);
        dto.setTitle("Producto");
        dto.setDescription("Descripción");
        dto.setCondition("NEW");
        dto.setSalePrice(BigDecimal.valueOf(100));
        dto.setLocationLat(BigDecimal.valueOf(10.0));
        dto.setLocationLng(BigDecimal.valueOf(20.0));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(catalogRepository.findById(2L)).thenReturn(Optional.of(catalog));

        supportProductService.createPost(dto, null);

        verify(postRepository, times(1)).save(any(SupportProductPost.class));
    }

    // Crear Post con usuario inválido

    @Test
    void createPost_InvalidUser_ShouldThrowException() {

        CreateSupportProductPostRequestDTO dto = new CreateSupportProductPostRequestDTO();
        dto.setUserId(99L);
        dto.setSupportProductCatalogId(2L);
        dto.setTitle("Producto");
        dto.setDescription("Descripción");
        dto.setCondition("NEW");
        dto.setSalePrice(BigDecimal.valueOf(100));
        dto.setLocationLat(BigDecimal.valueOf(10.0));
        dto.setLocationLng(BigDecimal.valueOf(20.0));

        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        when(catalogRepository.findById(2L)).thenReturn(Optional.of(catalog));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            supportProductService.createPost(dto, null);
        });

        assertTrue(ex.getMessage().contains("Usuario no válido"));
    }

    // Obtener Post por ID

    @Test
    void getPostById_ValidId_ShouldReturnPost() {

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));

        SupportProductPostResponseDTO result = supportProductService.getPostById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    // Post no encontrado

    @Test
    void getPostById_NotFound_ShouldThrowException() {

        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            supportProductService.getPostById(99L);
        });

        assertTrue(ex.getMessage().contains("Publicación no encontrada"));
    }

    // Actualizar Post exitoso

    @Test
    void updatePost_ValidData_ShouldUpdatePost() {

        UpdateSupportProductPostRequestDTO dto = new UpdateSupportProductPostRequestDTO();
        dto.setTitle("Nuevo título");
        dto.setSalePrice(BigDecimal.valueOf(200));

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));
        when(postRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SupportProductPostResponseDTO result = supportProductService.updatePost(10L, dto);

        assertNotNull(result);
        assertEquals("Nuevo título", post.getTitle());
        assertEquals(BigDecimal.valueOf(200), post.getSalePrice());
    }

    // Precio inválido

    @Test
    void updatePost_InvalidPrice_ShouldThrowException() {

        UpdateSupportProductPostRequestDTO dto = new UpdateSupportProductPostRequestDTO();
        dto.setSalePrice(BigDecimal.ZERO);

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            supportProductService.updatePost(10L, dto);
        });

        assertTrue(ex.getMessage().contains("Precio debe ser mayor a 0"));
    }

    // Eliminar Post

    @Test
    void deletePost_ValidId_ShouldDeletePost() {

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));

        supportProductService.deletePost(10L);

        verify(postRepository, times(1)).delete(post);
    }

    // Eliminar Post no encontrado

    @Test
    void deletePost_NotFound_ShouldThrowException() {

        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            supportProductService.deletePost(99L);
        });

        assertTrue(ex.getMessage().contains("Publicación no encontrada"));
    }
}