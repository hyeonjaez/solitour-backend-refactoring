package solitour_backend.solitour.image.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.image.dto.mapper.ImageMapper;
import solitour_backend.solitour.image.dto.response.ImageResponse;
import solitour_backend.solitour.image.entity.Image;
import solitour_backend.solitour.image.exception.ImageNotExistsException;
import solitour_backend.solitour.image.image_status.ImageStatus;
import solitour_backend.solitour.image.repository.ImageRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    @Transactional(readOnly = true)
    public List<ImageResponse> contentInformationImage(Long postId) {
        if (!imageRepository.existsByInformationId(postId)) {
            throw new ImageNotExistsException("해당하는 정보 id의 본문 이미지가 없습니다");
        }
        List<Image> contentImageList = imageRepository.findAllByInformationIdAndImageStatus(postId, ImageStatus.CONTENT);

        return imageMapper.toImageResponseList(contentImageList);
    }

    @Transactional(readOnly = true)
    public ImageResponse thumbnailInformationImage(Long postId) {
        if (!imageRepository.existsByInformationId(postId)) {
            throw new ImageNotExistsException("해당하는 정보 id의 썸네일 이미지가 없습니다");
        }
        Image image = imageRepository.findOneByInformationIdAndImageStatus(postId, ImageStatus.THUMBNAIL);

        return imageMapper.toImageResponse(image);
    }

    @Transactional(readOnly = true)
    public ImageResponse userImage(Long userId) {
        if (!imageRepository.existsByUserId(userId)) {
            throw new ImageNotExistsException("해당하는 user id의 이미자가 없습니다");
        }
        Image userImage = imageRepository.findOneByUserId(userId);
        return imageMapper.toImageResponse(userImage);
    }


}
