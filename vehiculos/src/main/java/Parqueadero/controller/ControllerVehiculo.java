package Parqueadero.controller;

import Parqueadero.businesslogic.BLVehiculo;
import Parqueadero.entity.Vehiculo;
import Parqueadero.service.InterServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE CONTROL (Controller) — Módulo Vehículos
 * ══════════════════════════════════════════════════════════
 *
 * @Controller: maneja peticiones y retorna nombres de vistas Thymeleaf.
 * Las vistas están en src/main/resources/templates/.
 *
 * Esta clase expone DOS interfaces:
 *  - MVC (Thymeleaf): /vehiculos/* → vistas HTML renderizadas.
 *  - REST (JSON): /api/vehiculos/* → endpoints REST para consumo
 *    desde otros microservicios o el frontend JavaScript.
 */
@Controller
@RequestMapping("/vehiculos")
public class ControllerVehiculo {

    private final BLVehiculo bl;
    private final InterServiceClient interService;

    @Autowired
    public ControllerVehiculo(BLVehiculo bl, InterServiceClient interService) {
        this.bl = bl;
        this.interService = interService;
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

    // ═══════════════════════════════════════════════════════
    //  ENDPOINTS REST (JSON) — Para consumo inter-microservicios
    // ═══════════════════════════════════════════════════════

    /**
     * GET /api/vehiculos — Lista todos los vehículos como JSON.
     */
    @GetMapping("/api/vehiculos")
    @ResponseBody
    public java.util.List<Vehiculo> listarApi() {
        return bl.listarTodos();
    }

    /**
     * GET /api/vehiculos/{id} — Devuelve un vehículo como JSON.
     */
    @GetMapping("/api/vehiculos/{id}")
    @ResponseBody
    public Vehiculo obtenerApi(@PathVariable long id) {
        var vehiculo = bl.buscarPorId(id);
        return vehiculo.orElse(null);
    }

    /**
     * POST /api/vehiculos — Guarda un vehículo desde JSON.
     */
    @PostMapping("/api/vehiculos")
    @ResponseBody
    public Vehiculo guardarApi(@RequestBody Vehiculo vehiculo) {
        return bl.guardar(vehiculo);
    }

    /**
     * PUT /api/vehiculos — Actualiza un vehículo desde JSON.
     */
    @PutMapping("/api/vehiculos")
    @ResponseBody
    public Vehiculo actualizarApi(@RequestBody Vehiculo vehiculo) {
        return bl.guardar(vehiculo);
    }

    /**
     * DELETE /api/vehiculos/{id} — Elimina un vehículo.
     */
    @DeleteMapping("/api/vehiculos/{id}")
    @ResponseBody
    public boolean eliminarApi(@PathVariable long id) {
        return bl.eliminar(id);
    }

    /**
     * GET /api/vehiculos/paginado?page=1&size=10 — Lista paginada.
     */
    @GetMapping("/api/vehiculos/paginado")
    @ResponseBody
    public java.util.List<Vehiculo> listarPaginadoApi(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return bl.listarPaginado(page, size);
    }

    /**
     * GET /vehiculos/api/vehiculos/resumen
     * Agrega datos de TODOS los microservicios: vehículos + usuarios + productos.
     * Demuestra la arquitectura de microservicios: este endpoint consolida
     * la información de los 3 servicios en una sola respuesta JSON.
     */
    @GetMapping("/api/vehiculos/resumen")
    @ResponseBody
    public Map<String, Object> resumen() {
        Map<String, Object> resumen = new LinkedHashMap<>();
        resumen.put("microservicio", "ms-parqueadero");
        resumen.put("vehiculos", bl.listarTodos());
        resumen.put("usuarios", interService.listarUsuarios());
        resumen.put("productos", interService.listarProductos());
        return resumen;
    }
}
