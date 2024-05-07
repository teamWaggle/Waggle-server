package com.example.waggle.global.util;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.global.service.aws.AwsS3Service;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.pet.persistence.entity.Pet;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MediaUtil {

    private static final String SERVER_URI = "https://waggle-bucket.s3.ap-northeast-2.amazonaws.com/";
    private static final String DEFAULT_MEMBER_PROFILE_IMG = "6d40e0b9-e675-45ce-ad84-c2032bfbb185.png";
    private static final String DEFAULT_PET_PROFILE_IMG = "1c463c40-a8df-405b-bbda-7b8c3763d886.png";

    public static String appendUri(String s3URI) {
        StringBuffer stringBuffer = new StringBuffer(SERVER_URI);
        return stringBuffer.append(s3URI).toString();
    }

    public static String getProfileImg(Member member) {
        if (member.getProfileImgUrl() == null) {
            return appendUri(DEFAULT_MEMBER_PROFILE_IMG);
        }
        return appendUri(member.getProfileImgUrl());
    }

    public static String getProfileImg(Pet pet) {
        if (pet.getProfileImgUrl() == null) {
            return appendUri(DEFAULT_PET_PROFILE_IMG);
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
