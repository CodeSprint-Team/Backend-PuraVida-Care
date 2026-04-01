package com.cenfotec.backendcodesprint.logic.Biometria;

import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Request.CreateRequestVerificationDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Request.UpdateRequestVerificationDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Response.IdentityVerificationResponseDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Mapper.IdentityVerificationMapper;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Repository.FileVerificationRepository;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Repository.IdentityVerificationRepository;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Service.IdentityVerificationService;
import com.cenfotec.backendcodesprint.logic.Cloudinary.CloudinaryService;
import com.cenfotec.backendcodesprint.logic.Model.IdentityVerification;
import com.cenfotec.backendcodesprint.logic.Rekognition.RekognitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentityVerificationServiceTest {

    @Mock
    private IdentityVerificationRepository identityRepo;

    @Mock
    private FileVerificationRepository fileRepo;

    @Mock
    private IdentityVerificationMapper mapper;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private RekognitionService rekognitionService;

    @InjectMocks
    private IdentityVerificationService identityVerificationService;

    private IdentityVerification identityVerification;
    private IdentityVerificationResponseDto responseDto;

    @BeforeEach
    void setUp() {
        identityVerification = new IdentityVerification();
        identityVerification.setId(1L);

        responseDto = new IdentityVerificationResponseDto();
    }

    @Test
    void create_WhenFacesMatch_ShouldSaveApprovedVerification() {

        MultipartFile idFront = new MockMultipartFile("idFront", "front.jpg", "image/jpeg", "front".getBytes());
        MultipartFile idBack = new MockMultipartFile("idBack", "back.jpg", "image/jpeg", "back".getBytes());
        MultipartFile selfie = new MockMultipartFile("selfie", "selfie.jpg", "image/jpeg", "selfie".getBytes());

        when(rekognitionService.compareFaces(idFront, selfie)).thenReturn(true);
        when(mapper.toEntity(any(CreateRequestVerificationDto.class))).thenReturn(identityVerification);
        when(identityRepo.save(any(IdentityVerification.class))).thenReturn(identityVerification);
        when(identityRepo.findById(1L)).thenReturn(Optional.of(identityVerification));
        when(mapper.toResponse(identityVerification)).thenReturn(responseDto);
        when(cloudinaryService.upload(any(MultipartFile.class))).thenReturn(
                Map.of(
                        "url", "https://cloudinary.com/test.jpg",
                        "path", "documents/test.jpg"
                )
        );

        IdentityVerificationResponseDto result = identityVerificationService.create(1L, idFront, idBack, selfie);

        assertNotNull(result);
        assertEquals("aprobado", identityVerification.getVerificationStatus());
        assertNull(identityVerification.getRejectionReason());

        verify(identityRepo, times(1)).save(any(IdentityVerification.class));
        verify(fileRepo, times(3)).save(any());
        verify(cloudinaryService, times(3)).upload(any(MultipartFile.class));
    }

    @Test
    void create_WhenFacesDoNotMatch_ShouldSaveRejectedVerification() {

        MultipartFile idFront = new MockMultipartFile("idFront", "front.jpg", "image/jpeg", "front".getBytes());
        MultipartFile idBack = new MockMultipartFile("idBack", "back.jpg", "image/jpeg", "back".getBytes());
        MultipartFile selfie = new MockMultipartFile("selfie", "selfie.jpg", "image/jpeg", "selfie".getBytes());

        when(rekognitionService.compareFaces(idFront, selfie)).thenReturn(false);
        when(mapper.toEntity(any(CreateRequestVerificationDto.class))).thenReturn(identityVerification);
        when(identityRepo.save(any(IdentityVerification.class))).thenReturn(identityVerification);
        when(identityRepo.findById(1L)).thenReturn(Optional.of(identityVerification));
        when(mapper.toResponse(identityVerification)).thenReturn(responseDto);
        when(cloudinaryService.upload(any(MultipartFile.class))).thenReturn(
                Map.of(
                        "url", "https://cloudinary.com/test.jpg",
                        "path", "documents/test.jpg"
                )
        );

        IdentityVerificationResponseDto result = identityVerificationService.create(1L, idFront, idBack, selfie);

        assertNotNull(result);
        assertEquals("rechazado", identityVerification.getVerificationStatus());
        assertEquals("El rostro del selfie no coincide con la cédula.", identityVerification.getRejectionReason());

        verify(identityRepo, times(1)).save(any(IdentityVerification.class));
        verify(fileRepo, times(3)).save(any());
    }

    @Test
    void updateStatus_WhenVerificationExists_ShouldUpdateAndReturnResponse() {

        UpdateRequestVerificationDto dto = new UpdateRequestVerificationDto();

        when(identityRepo.findById(1L)).thenReturn(Optional.of(identityVerification));
        when(identityRepo.save(identityVerification)).thenReturn(identityVerification);
        when(mapper.toResponse(identityVerification)).thenReturn(responseDto);

        IdentityVerificationResponseDto result = identityVerificationService.updateStatus(1L, dto);

        assertNotNull(result);

        verify(mapper, times(1)).updateEntity(identityVerification, dto);
        verify(identityRepo, times(1)).save(identityVerification);
    }

    @Test
    void updateStatus_WhenVerificationDoesNotExist_ShouldThrowException() {

        UpdateRequestVerificationDto dto = new UpdateRequestVerificationDto();

        when(identityRepo.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            identityVerificationService.updateStatus(99L, dto);
        });

        assertTrue(exception.getMessage().contains("Verificación no encontrada con id=99"));
    }

    @Test
    void findByUser_ShouldReturnVerificationList() {

        when(identityRepo.findByUserId(1L)).thenReturn(List.of(identityVerification));
        when(mapper.toResponse(identityVerification)).thenReturn(responseDto);

        List<IdentityVerificationResponseDto> result = identityVerificationService.findByUser(1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(identityRepo, times(1)).findByUserId(1L);
    }

    @Test
    void findAll_ShouldReturnAllVerifications() {

        when(identityRepo.findAll()).thenReturn(List.of(identityVerification));
        when(mapper.toResponse(identityVerification)).thenReturn(responseDto);

        List<IdentityVerificationResponseDto> result = identityVerificationService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(identityRepo, times(1)).findAll();
    }
}
