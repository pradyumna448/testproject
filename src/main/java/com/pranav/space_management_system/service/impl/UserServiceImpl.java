package com.pranav.space_management_system.service.impl;

import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.pranav.space_management_system.dto.UserDto;
import com.pranav.space_management_system.entity.RoomEntity;
import com.pranav.space_management_system.entity.UserEntity;
import com.pranav.space_management_system.exceptions.ServiceException;
import com.pranav.space_management_system.exceptions.util.ErrorConstants;
import com.pranav.space_management_system.repository.RoomRepository;
import com.pranav.space_management_system.repository.UserRepository;
import com.pranav.space_management_system.service.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ModelMapper modelMapper;

	ExecutorService executorService = Executors.newSingleThreadExecutor();

	@Bean
	public ModelMapper ModelMapper() {
		return new ModelMapper();
	}

	@Override
	public UserDto addUser(UserDto user) throws ServiceException {
		Optional<RoomEntity> roomEntity;
		roomEntity = roomRepository.findById(user.getRoom().getRoomId());
		roomEntity.orElseThrow(() -> new ServiceException(ErrorConstants.NOSUCHROOM));
		UserEntity userEntity = convertDtoToEntity(user);
		userEntity.setRoom(roomEntity.get());
		return convertEntityToDto(userRepository.save(userEntity));
	}

	@Override
	public List<UserDto> getAllUser() throws ServiceException {
		List<UserEntity> userEntities = null;
		try {
			userEntities = userRepository.findAll();
		} catch (DataAccessException e) {
			throw new ServiceException(ErrorConstants.NOSUCHUSER);
		}
		return userEntities.stream().map(userEntity -> convertEntityToDto(userEntity)).collect(Collectors.toList());
	}

	@Override
	public UserDto getUserById(Long id) throws ServiceException {
		Optional<UserEntity> userEntity = userRepository.findById(id);
		userEntity.orElseThrow(() -> new ServiceException(ErrorConstants.NOSUCHUSER));
		return convertEntityToDto(userEntity.get());
	}

	@Override
	public UserDto updateUserById(Long id, UserDto userDto) {
		Optional<UserEntity> userEntityOptional = userRepository.findById(id);
		userEntityOptional.orElseThrow(() -> new NoSuchElementException(ErrorConstants.NOSUCHUSER));
		Optional<RoomEntity> roomEntityOptional = roomRepository.findById(userDto.getRoom().getRoomId());
		roomEntityOptional.orElseThrow(() -> new NoSuchElementException(ErrorConstants.NOSUCHROOM));
		userEntityOptional.get().setRoom(roomEntityOptional.get());
		UserEntity savedUserEntity = userRepository.save(userEntityOptional.get());
		return convertEntityToDto(savedUserEntity);
	}

	private UserDto convertEntityToDto(UserEntity userEntity) {
		return modelMapper.map(userEntity, UserDto.class);
	}

	private UserEntity convertDtoToEntity(UserDto userDto) {
		return modelMapper.map(userDto, UserEntity.class);
	}

	public void testMethod() {
		try {
			Future future = executorService.submit(() -> {
				return "dummy string";
			});
			System.err.println("Inside the test method : " + future.get().toString());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void testSHA() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void testMap() {
		int s[] = { 1, 2, 3, 4, 5 };
		Map<Object, Object> map = Arrays.asList(s).parallelStream().collect(Collectors.toMap(x -> x, x -> x));
		for (Object num : map.keySet()) {
			map.get((int) num);
		}
	}

	public void testIdentical() {
		int x = 0;
		if (Math.random() > 0.5) {
			x = 15;
		} else {
			x = 15;
		}
		System.out.println(x);
	}
	
	public void testKeyPairGenerator() {
        KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	        keyPairGenerator.initialize(1024);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	 public void testPasswordHardcode() {
	        Map<String, Object> remote = new HashMap<>();
	        remote.put("host", "https://example.com:9200");
	        remote.put("username", "testuser");
	        remote.put("password", "testpass");
	 }
}
