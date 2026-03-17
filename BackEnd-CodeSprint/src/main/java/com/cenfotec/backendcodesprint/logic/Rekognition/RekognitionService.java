package com.cenfotec.backendcodesprint.logic.Rekognition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RekognitionService {

    private final RekognitionClient rekognitionClient;
    @Value("${aws.rekognition.similarity-threshold}")
    private float similarityThreshold;

    public boolean compareFaces(MultipartFile idFront, MultipartFile selfie) {

        try {
            Image sourceImage = toRekognitionImage(idFront);
            Image targetImage = toRekognitionImage(selfie);
            CompareFacesRequest request = CompareFacesRequest.builder()
                    .sourceImage(sourceImage)
                    .targetImage(targetImage)
                    .similarityThreshold(similarityThreshold)
                    .build();

            CompareFacesResponse response = rekognitionClient.compareFaces(request);
            return response.faceMatches()
                    .stream()
                    .anyMatch(match -> match.similarity() >= similarityThreshold);
        } catch (InvalidParameterException e) {
            throw new RuntimeException("No se detectó un rostro válido en las imágenes.", e);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer los archivos de imagen: " + e.getMessage(), e);
        }

    }
    private Image toRekognitionImage(MultipartFile file) throws IOException {
        return Image.builder()
                .bytes(SdkBytes.fromByteArray(file.getBytes()))
                .build();
    }

}
