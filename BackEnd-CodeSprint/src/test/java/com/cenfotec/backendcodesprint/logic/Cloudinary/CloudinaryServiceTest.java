package com.cenfotec.backendcodesprint.logic.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void upload_ShouldReturnUrlAndPath() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "fake-image".getBytes()
        );

        Map<String, Object> cloudinaryResponse = new HashMap<>();
        cloudinaryResponse.put("secure_url", "https://cloudinary.com/test.jpg");
        cloudinaryResponse.put("public_id", "verificaciones/test123");

        when(uploader.upload(any(byte[].class), any(Map.class)))
                .thenReturn(cloudinaryResponse);

        Map<String, String> result = cloudinaryService.upload(file);

        assertNotNull(result);
        assertEquals("https://cloudinary.com/test.jpg", result.get("url"));
        assertEquals("verificaciones/test123", result.get("path"));

        verify(uploader, times(1)).upload(any(byte[].class), any(Map.class));
    }

    @Test
    void upload_WhenCloudinaryFails_ShouldThrowRuntimeException() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "fake-image".getBytes()
        );

        when(uploader.upload(any(byte[].class), any(Map.class)))
                .thenThrow(new RuntimeException("Upload failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cloudinaryService.upload(file);
        });

        assertTrue(exception.getMessage().contains("Error en Cloudinary upload"));
    }

    @Test
    void delete_ShouldCallDestroySuccessfully() throws Exception {

        when(uploader.destroy(anyString(), any(Map.class)))
                .thenReturn(Map.of("result", "ok"));

        assertDoesNotThrow(() -> cloudinaryService.Delete("verificaciones/test123"));

        verify(uploader, times(1)).destroy(anyString(), any(Map.class));
    }

    @Test
    void delete_WhenCloudinaryFails_ShouldThrowRuntimeException() throws Exception {

        when(uploader.destroy(anyString(), any(Map.class)))
                .thenThrow(new RuntimeException("Delete failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cloudinaryService.Delete("verificaciones/test123");
        });

        assertTrue(exception.getMessage().contains("Error en Cloudinary Delete"));
    }
}

