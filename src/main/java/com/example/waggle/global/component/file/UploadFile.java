package com.example.waggle.global.component.file;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class UploadFile {
    private String originalFilename;
    private String storeFileName;

    public UploadFile(String originalFilename, String storeFileName) {
        this.originalFilename = originalFilename;
        this.storeFileName = storeFileName;
    }
}
