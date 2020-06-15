package com.pranav.space_management_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pranav.space_management_system.entity.BuildingEntity;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {
	
	Optional<BuildingEntity> getBuildingByBuildingName(String buildingName);

}
