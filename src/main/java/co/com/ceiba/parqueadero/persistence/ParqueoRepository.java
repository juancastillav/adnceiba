package co.com.ceiba.parqueadero.persistence;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ParqueoRepository extends JpaRepository<ParqueoEntity, Long>{

	ParqueoEntity findByVehiculoEntity(VehiculoEntity vehiculoEntity);
	

}
