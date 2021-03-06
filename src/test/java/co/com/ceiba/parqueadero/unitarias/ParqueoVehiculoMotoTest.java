package co.com.ceiba.parqueadero.unitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import co.com.ceiba.parqueadero.model.ParqueoVehiculoMoto;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;

public class ParqueoVehiculoMotoTest {

	@Test
	void calcularValorAPagarBajoCilindraje() {
		// given
		ParqueoVehiculoMoto parqueoVehiculoMotoHoras = new ParqueoVehiculoMoto();
		ParqueoVehiculoMoto parqueoVehiculoMotoDia = new ParqueoVehiculoMoto();
		ParqueoVehiculoMoto parqueoVehiculoMotoMasDelDia = new ParqueoVehiculoMoto();
		VehiculoEntity vehiculoEntity = new VehiculoEntity(300, "ABC", "MOTO");
		// then
		assertEquals(1500, parqueoVehiculoMotoHoras.calcularValorAPagar(3, vehiculoEntity));
		assertEquals(4000, parqueoVehiculoMotoDia.calcularValorAPagar(10, vehiculoEntity));
		assertEquals(5500, parqueoVehiculoMotoMasDelDia.calcularValorAPagar(27, vehiculoEntity));
	}
	
	@Test
	void calcularValorAPagarAltoCilindraje() {
		// given
		ParqueoVehiculoMoto parqueoVehiculoMotoHoras = new ParqueoVehiculoMoto();
		ParqueoVehiculoMoto parqueoVehiculoMotoDia = new ParqueoVehiculoMoto();
		ParqueoVehiculoMoto parqueoVehiculoMotoMasDelDia = new ParqueoVehiculoMoto();
		VehiculoEntity vehiculoEntity = new VehiculoEntity(800, "ABC", "MOTO");
		// then
		assertEquals(3500, parqueoVehiculoMotoHoras.calcularValorAPagar(3, vehiculoEntity));
		assertEquals(6000, parqueoVehiculoMotoDia.calcularValorAPagar(10, vehiculoEntity));
		assertEquals(7500, parqueoVehiculoMotoMasDelDia.calcularValorAPagar(27, vehiculoEntity));
	}

}
