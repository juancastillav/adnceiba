package co.com.ceiba.parqueadero.integrales;

import static org.junit.Assert.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import co.com.ceiba.parqueadero.controller.ParqueoController;
import co.com.ceiba.parqueadero.model.Parqueo;
import co.com.ceiba.parqueadero.persistence.ParqueoEntity;
import co.com.ceiba.parqueadero.persistence.ParqueoRepository;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParqueoControllerTest {

	@Autowired
	private ParqueoRepository parqueoRepository;
	@Autowired
	private VehiculoRepository vehiculoRepository;
	@Autowired
	private TestEntityManager entityManager;
	@BeforeEach
	public void setup(){
		parqueoRepository.deleteAll();
		vehiculoRepository.deleteAll();
		}

	@Test
	public void todosLosParqueos() {
		//given
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		int contador = 20;
		//when
		while (contador > 0) {
			entityManager.persist(new ParqueoEntity());
			contador--;
		}
		//then
		assertEquals(20, parqueoController.todosLosParqueos().size());
	}	

	@Test
	public void nuevoParqueo() {
		//given
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		//when
		ParqueoEntity parqueoCorrecto = new ParqueoEntity(entityManager.persist(new VehiculoEntity(1000, "XBC", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00));
		//then
		assertEquals("{\"message\":\"PARQUEO CREADO CON EXITO\"}", parqueoController.nuevoParqueo(parqueoCorrecto).getBody());

	}

	@Test
	public void nuevoParqueoVehiculoNoPuedeIngresarPorDia() {
		//given
		Parqueo parqueo=new Parqueo(parqueoRepository, vehiculoRepository);
		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-04T09:00:00.00Z"),
				ZoneId.of("America/Bogota")));
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);		
		//when
		ParqueoEntity parqueoNoPuedeIngresarDia = new ParqueoEntity(entityManager
				.persist(new VehiculoEntity(1000, "ABC", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 4, 10, 00, 00));
		//then
		assertEquals("{\"message\":\"EL VEHICULO NO PUEDE INGRESAR EL DIA DE HOY\"}",
				parqueoController.nuevoParqueo(parqueoNoPuedeIngresarDia).getBody());
	}

	@Test
	public void nuevoParqueoVehiculoLimiteSuperado() {
		//given
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		int contador = 20;	
		//when
		while (contador > 0) {
			entityManager.persist(new VehiculoEntity(1000, "XXX", "CARRO"));			
			contador--;			
		}
		VehiculoEntity temp =entityManager.persist(new VehiculoEntity(1000, "YXZ", "CARRO"));
		ParqueoEntity parqueoCorrecto = new ParqueoEntity(temp,LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00));
		//then
		assertEquals("{\"message\":\"EL LIMITE DE: CARRO HA SIDO SUPERADO\"}", parqueoController.nuevoParqueo(parqueoCorrecto).getBody());
	}	

	@Test
	public void salidaDeParqueoExitosa() {		
		//given
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);	
		//when
		ParqueoEntity parqueoEntity= entityManager
				.persist(new ParqueoEntity(entityManager.persist(new VehiculoEntity(1000, "XBC", "CARRO")), LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00)));
		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-01T10:30:00.00Z"),
				ZoneId.of("America/Bogota")));
		parqueoController.salidaDeParqueo(parqueoEntity);
		//then
		assertEquals("{\"message\":\"SALIDA CREADA CON EXITO\"}", parqueoController.salidaDeParqueo(parqueoEntity).getBody());

	}

	@Test
	public void salidaDeParqueoNoExiste() {
		//given
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		//then
		assertEquals("{\"message\":\"EL PARQUEO INGRESADO NO EXISTE\"}", parqueoController.salidaDeParqueo(null).getBody());
	}

	@Test
	public void salidaDeParqueoVehiculoNoExiste() {
		//given
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		//when
		ParqueoEntity parqueoEntity= entityManager
				.persist(new ParqueoEntity(new VehiculoEntity(1000, "XBC", "CARRO"), LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00)));
		VehiculoEntity vehiculoErroneo=new VehiculoEntity(1000, "PPP", "CARRO");
		parqueoEntity.setVehiculoEntity(vehiculoErroneo);
		//then
		assertEquals("{\"message\":\"EL VEHICULO DEL PARQUEO NO SE PUDO ENCONTRAR\"}",
				parqueoController.salidaDeParqueo(parqueoEntity).getBody());
	}
	
	@Test
	public void parqueoPorPlacaVehiculoEncontrador() {
		//given
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		//when
		entityManager.persist(new ParqueoEntity(new VehiculoEntity(1000, "XBC", "CARRO"), LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00)));
		//then
		List<ParqueoEntity> respuesta=parqueoController.parqueoPorPlacaVehiculo("XBC");
		assertEquals(1,respuesta.size());
	}
	
	@Test
	public void parqueoPorPlacaVehiculoNoEncontrador() {
		//given
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		//when
		entityManager.persist(new ParqueoEntity(new VehiculoEntity(1000, "XBC", "CARRO"), LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00)));
		List<ParqueoEntity> respuesta=parqueoController.parqueoPorPlacaVehiculo("HHH");
		//then
		assertEquals(0,respuesta.size());
	}

}
