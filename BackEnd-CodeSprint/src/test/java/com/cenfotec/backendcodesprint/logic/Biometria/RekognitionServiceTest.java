package com.cenfotec.backendcodesprint.logic.Biometria;

import com.cenfotec.backendcodesprint.logic.Rekognition.RekognitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.CompareFacesRequest;
import software.amazon.awssdk.services.rekognition.model.CompareFacesResponse;
import software.amazon.awssdk.services.rekognition.model.FaceMatch;
import software.amazon.awssdk.services.rekognition.model.InvalidParameterException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RekognitionServiceTest {

    @Mock
    private RekognitionClient rekognitionClient;

    @InjectMocks
    private RekognitionService rekognitionService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rekognitionService, "similarityThreshold", 80f);
    }

    @Test
    void compareFaces_WhenFacesMatch_ShouldReturnTrue() {

        MockMultipartFile idFront = new MockMultipartFile(
                "idFront",
                "id.jpg",
                "image/jpeg",
                "fake-image".getBytes()
        );

        MockMultipartFile selfie = new MockMultipartFile(
                "selfie",
                "selfie.jpg",
                "image/jpeg",
                "fake-image".getBytes()
        );

        FaceMatch faceMatch = FaceMatch.builder()
                .similarity(95f)
                .build();

        CompareFacesResponse response = CompareFacesResponse.builder()
                .faceMatches(f -> f.similarity(95f))
                .build();

        when(rekognitionClient.compareFaces(any(CompareFacesRequest.class)))
                .thenReturn(response);

        boolean result = rekognitionService.compareFaces(idFront, selfie);

        assertTrue(result);
        verify(rekognitionClient, times(1)).compareFaces(any(CompareFacesRequest.class));
    }

    @Test
    void compareFaces_WhenFacesDoNotMatch_ShouldReturnFalse() {

        MockMultipartFile idFront = new MockMultipartFile(
                "idFront",
                "id.jpg",
                "image/jpeg",
                "fake-image".getBytes()
        );

        MockMultipartFile selfie = new MockMultipartFile(
                "selfie",
                "selfie.jpg",
                "image/jpeg",
                "fake-image".getBytes()
        );

        CompareFacesResponse response = CompareFacesResponse.builder()
                .faceMatches(f -> f.similarity(50f))
                .build();

        when(rekognitionClient.compareFaces(any(CompareFacesRequest.class)))
                .thenReturn(response);

        ReflectionTestUtils.setField(rekognitionService, "similarityThreshold", 80f);

        boolean result = rekognitionService.compareFaces(idFront, selfie);

        assertFalse(result);
    }

    @Test
    void compareFaces_WhenNoFaceDetected_ShouldThrowRuntimeException() {

        MockMultipartFile idFront = new MockMultipartFile(
                "idFront",
                "id.jpg",
                "image/jpeg",
                "fake-image".getBytes()
        );

        MockMultipartFile selfie = new MockMultipartFile(
                "selfie",
                "selfie.jpg",
                "image/jpeg",
                "fake-image".getBytes()
        );

        when(rekognitionClient.compareFaces(any(CompareFacesRequest.class)))
                .thenThrow(InvalidParameterException.builder()
                        .message("No face detected")
                        .build());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rekognitionService.compareFaces(idFront, selfie);
        });

        assertTrue(exception.getMessage().contains("No se detectó un rostro válido"));
    }

    @Test
    void compareFaces_WhenImageReadFails_ShouldThrowRuntimeException() throws IOException {

        MockMultipartFile selfie = new MockMultipartFile(
                "selfie",
                "selfie.jpg",
                "image/jpeg",
                "fake-image".getBytes()
        );

        MockMultipartFile brokenFile = mock(MockMultipartFile.class);

        when(brokenFile.getBytes()).thenThrow(new IOException("Read error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rekognitionService.compareFaces(brokenFile, selfie);
        });

        assertTrue(exception.getMessage().contains("Error al leer los archivos de imagen"));
    }
}


