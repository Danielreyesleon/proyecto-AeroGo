# AeroGO — Sistema de Gestión de Aerolínea

Un sistema completo de gestión de aerolíneas desarrollado en Java aplicando principios de **Programación Orientada a Objetos (POO)** y una interfaz gráfica interactiva creada con **Swing**.

---

##  Características Principales

- **Gestión de Vuelos y Aviones:** Configuración de matrices de asientos por clases (Primera, Ejecutiva y Económica) con cálculo de porcentaje de ocupación en tiempo real.
- **Venta de Tiquetes y Tarifación Dinámica:** Cálculo de precios en función de la clase del asiento, el nivel del pasajero (Platino, Oro, Estándar) y la alta demanda de ocupación (>80%).
- **Mapeo de Asientos:** Matriz interactiva para visualización, selección, ocupación y liberación de asientos.
- **Módulo de Reservas y Check-in:** Gestión del ciclo de vida del abordaje (Activa, Check-in Realizado, Abordado, Cancelada).
- **Control de Equipaje:** Cálculo automático de penalizaciones y cobros adicionales por exceso de peso (>23 kg por maleta).
- **Gestión Financiera (Patrón Singleton):** Rastreo unificado de ingresos por tiquetes, equipajes adicionales, servicios a bordo y penalizaciones por cancelación.

---

##  Tecnologías Utilizadas

- **Lenguaje:** Java (JDK 8+)
- **GUI:** Java Swing / AWT
- **Arquitectura:** Programación Orientada a Objetos (POO), Patrón Singleton.

---

##  Estructura del Proyecto

```text
AeroGO/
└── src/
    └── aerolinea/
        └── global/
            └── java/
                ├── Asiento.java             # Representación y estado del asiento
                ├── Avion.java               # Configuración de avión y distribución de clase
                ├── FinanzasAerolinea.java   # Módulo Singleton para ingresos totales
                ├── Main.java                # Punto de entrada de la aplicación
                ├── Pasajero.java            # Entidad de pasajeros y categoría
                ├── Reserva.java             # Flujo de reservas, maletas y reembolso
                ├── Tarificador.java         # Motor de cálculo dinámico de precios
                ├── TipoClase.java           # Enum para categorías de clase
                ├── Tiquete.java             # Emisión de billetes
                ├── Ventas.java              # Proceso de compra y ocupación
                ├── VentanaPrincipal.java    # Interfaz gráfica principal (Swing)
                └── Vuelo.java               # Gestión de información del vuelo


git clone [https://github.com/Danielreyesleon/AeroGO.git](https://github.com/Danielreyesleon/AeroGO.git)
cd AeroGO

javac src/aerolinea/global/java/*.java -d bin

java -cp bin aerolinea.global.java.Main
