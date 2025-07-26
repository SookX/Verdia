package com.anastassow.server.service.serviceImpl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anastassow.server.models.Plants;
import com.anastassow.server.repository.PlantsRepository;
import com.anastassow.server.service.PlantsService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class PlantsServiceImpl implements PlantsService{
    
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PlantsRepository plantsRepo;

    @Override
    public Plants uploadPlant(MultipartFile file) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "spring_uploads")
            );

            String url = (String) uploadResult.get("secure_url");

            Plants image = Plants.builder()
                    .imageUrl(url)
                    .build();

            return plantsRepo.save(image);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}
