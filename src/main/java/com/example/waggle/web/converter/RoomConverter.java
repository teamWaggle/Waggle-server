package com.example.waggle.web.converter;

import com.example.waggle.domain.chat.entity.Room;
import com.example.waggle.web.dto.chat.RoomResponse;
import java.util.List;
import java.util.stream.Collectors;

public class RoomConverter {

    public static RoomResponse.RoomListDto mapRoomListToRoomListDto(List<Room> roomList) {
        List<RoomResponse.RoomDto> roomDtoList = roomList.stream()
                .map(RoomConverter::mapRoomToRoomDto)
                .collect(Collectors.toList());

        return RoomResponse.RoomListDto.builder()
                .rooms(roomDtoList)
                .build();
    }

    public static RoomResponse.RoomDto mapRoomToRoomDto(Room room) {
        return RoomResponse.RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .build();
    }

}
