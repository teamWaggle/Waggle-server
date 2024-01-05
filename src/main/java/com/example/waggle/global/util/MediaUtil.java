package com.example.waggle.global.util;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MediaUtil {

    private static String SERVER_URI = "https://waggle-bucket.s3.ap-northeast-2.amazonaws.com";

    public static String appendUri(String s3URI) {
        StringBuffer stringBuffer = new StringBuffer(SERVER_URI);
        return stringBuffer.append("/").append(s3URI).toString();
    }

    public static String getProfile(Member member) {
        if (member.getProfileImgUrl() == null) {
            return null;
        }
        return appendUri(member.getProfileImgUrl());
    }

    public static String getThumbnail(Board board) {
        if (board.getMedias().isEmpty()) {
            return null;
        }
        return appendUri(board.getMedias().get(0).getUploadFile());
    }

    public static String saveProfileImg(MultipartFile file, AwsS3Service awsS3Service) {
        String url = null;
        if (!file.isEmpty()) {
            String profileImg = awsS3Service.uploadFile(file);
            StringBuffer stringBuffer = new StringBuffer(SERVER_URI);
            StringBuffer save = stringBuffer.append("/").append(profileImg);
            url = save.toString();
        }
        return url;
    }

    public static List<String> getBoardMedias(Board board) {
        if (board.getMedias().isEmpty()) {
            return null;
        }
        return board.getMedias().stream()
                .map(media -> appendUri(media.getUploadFile())).collect(Collectors.toList());
    }

}
