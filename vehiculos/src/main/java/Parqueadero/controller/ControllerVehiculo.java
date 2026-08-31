package Parqueadero.controller;

import Parqueadero.businesslogic.BLVehiculo;
import Parqueadero.entity.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE CONTROL (Controller) — Módulo Vehículos
 * ══════════════════════════════════════════════════════════
 *
 * @Controller: maneja peticiones y retorna nombres de vistas Thymeleaf.
 * Las vistas están en src/main/resources/templates/.
 */
@Controller
@RequestMapping("/vehiculos")
public class ControllerVehiculo {

    private final BLVehiculo bl;

    @Autowired
    public ControllerVehiculo(BLVehiculo bl) {
        this.bl = bl;
    }

    /**
     * GET /vehiculos → Lista todos los vehículos.
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vehiculos", bl.listarTodos());
        return "vehiculos/listar";
    }

    /**
     * GET /vehiculos/nuevo → Formulario para crear.
     */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "vehiculos/formulario";
    }

    /**
     * GET /vehiculos/{id} → Ver detalle de un vehículo.
     */
    @GetMapping("/{id}")
    public String ver(@PathVariable long id, Model model, RedirectAttributes ra) {
        var vehiculo = bl.buscarPorId(id);
        if (vehiculo.isPresent()) {
            model.addAttribute("vehiculo", vehiculo.get());
            return "vehiculos/detalle";
        }
        ra.addFlashAttribute("error", "Vehículo no encontrado");
        return "redirect:/vehiculos";
    }

    /**
     * GET /vehiculos/{id}/editar → Formulario para editar.
     */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable long id, Model model, RedirectAttributes ra) {
        var vehiculo = bl.buscarPorId(id);
        if (vehiculo.isPresent()) {
            model.addAttribute("vehiculo", vehiculo.get());
            return "vehiculos/formulario";
        }
        ra.addFlashAttribute("error", "Vehículo no encontrado");
        return "redirect:/vehiculos";
    }

    /**
     * POST /vehiculos/guardar → Guardar (crear o actualizar).
     */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Vehiculo vehiculo, RedirectAttributes ra) {
        var guardado = bl.guardar(vehiculo);
        if (guardado != null) {
            ra.addFlashAttribute("exito", "Vehículo guardado correctamente");
        } else {
            ra.addFlashAttribute("error", "Datos inválidos: placa (6), marca, modelo (4)");
        }
        return "redirect:/vehiculos";
    }

    /**
     * POST /vehiculos/{id}/eliminar → Eliminar un vehículo.
     */
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable long id, RedirectAttributes ra) {
        if (bl.eliminar(id)) {
            ra.addFlashAttribute("exito", "Vehículo eliminado");
        } else {
            ra.addFlashAttribute("error", "No se pudo eliminar");
        }
        return "redirect:/vehiculos";
    }
}
