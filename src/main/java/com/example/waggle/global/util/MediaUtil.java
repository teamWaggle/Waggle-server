package com.example.waggle.global.util;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.pet.persistence.entity.Pet;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import com.example.waggle.global.service.aws.AwsS3Service;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class MediaUtil {

    private static final String SERVER_URI = "https://waggle-dev-s3.s3.ap-northeast-2.amazonaws.com/";
    private static final String DEFAULT_MEMBER_PROFILE_IMG = "1d99fc66-0d87-49c7-b173-3093019e46c0.png";
    private static final String DEFAULT_PET_PROFILE_IMG = "ee8020e7-86e6-435a-a12f-c02826b8d0fd.png";

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

    public static String getDefaultMemberProfileImgUrl() {
        return appendUri(DEFAULT_MEMBER_PROFILE_IMG);
    }
}
