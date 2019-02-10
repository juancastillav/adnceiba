package co.com.ceiba.parqueadero;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import co.com.ceiba.parqueadero.persistence.ParqueoEntity;
import co.com.ceiba.parqueadero.persistence.ParqueoRepository;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;


@SpringBootApplication
public class ParqueaderoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParqueaderoApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(VehiculoRepository vehiculoRepository, ParqueoRepository parqueoRepository) {
		return (args) -> {			
			vehiculoRepository.save(new VehiculoEntity(500, "ABC", "CARRO"));
			vehiculoRepository.save(new VehiculoEntity(1500, "SDF", "CARRO"));
			vehiculoRepository.save(new VehiculoEntity(2500, "ERT", "MOTO"));
			vehiculoRepository.save(new VehiculoEntity(3500, "YJH", "CARRO"));
			vehiculoRepository.save(new VehiculoEntity(4500, "KLO", "MOTO"));			
			Calendar calendar = new GregorianCalendar(2019,1,5,6,24,00); 
			System.out.println("FECHAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "+calendar.getTime());
			parqueoRepository.save(new ParqueoEntity(Long.valueOf(1),calendar));
		};
	}
	
	

}

