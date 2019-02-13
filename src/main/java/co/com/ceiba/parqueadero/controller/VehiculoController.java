package co.com.ceiba.parqueadero.controller;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.com.ceiba.parqueadero.model.Vehiculo;
import co.com.ceiba.parqueadero.persistence.VehiculoEntity;
import co.com.ceiba.parqueadero.persistence.VehiculoRepository;


@CrossOrigin
@RestController
@RequestMapping("vehiculo")
public class VehiculoController {
	
	private final VehiculoRepository vehiculoRepository;
	private Vehiculo vehiculo;
	

	public VehiculoController(VehiculoRepository vehiculoRepository) {		
		this.vehiculoRepository = vehiculoRepository;
		vehiculo = new Vehiculo( vehiculoRepository);
	}
	
	@GetMapping("/all")
	public List<VehiculoEntity> todosLosVehiculos(){
		return vehiculoRepository.findAll();
	}
	@GetMapping("/{vehiculoId}")
	public ResponseEntity<Object> vehiculoPorId(@PathVariable long vehiculoId) {
		Optional<VehiculoEntity> vehiculoEntity=vehiculoRepository.findById(vehiculoId);
		if(vehiculoEntity.isPresent())
			return new ResponseEntity<>(vehiculoEntity.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		
	}
	
	@PostMapping("/vehiculo")
	public ResponseEntity<Object> nuevoVehiculo(@RequestBody VehiculoEntity vehiculoEntity) {
		VehiculoEntity mensaje = this.vehiculo.nuevoVehiculo(vehiculoEntity);
		if (mensaje==null)			
			return new ResponseEntity<>(mensaje, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>("VEHICULO CREADO CON EXITO", HttpStatus.OK);
			
	}

}
