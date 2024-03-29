package com.example.waggle.global.util;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.schedule.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MediaUtil {

    private static String SERVER_URI = "https://waggle-bucket.s3.ap-northeast-2.amazonaws.com/";

    public static String appendUri(String s3URI) {
        StringBuffer stringBuffer = new StringBuffer(SERVER_URI);
        return stringBuffer.append(s3URI).toString();
    }

    public static String getProfileImg(Member member) {
        if (member.getProfileImgUrl() == null) {
            return null;
        }
        return appendUri(member.getProfileImgUrl());
    }

    public static String getProfileImg(Pet pet) {
        if (pet.getProfileImgUrl() == null) {
            return null;
        }
        return appendUri(pet.getProfileImgUrl());
    }

    public static String getCoverImg(Team team) {
        if (team.getCoverImageUrl() == null) {
            return null;
        }
        return appendUri(team.getCoverImageUrl());
    }

    public static String getThumbnail(Board board) {
        if (board.getMedias().isEmpty()) {
            return null;
        }
        return appendUri(board.getMedias().get(0).getUploadFile());
    }

    public static String saveProfileImg(MultipartFile file, AwsS3Service awsS3Service) {
        return ObjectUtil.isPresent(file) ? awsS3Service.uploadFile(file) : null;
    }

    public static List<String> getBoardMedias(Board board) {
        if (board.getMedias().isEmpty()) {
            return null;
        }
        return board.getMedias().stream()
                .map(media -> appendUri(media.getUploadFile())).collect(Collectors.toList());
    }

    public static String removePrefix(String url) {
        String subPrefixUrl = null;
        if (url != null) {
            if (url.startsWith(SERVER_URI)) {
                subPrefixUrl = url.substring(SERVER_URI.length());
            }
        }
        return subPrefixUrl;
    }
}
