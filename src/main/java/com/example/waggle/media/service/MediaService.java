package com.example.waggle.media.service;

import com.example.waggle.commons.component.file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    List<UploadFile> createMedias(Long boardId, List<MultipartFile> multipartFiles, String boardType) throws IOException;
}
