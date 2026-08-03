package Parqueadero.controler;

import Parqueadero.businesslogic.BLVehiculo;
import Parqueadero.entity.Vehiculo;
import org.springframework.web.bind.annotation.*;

@RestController("")
@RequestMapping("/ControllerVehiculo")
public class ControllerVehiculo {

    //esta clase tendra el CRUD de vehiculo
    @DeleteMapping("/eliminar")
    public void deleteVehiculo(@RequestParam String placa){
        BLVehiculo bl = new BLVehiculo();
        bl.eliminarVehiculo(placa);
    }

    @GetMapping("/consultar")
    public Vehiculo consultarVehiculo(@RequestParam String placa){
        if (placa.length() == 6){
            BLVehiculo vr = new BLVehiculo();
            return vr.consultarVehiculo(placa);
        }
        return null;
    }
}
