
---

## Ejecución
1. Abre el proyecto en **IntelliJ IDEA**.
2. Asegúrate de tener configurado el SDK de Kotlin.
3. Ejecuta el archivo `Main.kt` desde el botón ▶️.
4. Verás en consola el flujo de registro de mascotas y los intentos de adopción.

---

## Conceptos aplicados
- **Enums**: Para representar el estado de salud de cada mascota.
- **Data Classes**: Para modelar al adoptante con atributos simples.
- **Clase Abstracta**: `Mascota` define atributos y métodos comunes.
- **Herencia y Polimorfismo**: `Perro` y `Gato` extienden `Mascota` y redefinen comportamiento.
- **Interfaces y Sealed Classes**: Para manejar resultados de adopción de forma segura.
- **Singleton**: `RefugioCentral` como punto único de gestión de mascotas.

---

## Diagrama UML

```plaintext
                Mascota (abstract)
               /        \
          Perro          Gato
             \            /
              RefugioCentral (Singleton)
                     |
                 Adoptante
