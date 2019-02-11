package co.com.ceiba.parqueadero.unitarias;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import co.com.ceiba.parqueadero.model.Parqueo;
import co.com.ceiba.parqueadero.persistence.ParqueoEntity;
import co.com.ceiba.parqueadero.persistence.ParqueoRepository;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;
@RunWith(SpringRunner.class)
@DataJpaTest
public class ParqueoTest {
	
	@Autowired
	private ParqueoRepository parqueoRepository;
	@Autowired
	private VehiculoRepository vehiculoRepository;	
	@Autowired
	private TestEntityManager entityManager;	
	@BeforeEach
	public void init() {
		vehiculoRepository.deleteAll();
		parqueoRepository.deleteAll();
	}	
	
	@Test	
	public void nuevoParqueoExitoso() {		
		//given
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);		
		long idGenerado=(long) entityManager.persistAndGetId(new VehiculoEntity(1000, "XBC", "CARRO"));		
		//when
		ParqueoEntity parqueoEntity=new ParqueoEntity(idGenerado,new GregorianCalendar(2019,1,5,6,24,00));
		//then
		assertEquals("OK", parqueo.nuevoParqueo(parqueoEntity));
	}
	
	@Test	
	public void nuevoParqueoVehiculoNoExiste() {		
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		ParqueoEntity parqueoEntity=new ParqueoEntity(0,new GregorianCalendar(2019,1,5,6,24,00));	
		assertEquals("EL VEHICULO NO EXISTE", parqueo.nuevoParqueo(parqueoEntity));
	}
	@Test	
	public void nuevoParqueoVehiculoNoPuedeIngresarPorPlacaYDia() {
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);			
		long idGenerado=(long) entityManager.persistAndGetId(new VehiculoEntity(1000, "ABC", "CARRO"));	
		ParqueoEntity parqueoEntity=new ParqueoEntity(idGenerado,new GregorianCalendar(2019,1,5,6,24,00));				
		assertEquals("EL VEHICULO NO PUEDE INGRESAR EL DIA DE HOY", parqueo.nuevoParqueo(parqueoEntity));
	}
	@Test	
	public void nuevoParqueoVehiculoSuperaCantidadMaximaVehiculos() {
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		long idGenerado=(long) entityManager.persistAndGetId(new VehiculoEntity(1000, "XXX", "CARRO"));	
		ParqueoEntity parqueoEntity=new ParqueoEntity(idGenerado,new GregorianCalendar(2019,1,5,6,24,00));	
		int contador =20;
		while(contador>0) {
			entityManager.persist(new VehiculoEntity(1000, "GGG", "CARRO"));	
			contador--;
		}		
			
		assertEquals("EL LIMITE DE: CARRO HA SIDO SUPERADO", parqueo.nuevoParqueo(parqueoEntity));
	}
	@Test
	public void salidaDeParqueo() {		
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		long idGeneradoVehiculo=(long) entityManager.persistAndGetId(new VehiculoEntity(1000, "ABC", "CARRO"));	
		long idGeneradoParqueo=(long) entityManager.persistAndGetId(new ParqueoEntity(idGeneradoVehiculo,new GregorianCalendar(2019,1,5,6,24,00)));				
		assertEquals("OK", parqueo.salidaDeParqueo(idGeneradoParqueo));
		
	}	
	@Test
	public void calcularValorAPagar() {
		// arrange
		Parqueo parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
		long idGeneradoVehiculoPorHoras=(long) entityManager.persistAndGetId(new VehiculoEntity(1000, "ABC", "CARRO"));	
		long idGeneradoVehiculoPorDia=(long) entityManager.persistAndGetId(new VehiculoEntity(1000, "EFG", "CARRO"));	
		long idGeneradoVehiculoMasDelDia=(long) entityManager.persistAndGetId(new VehiculoEntity(1000, "EFG", "CARRO"));	
		long idGeneradoMotoBajoCilindraje=(long) entityManager.persistAndGetId(new VehiculoEntity(250, "EFG", "MOTO"));
		long idGeneradoMotoAltoCilindraje=(long) entityManager.persistAndGetId(new VehiculoEntity(1000, "EFG", "MOTO"));
		
		ParqueoEntity parqueoEntityVehiculoPorHoras=new ParqueoEntity(idGeneradoVehiculoPorHoras,new GregorianCalendar(2019,1,5,6,00,00));	
		ParqueoEntity parqueoEntityVehiculoPorDia=new ParqueoEntity(idGeneradoVehiculoPorDia,new GregorianCalendar(2019,1,5,6,00,00));	
		ParqueoEntity parqueoEntityVehiculoMasDelDia=new ParqueoEntity(idGeneradoVehiculoMasDelDia,new GregorianCalendar(2019,1,5,6,00,00));	
		ParqueoEntity parqueoEntityMotoBajoCilindraje=new ParqueoEntity(idGeneradoMotoBajoCilindraje,new GregorianCalendar(2019,1,5,6,00,00));	
		ParqueoEntity parqueoEntityMotoAltoCilindraje=new ParqueoEntity(idGeneradoMotoAltoCilindraje,new GregorianCalendar(2019,1,5,6,00,00));	
		
		// assert
		parqueo.setCalendar(new GregorianCalendar(2019,1,5,9,00,00));
		assertEquals(3000, parqueo.calcularValorAPagar(parqueoEntityVehiculoPorHoras));
		parqueo.setCalendar(new GregorianCalendar(2019,1,5,16,00,00));
		assertEquals(8000, parqueo.calcularValorAPagar(parqueoEntityVehiculoPorDia));
		parqueo.setCalendar(new GregorianCalendar(2019,1,6,8,00,00));
		assertEquals(10000, parqueo.calcularValorAPagar(parqueoEntityVehiculoMasDelDia));
		parqueo.setCalendar(new GregorianCalendar(2019,1,5,9,00,00));
		assertEquals(1500, parqueo.calcularValorAPagar(parqueoEntityMotoBajoCilindraje));
		parqueo.setCalendar(new GregorianCalendar(2019,1,5,9,00,00));
		assertEquals(3500, parqueo.calcularValorAPagar(parqueoEntityMotoAltoCilindraje));
		
	}

}
