package co.com.ceiba.parqueadero.persistence;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParqueoRepository extends JpaRepository<ParqueoEntity, Long>{

	ParqueoEntity findByVehiculoEntity(VehiculoEntity vehiculoEntity);
	ParqueoEntity findByVehiculoEntityAndFechaHoraDeSalidaIsNull(VehiculoEntity vehiculoEntity);
	List<ParqueoEntity> findByfechaHoraDeSalidaIsNull();
	List<ParqueoEntity> findByfechaHoraDeSalidaIsNotNull();
	

}
