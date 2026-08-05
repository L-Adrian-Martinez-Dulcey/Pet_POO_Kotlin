import java.util.UUID

// Paso 1: Enums y Data Classes
enum class EstadoSalud(val descripcion: String) {
    EXCELENTE("La mascota está lista para adopción inmediata"),
    EN_TRATAMIENTO("La mascota requiere atención médica antes de ir a casa"),
    ADOPTADO("La mascota ya cuenta con un hogar feliz")
}

data class Adoptante(
    val id: String = UUID.randomUUID().toString().take(6),
    val nombre: String,
    val telefono: String,
    val tieneExperienciaPrevia: Boolean
)

// Paso 2: Clase Abstracta Mascota
abstract class Mascota(
    val id: String,
    val nombre: String,
    var edad: Int,
    estadoSaludInicial: EstadoSalud
) {
    var estadoSalud: EstadoSalud = estadoSaludInicial
        protected set

    fun actualizarEdad() {
        edad++
        println("🎂 ¡Feliz cumpleaños $nombre! Ahora tiene $edad años.")
    }

    fun cambiarEstadoSalud(nuevoEstado: EstadoSalud) {
        this.estadoSalud = nuevoEstado
        println("🏥 El estado médico de $nombre cambió a: ${nuevoEstado.descripcion}")
    }

    abstract fun hacerSonido(): String
    abstract fun calcularCostoManutencionMensual(): Double
}

// Paso 3: Herencia y Polimorfismo
class Perro(
    id: String,
    nombre: String,
    edad: Int,
    estadoSalud: EstadoSalud,
    val tamanoRaza: String
) : Mascota(id, nombre, edad, estadoSalud) {
    override fun hacerSonido(): String = "🐶 ¡Guau! ¡Guau!"
    override fun calcularCostoManutencionMensual(): Double =
        when (tamanoRaza.lowercase()) {
            "grande" -> 80.0
            "mediana" -> 50.0
            else -> 30.0
        }
}

class Gato(
    id: String,
    nombre: String,
    edad: Int,
    estadoSalud: EstadoSalud,
    val esIndoor: Boolean
) : Mascota(id, nombre, edad, estadoSalud) {
    override fun hacerSonido(): String = "🐱 ¡Miau miau!"
    override fun calcularCostoManutencionMensual(): Double =
        if (esIndoor) 40.0 else 25.0
}

// Paso 4: Interfaces y Sealed Classes
sealed class ResultadoAdopcion {
    data class Exito(val mascota: Mascota, val adoptante: Adoptante, val codigoContrato: String) : ResultadoAdopcion()
    data class Rechazado(val motivo: String) : ResultadoAdopcion()
}

interface Adoptable {
    fun procesarAdopcion(adoptante: Adoptante): ResultadoAdopcion
}

// Paso 5: Singleton RefugioCentral
object RefugioCentral {
    val nombreRefugio = "Refugio Huellitas de Amor 🐾"
    private val listaMascotas = mutableListOf<Mascota>()

    fun registrarMascota(mascota: Mascota) {
        listaMascotas.add(mascota)
        println("📥 Mascota '${mascota.nombre}' registrada en el inventario del refugio.")
    }

    fun mostrarInventario() {
        println("\n==========================================")
        println("📋 INVENTARIO ACTUAL DE $nombreRefugio")
        println("==========================================")
        if (listaMascotas.isEmpty()) {
            println("El refugio está vacío en este momento.")
            return
        }
        for (m in listaMascotas) {
            println("• [ID: ${m.id}] ${m.nombre} (${m.javaClass.simpleName}) | Edad: ${m.edad} años | Salud: ${m.estadoSalud}")
            println(" Sonido: ${m.hacerSonido()} | Costo mensual: $${m.calcularCostoManutencionMensual()}")
        }
        println("==========================================\n")
    }

    fun intentarAdopcion(mascotaId: String, adoptante: Adoptante): ResultadoAdopcion {
        val mascota = listaMascotas.find { it.id == mascotaId }
        if (mascota == null) return ResultadoAdopcion.Rechazado("La mascota con ID $mascotaId no existe.")
        if (mascota.estadoSalud == EstadoSalud.EN_TRATAMIENTO)
            return ResultadoAdopcion.Rechazado("La mascota '${mascota.nombre}' está en tratamiento médico y aún no puede ser adoptada.")
        if (mascota.estadoSalud == EstadoSalud.ADOPTADO)
            return ResultadoAdopcion.Rechazado("La mascota '${mascota.nombre}' ya fue adoptada previamente.")

        mascota.cambiarEstadoSalud(EstadoSalud.ADOPTADO)
        val codigo = "CONTRATO-${UUID.randomUUID().toString().take(5).uppercase()}"
        return ResultadoAdopcion.Exito(mascota, adoptante, codigo)
    }
}

// Paso 6: Función main()
fun main() {
    println("🌟 BIENVENIDO AL SISTEMA DE ADOPCIÓN DE MASCOTAS 🌟")

    val p1 = Perro("P01", "Firulais", 3, EstadoSalud.EXCELENTE, "Grande")
    val g1 = Gato("G01", "Michi", 2, EstadoSalud.EN_TRATAMIENTO, true)
    val p2 = Perro("P02", "Bobi", 1, EstadoSalud.EXCELENTE, "Pequeña")

    RefugioCentral.registrarMascota(p1)
    RefugioCentral.registrarMascota(g1)
    RefugioCentral.registrarMascota(p2)

    RefugioCentral.mostrarInventario()

    val cliente = Adoptante(nombre = "Maria Lopez", telefono = "555-1234", tieneExperienciaPrevia = true)
    println("👤 Adoptante registrado: ${cliente.nombre}")

    println("\n--- Intento de Adopción 1 (Gato en tratamiento) ---")
    val resultado1 = RefugioCentral.intentarAdopcion("G01", cliente)
    procesarResultado(resultado1)

    println("\n--- Intento de Adopción 2 (Perro excelente salud) ---")
    val resultado2 = RefugioCentral.intentarAdopcion("P01", cliente)
    procesarResultado(resultado2)

    RefugioCentral.mostrarInventario()
}

fun procesarResultado(resultado: ResultadoAdopcion) {
    when (resultado) {
        is ResultadoAdopcion.Exito -> {
            println("🎉 ¡ADOPCIÓN APROBADA!")
            println(" Mascota: ${resultado.mascota.nombre}")
            println(" Adoptante: ${resultado.adoptante.nombre}")
            println(" Código de Contrato: ${resultado.codigoContrato}")
        }
        is ResultadoAdopcion.Rechazado -> {
            println("❌ ADOPCIÓN RECHAZADA")
            println(" Motivo: ${resultado.motivo}")
        }
    }
}
