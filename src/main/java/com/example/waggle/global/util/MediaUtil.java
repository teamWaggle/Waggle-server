package com.example.waggle.global.util;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.pet.persistence.entity.Pet;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import com.example.waggle.global.service.aws.AwsS3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MediaUtil {

    private static String SERVER_URI;

    private static String DEFAULT_MEMBER_PROFILE_IMG;

    private static String DEFAULT_PET_PROFILE_IMG;

    @Value("${app.server.uri}")
    public void setServerUri(String SERVER_URI) {
        this.SERVER_URI = SERVER_URI + "/";
    }

    @Value("${app.profile.member}")
    public void setDefaultMemberProfileImg(String DEFAULT_MEMBER_PROFILE_IMG) {
        this.DEFAULT_MEMBER_PROFILE_IMG = DEFAULT_MEMBER_PROFILE_IMG;
    }

    @Value("${app.profile.pet}")
    public void setDefaultPetProfileImg(String DEFAULT_PET_PROFILE_IMG) {
        this.DEFAULT_PET_PROFILE_IMG = DEFAULT_PET_PROFILE_IMG;
    }

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
