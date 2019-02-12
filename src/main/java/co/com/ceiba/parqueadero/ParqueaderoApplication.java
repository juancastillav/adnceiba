package co.com.ceiba.parqueadero;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
	private static final  String CARRO="CARRO";
	private static final  String MOTO="MOTO";
	@Bean
	public CommandLineRunner demo(VehiculoRepository vehiculoRepository, ParqueoRepository parqueoRepository) {
		return args -> {			
			VehiculoEntity test=new VehiculoEntity(500, "ABC", CARRO);
			vehiculoRepository.save(test);
			vehiculoRepository.save(new VehiculoEntity(1500, "SDF", CARRO));
			vehiculoRepository.save(new VehiculoEntity(2500, "ERT", MOTO));
			vehiculoRepository.save(new VehiculoEntity(3500, "YJH", CARRO));
			vehiculoRepository.save(new VehiculoEntity(4500, "KLO", MOTO));			
			Calendar calendar = new GregorianCalendar(2019,1,5,6,24,0); 			
			parqueoRepository.save(new ParqueoEntity(Long.valueOf(1),calendar));
			parqueoRepository.save(new ParqueoEntity(Long.valueOf(2),calendar));
			parqueoRepository.save(new ParqueoEntity(Long.valueOf(3),calendar));
			parqueoRepository.save(new ParqueoEntity(Long.valueOf(4),calendar));
			parqueoRepository.save(new ParqueoEntity(Long.valueOf(5),calendar));
		};
	}
	
	

}

