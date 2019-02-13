package co.com.ceiba.parqueadero.integrales;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.HttpStatus;
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
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		int contador = 20;
		while (contador > 0) {
			entityManager.persist(new ParqueoEntity());
			contador--;
		}
		assertEquals(20, parqueoController.todosLosParqueos().size());
	}

	@Test
	public void parqueoPorId() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		long idParqueo = (long) entityManager.persistAndGetId(new ParqueoEntity());
		assertNotNull(parqueoController.parqueoPorId("" + idParqueo));
	}

	@Test
	public void parqueoPorIdNotFound() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		assertEquals(HttpStatus.NOT_FOUND, parqueoController.parqueoPorId("890").getStatusCode());
	}

	@Test
	public void nuevoParqueo() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		
		ParqueoEntity parqueoCorrecto = new ParqueoEntity(entityManager.persist(new VehiculoEntity(1000, "XBC", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00));

		assertEquals("PARQUEO CREADO CON EXITO", parqueoController.nuevoParqueo(parqueoCorrecto).getBody());

	}

	@Test
	public void nuevoParqueoVehiculoNoPuedeIngresarPorDia() {
		Parqueo parqueo=new Parqueo(parqueoRepository, vehiculoRepository);
		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-04T09:00:00.00Z"),
				ZoneId.of("America/Bogota")));
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);		
		ParqueoEntity parqueoNoPuedeIngresarDia = new ParqueoEntity(entityManager
				.persist(new VehiculoEntity(1000, "ABC", "CARRO")),
				LocalDateTime.of(2019, Month.JANUARY, 4, 10, 00, 00));
		assertEquals("EL VEHICULO NO PUEDE INGRESAR EL DIA DE HOY",
				parqueoController.nuevoParqueo(parqueoNoPuedeIngresarDia).getBody());
	}

	@Test
	public void nuevoParqueoVehiculoLimiteSuperado() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		int contador = 20;		
		while (contador > 0) {
			entityManager.persist(new VehiculoEntity(1000, "XXX", "CARRO"));			
			contador--;			
		}
		VehiculoEntity temp =entityManager.persist(new VehiculoEntity(1000, "YXZ", "CARRO"));
		ParqueoEntity parqueoCorrecto = new ParqueoEntity(temp,LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00));
		assertEquals("EL LIMITE DE: CARRO HA SIDO SUPERADO", parqueoController.nuevoParqueo(parqueoCorrecto).getBody());
	}	

	@Test
	public void salidaDeParqueoExitosa() {		
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);		
		ParqueoEntity parqueoEntity= entityManager
				.persist(new ParqueoEntity(entityManager.persist(new VehiculoEntity(1000, "XBC", "CARRO")), LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00)));
		parqueo.setClock(Clock.fixed(Instant.parse("2019-01-01T10:30:00.00Z"),
				ZoneId.of("America/Bogota")));
		parqueoController.salidaDeParqueo(parqueoEntity);
		assertEquals("SALIDA CREADA CON EXITO", parqueoController.salidaDeParqueo(parqueoEntity).getBody());

	}

	@Test
	public void salidaDeParqueoNoExiste() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		assertEquals("EL PARQUEO INGRESADO NO EXISTE", parqueoController.salidaDeParqueo(null).getBody());
	}

	@Test
	public void salidaDeParqueoVehiculoNoExiste() {
		ParqueoController parqueoController = new ParqueoController(parqueoRepository, vehiculoRepository);
		ParqueoEntity parqueoEntity= entityManager
				.persist(new ParqueoEntity(new VehiculoEntity(1000, "XBC", "CARRO"), LocalDateTime.of(2019, Month.JANUARY, 1, 10, 00, 00)));
		VehiculoEntity vehiculoErroneo=new VehiculoEntity(1000, "PPP", "CARRO");
		parqueoEntity.setVehiculoEntity(vehiculoErroneo);
		assertEquals("EL VEHICULO DEL PARQUEO NO SE PUDO ENCONTRAR",
				parqueoController.salidaDeParqueo(parqueoEntity).getBody());
	}

}
