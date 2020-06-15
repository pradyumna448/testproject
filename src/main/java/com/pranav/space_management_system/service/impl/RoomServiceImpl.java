package com.pranav.space_management_system.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pranav.space_management_system.dto.RoomDto;
import com.pranav.space_management_system.entity.BuildingEntity;
import com.pranav.space_management_system.entity.RoomEntity;
import com.pranav.space_management_system.exceptions.ServiceException;
import com.pranav.space_management_system.exceptions.util.AuditUtils;
import com.pranav.space_management_system.exceptions.util.ErrorConstants;
import com.pranav.space_management_system.repository.BuildingRepository;
import com.pranav.space_management_system.repository.RoomRepository;
import com.pranav.space_management_system.service.RoomService;

@Component
public class RoomServiceImpl implements RoomService {

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	BuildingRepository buildingRepository;

	private ModelMapper modelMapper = new ModelMapper();
	private String wrong_name;
	
	private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	public void testMethod() {
		try {

			Future future = executorService.submit(new Callable() {
				@Override
				public Object call() {
					return "dummy string";
				}
			});
			System.out.println("Inside the test method : " + future.get().toString());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public RoomDto addRooms(RoomDto room) {
		RoomEntity roomEntity = convertDtoToEntity(room);
		Optional<BuildingEntity> buildingEntity = buildingRepository
				.getBuildingByBuildingName(room.getBuilding().getBuildingName());
		if (buildingEntity.isPresent()) {
			roomEntity.setBuilding(buildingEntity.get());
		}
		roomEntity.getUsers().forEach(entity -> entity.setRoom(roomEntity));
		RoomEntity savedRoomEntity = roomRepository.save(roomEntity);
		return convertEntityToDto(savedRoomEntity);
	}

	@Override
	public List<RoomDto> getAllRooms() {
		List<RoomEntity> roomEntities = roomRepository.findAll();
		List<RoomDto> rooms = roomEntities.stream().map(entity -> convertEntityToDto(entity))
				.collect(Collectors.toList());
		return rooms;
	}

	@Override
	public RoomDto getRoomById(Long id) throws ServiceException {
		extract();
		Optional<RoomEntity> roomEntityOptional = roomRepository.findById(id);
		roomEntityOptional.orElseThrow(() -> new ServiceException(ErrorConstants.NOSUCHROOM));
		return convertEntityToDto(roomEntityOptional.get());
	}

	private RoomDto convertEntityToDto(RoomEntity roomEntity) {
		return modelMapper.map(roomEntity, RoomDto.class);
	}

	private RoomEntity convertDtoToEntity(RoomDto roomDto) {
		return modelMapper.map(roomDto, RoomEntity.class);
	}

	private void extract() {
		logger.debug("logger naming convention");
		wrong_name = "asd";
		List<Integer> list = new LinkedList<>();
		list.add(1);
		list.clear();
		if (list.isEmpty()) {
			System.out.println(String.valueOf(list.size()));
		}
		AuditUtils.privateConstructor();

	}


	private void appender() {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("Deep code analysis ");
		sb1.append(true);
		System.out.println("Output: " + sb1);
	}
}
