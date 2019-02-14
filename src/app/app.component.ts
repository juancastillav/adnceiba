import { Component, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';
import { Observable } from 'rxjs';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
@Injectable()
export class AppComponent {

  readonly BACK_IP = 'http://192.168.0.4:8090';
  parqueosActivos: any;
  parqueosCerrados: any;
  vehiculos: any;
  darSalidaResponse: any;
  vehiculoPorId: any;
  inputBuscarVehiculo: any;
  respuesta: any;
  tipoVehiculo: string = '';
  data: JSON;
  user: any = {
    message: 'asdsad'
  };

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.todosParqueos();
    this.todosParqueosCerrados()
  }

  todosParqueos() {
    this.parqueosActivos = this.http.get(this.BACK_IP + '/parqueo/parqueosActivos')
  }

  todosParqueosCerrados() {
    this.parqueosCerrados = this.http.get(this.BACK_IP + '/parqueo/parqueosCerrados')
  }

  todosVehiculos() {
    this.vehiculos = this.http.get(this.BACK_IP + '/vehiculo/all')
  }

  darSalida(parqueo) {
    this.http.put(this.BACK_IP + '/parqueo/salida', parqueo, { observe: 'response' })
      .pipe().subscribe((response: any) => {
        if (response.statusText == "OK") {
          this.todosParqueos();
          this.todosParqueosCerrados()
        } else {
          alert("ERROR " + response.statusText);
        }
      });
  }

  registrarParqueoNuevo(cilindraje, placa) {
    console.log("ENTRO A REGISTRAR PARQUEO");
    let datos = {
      "vehiculoEntity": {
        "cilindraje": cilindraje,
        "placa": placa,
        "tipoVehiculo": this.tipoVehiculo
      }
    };

    this.http.post(this.BACK_IP + '/parqueo/parqueo', datos, { observe: 'response' })
      .pipe().subscribe((response: any) => {
        if (response.status == 200) {
          this.todosParqueos();
        } else {
          this.user = response.body;
          alert("ERROR: " + this.user.message);
        }

      });
  }

  buscarParqueoPorPlaca(title: string) {
    if (title === "") {
      this.todosParqueos();
    } else {
      this.respuesta = this.http.get(this.BACK_IP + '/parqueo/parqueo/' + title)
      if (this.respuesta != null) {
        this.parqueosActivos = this.respuesta;
      }
      else {
        console.log("ES NULO ");
      }

    }

  }


}
