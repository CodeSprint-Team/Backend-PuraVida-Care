package com.cenfotec.backendcodesprint.logic.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map<String, String> upload(MultipartFile file) {
        try {
            Map<?,?> result= cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "verificaciones")
            );
            return Map.of(
                    "url", result.get("secure_url").toString(),
                    "path", result.get("public_id").toString()
            );
        }
        catch (Exception e) {
            throw new RuntimeException("Error en Cloudinary upload" + e.getMessage(),e);
        }
    }

    public void Delete(String path){
        try {
            cloudinary.uploader().destroy(path, ObjectUtils.emptyMap());
        }
        catch (Exception e) {
            throw new RuntimeException("Error en Cloudinary Delete " + e.getMessage(),e);
        }
    }
}
