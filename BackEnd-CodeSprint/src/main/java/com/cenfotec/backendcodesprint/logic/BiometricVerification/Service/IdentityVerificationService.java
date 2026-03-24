package com.cenfotec.backendcodesprint.logic.BiometricVerification.Service;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Request.CreateRequestVerificationDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Request.UpdateRequestVerificationDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Response.IdentityVerificationResponseDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Mapper.IdentityVerificationMapper;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Repository.FileVerificationRepository;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Repository.IdentityVerificationRepository;
import com.cenfotec.backendcodesprint.logic.Cloudinary.CloudinaryService;
import com.cenfotec.backendcodesprint.logic.Model.FileVerification;
import com.cenfotec.backendcodesprint.logic.Model.IdentityVerification;
import com.cenfotec.backendcodesprint.logic.Rekognition.RekognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IdentityVerificationService {

    private final IdentityVerificationRepository identityRepo;
    private final FileVerificationRepository fileRepo;
    private final IdentityVerificationMapper mapper;
    private final CloudinaryService cloudinaryService;
    private final RekognitionService rekognitionService;

    @Transactional
    public IdentityVerificationResponseDto create(Long userId,
                                                  MultipartFile idFront,
                                                  MultipartFile idBack,
                                                  MultipartFile selfie) {

        // 1. Comparar rostro cédula vs selfie con Rekognition
        boolean facesMatch = rekognitionService.compareFaces(idFront, selfie);

        // 2. Crear registro con estado según resultado
        CreateRequestVerificationDto createDto = new CreateRequestVerificationDto(userId);
        IdentityVerification entity = mapper.toEntity(createDto);
        entity.setVerificationStatus(facesMatch ? "aprobado" : "rechazado");

        if (!facesMatch) {
            entity.setRejectionReason("El rostro del selfie no coincide con la cédula.");
        }

        entity = identityRepo.save(entity);

        // 3. Subir archivos a Cloudinary independientemente del resultado
        saveFile(entity, idFront, "cedula_front");
        saveFile(entity, idBack,  "cedula_back");
        saveFile(entity, selfie,  "selfie");

        // 4. Retornar respuesta completa
        return mapper.toResponse(identityRepo.findById(entity.getId()).orElseThrow());
    }

    // PATCH: aprobar o rechazar manualmente
    @Transactional
    public IdentityVerificationResponseDto updateStatus(Long id, UpdateRequestVerificationDto dto) {
        IdentityVerification entity = identityRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Verificación no encontrada con id=" + id));

        mapper.updateEntity(entity, dto);
        return mapper.toResponse(identityRepo.save(entity));
    }

    // ── GET: consultar por usuario
    public List<IdentityVerificationResponseDto> findByUser(Long userId) {
        return identityRepo.findByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ── GET: listar todas
    public List<IdentityVerificationResponseDto> findAll() {
        return identityRepo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // ── helper interno
    private void saveFile(IdentityVerification verification,
                          MultipartFile file,
                          String fileType) {

        Map<String, String> uploaded = cloudinaryService.upload(file);

        FileVerification fileEntity = new FileVerification();
        fileEntity.setIdentityVerification(verification);
        fileEntity.setFileType(fileType);
        fileEntity.setUrl(uploaded.get("url"));
        fileEntity.setPath(uploaded.get("path"));

        fileRepo.save(fileEntity);
    }
}
