package co.com.ceiba.parqueadero.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.com.ceiba.parqueadero.model.Parqueo;
import co.com.ceiba.parqueadero.persistence.ParqueoEntity;
import co.com.ceiba.parqueadero.persistence.ParqueoRepository;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;

@CrossOrigin
@RestController
@RequestMapping("parqueo")
public class ParqueoController {

	private final ParqueoRepository parqueoRepository;		
	private Parqueo parqueo;

	public ParqueoController(ParqueoRepository parqueoRepository, VehiculoRepository vehiculoRepository) {
		this.parqueoRepository = parqueoRepository;
		parqueo = new Parqueo(parqueoRepository, vehiculoRepository);
	}

	@GetMapping("/all")
	public List<ParqueoEntity> todosLosParqueos() {
		return parqueoRepository.findAll();
	}

	@GetMapping("/{parqueoId}")
	public ResponseEntity<Object> parqueoPorId(@PathVariable String parqueoId) {		
		Optional<ParqueoEntity> parqueoEntity = parqueoRepository.findById(Long.valueOf(parqueoId));
		if (parqueoEntity.isPresent())
			return new ResponseEntity<>(parqueoEntity.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/parqueo/{placaVehiculo}")
	public List<ParqueoEntity> parqueoPorPlacaVehiculo(@PathVariable String placaVehiculo) {
		ParqueoEntity buscado = parqueo.buscarParqueoPorPlaca(placaVehiculo);
		List<ParqueoEntity>listaRetorno =new ArrayList<>();
		if(buscado==null)				
			return Collections.emptyList();
		else
			listaRetorno.add(buscado);
		return listaRetorno;
	}

	@PostMapping("/parqueo")
	public ResponseEntity<Object> nuevoParqueo(@RequestBody ParqueoEntity parqueo) {
		String mensaje = this.parqueo.nuevoParqueo(parqueo);		
		if ("OK".equals(mensaje))
			return new ResponseEntity<>("PARQUEO CREADO CON EXITO", HttpStatus.OK);
		else
			return new ResponseEntity<>(mensaje, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping("/salida")
	public ResponseEntity<Object> salidaDeParqueo(@RequestBody ParqueoEntity parqueoEntity) {
		String mensaje = parqueo.salidaDeParqueo(parqueoEntity);
		if ("OK".equals(mensaje)) 
			return new ResponseEntity<>("SALIDA CREADA CON EXITO", HttpStatus.OK);			
		else
			return new ResponseEntity<>(mensaje, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	

}
