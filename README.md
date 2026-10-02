# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción

Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño
de Software. Un único proyecto Spring Boot (`compras-comportamiento`)
que resuelve cuatro necesidades reales del backend de ComprasUDES,
el sistema interno de solicitudes de compra corporativas: aprobación
por niveles jerárquicos, ejecución reversible de solicitudes
aprobadas, notificaciones ante cambios de estado y reglas de
transición según el estado actual de la solicitud.

## Estructura del proyecto

```
sanchez-post1-u4/
├── pom.xml
└── src/
    ├── main/java/com/universidad/compras/
    │   ├── ComprasApp.java                 (DADO) arranque Spring Boot
    │   ├── modelo/
    │   │   └── Solicitud.java              (DADO) entidad compartida
    │   ├── aprobacion/                     Necesidad 1 — Chain of Responsibility
    │   │   ├── ServicioAprobacion.java     (DADO) contrato
    │   │   ├── ResultadoAprobacion.java    (DADO)
    │   │   ├── ControladorSolicitudes.java (DADO) REST, no modificado
    │   │   ├── NivelAprobacion.java         manejador abstracto de la cadena
    │   │   ├── RevisorCumplimiento.java     eslabón para categoría INTERNACIONAL
    │   │   ├── SupervisorArea.java          nivel por monto (≤ 2.000.000)
    │   │   ├── GerenteArea.java             nivel por monto (≤ 10.000.000)
    │   │   ├── DirectorFinanciero.java      nivel por monto (sin límite)
    │   │   └── ServicioAprobacionEncadenado.java  arma la cadena (@Service)
    │   ├── ejecucion/                      Necesidad 2 — Command
    │   │   ├── PresupuestoService.java     (DADO) no modificado
    │   │   ├── OrdenCompraService.java     (DADO) no modificado
    │   │   ├── ComandoEjecucion.java        interfaz comando
    │   │   ├── ReservarPresupuestoComando.java
    │   │   ├── GenerarOrdenComando.java
    │   │   └── EjecutorSolicitud.java        invocador + historial
    │   ├── notificacion/                   Necesidad 3 — Observer
    │   │   ├── ClientesNotificacion.java   (DADO) no modificado
    │   │   ├── ObservadorSolicitud.java     interfaz suscriptor
    │   │   ├── ObservadorCorreo.java
    │   │   ├── ObservadorDashboard.java
    │   │   ├── ObservadorAuditoria.java
    │   │   └── PublicadorCambioEstado.java  sujeto (punto único de cambio)
    │   └── estado/                         Necesidad 4 — State
    │       ├── EstadoSolicitud.java         interfaz estado
    │       ├── EstadoBase.java              rechazo por defecto
    │       ├── EstadoPendiente.java / EstadoEnAprobacion.java
    │       ├── EstadoAprobada.java / EstadoRechazada.java
    │       ├── EstadoEjecutada.java / EstadoCancelada.java
    │       ├── EstadosSolicitud.java        fábrica nombre -> estado
    │       └── ContextoSolicitud.java       contexto State (+ enlace a Observer)
    └── test/java/com/universidad/compras/
        ├── aprobacion/AprobacionNivelesTest.java
        ├── ejecucion/EjecucionSolicitudTest.java
        ├── notificacion/NotificacionEstadoTest.java
        └── estado/TransicionEstadoTest.java
```

Dependencias entre paquetes (sin ciclos): `aprobacion` y `ejecucion`
→ `estado` → `notificacion` → `modelo`.

## Cómo ejecutar

```
$ mvn clean package
$ mvn spring-boot:run
$ mvn test
```

> El proyecto fija `<java.version>17</java.version>` en el `pom.xml`
> (el enunciado exige Java 17). Se agregaron al `pom.xml` únicamente esa
> propiedad y el `spring-boot-maven-plugin` para poder empaquetar y usar
> `mvn spring-boot:run`; no se añadió ninguna dependencia nueva.

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos

**Patrón aplicado: Chain of Responsibility.** Cada nivel es un eslabón
(`NivelAprobacion` y sus subclases `RevisorCumplimiento`, `SupervisorArea`,
`GerenteArea`, `DirectorFinanciero`). Cada uno decide si resuelve la
solicitud o la delega al siguiente mediante `NivelAprobacion.delegar(...)`.
`ServicioAprobacionEncadenado.construirCadena()` enlaza los niveles
(`revisor.enlazarCon(supervisor).enlazarCon(gerente).enlazarCon(director)`)
y entrega solo la cabeza; `ControladorSolicitudes` (DADO) sigue dependiendo
únicamente del contrato `ServicioAprobacion` y nunca sabe cuántos niveles
hay ni en qué orden. El `RevisorCumplimiento` se coloca antes de los niveles
por monto y solo resuelve las solicitudes `INTERNACIONAL`; para el resto
delega de inmediato, por lo que una sola cadena atiende ambos casos.
Agregar, quitar o reordenar un nivel se hace en ese único método.

**Alternativa descartada: Command.** Command encapsula una acción con
`ejecutar()`/`deshacer()` e historial; serviría si cada *decisión* debiera
poder revertirse o quedar registrada, pero aquí no hay nada que deshacer:
el problema es **encaminar** una petición por una secuencia de decisores
hasta que uno la resuelve. Command no modela esa delegación condicional
entre niveles (quién pasa a quién), que es justo lo que da
Chain of Responsibility.

### Necesidad 2 — Ejecución reversible de solicitudes

**Patrón aplicado: Command.** `ComandoEjecucion` define
`ejecutar()`/`deshacer()`; `ReservarPresupuestoComando` y
`GenerarOrdenComando` encapsulan cada operación delegando en los servicios
DADO (`PresupuestoService`, `OrdenCompraService`) sin reimplementarlos.
El invocador `EjecutorSolicitud` guarda cada comando ejecutado en un
`Deque` (`historial`) en orden; `getHistorial()` expone toda la secuencia
—no solo la última— y `deshacerUltima()` revierte únicamente el último
comando (p. ej. cancelar la orden dejando intacta la reserva). Al terminar
`ejecutarCompra(...)`, la solicitud queda `EJECUTADA`.

**Alternativa descartada: Chain of Responsibility (la de la Necesidad 1).**
No aplica: aquí no hay decisores evaluando condiciones para delegar o
resolver una petición entrante; hay operaciones discretas que un mismo
actor (el equipo de Compras) ejecuta y, eventualmente, deshace. Una cadena
no ofrece `deshacer()` por operación ni un historial inspeccionable; solo
enruta. Por eso la Necesidad 1 no resuelve la 2 ni viceversa: una enruta
una petición, la otra registra y revierte acciones.

### Necesidad 3 — Notificaciones ante cambio de estado

**Patrón aplicado: Observer.** `PublicadorCambioEstado` (sujeto) mantiene
la lista de `ObservadorSolicitud` y, en `cambiarEstado(...)`, cambia el
estado y notifica a todos. Los observadores concretos `ObservadorCorreo`,
`ObservadorDashboard` y `ObservadorAuditoria` usan los tres métodos de
`ClientesNotificacion` (DADO). El emisor no conoce las reacciones concretas:
publica contra la interfaz. Agregar una cuarta reacción se hace con
`suscribir(...)` sin tocar el publicador (lo verifica
`NotificacionEstadoTest` con un colector de prueba). Este mecanismo se
conecta a los puntos de cambio de estado de las Necesidades 1 y 2 porque
`ContextoSolicitud` (Necesidad 4) realiza **toda** transición a través de
este publicador.

**Alternativa descartada: State (la de la Necesidad 4).** State cambia el
comportamiento *propio* de la solicitud según su estado; aquí la solicitud
ya cambió y son objetos **ajenos** a ella (correo, contabilidad, auditoría)
los que deben enterarse y actuar. No se trata de qué puede hacer la
solicitud, sino de quién reacciona cuando algo le pasó: eso es Observer,
no State.

### Necesidad 4 — Reglas de transición según el estado

**Patrón aplicado: State.** `EstadoSolicitud` declara `aprobar`, `rechazar`,
`ejecutar`, `cancelar`; `EstadoBase` las rechaza por defecto y cada estado
concreto (`EstadoPendiente`, `EstadoAprobada`, `EstadoEjecutada`, etc.)
sobrescribe solo las que permite y transiciona llamando a
`ContextoSolicitud.transicionarA(...)`. `ContextoSolicitud` delega cada
operación en su estado actual; una operación inválida no cambia el estado
(p. ej. `ejecutar()` sobre una `PENDIENTE`). Agregar un estado nuevo
(como `EN_ESPERA_PROVEEDOR`) es crear su clase y registrarla en la fábrica
`EstadosSolicitud`, sin tocar `if/else` dispersos.

**Alternativa descartada: Strategy.** Estructuralmente se parece (una
interfaz con varias implementaciones), pero la intención difiere: en
Strategy un **cliente externo** elige e inyecta el comportamiento en cada
llamada (como un carrito que activa una estrategia de descuento). Aquí
nadie elige desde afuera: es la **propia solicitud** quien decide qué
operaciones son válidas según el estado en que se encuentra, y además
**transiciona** sola de un estado a otro al resolver la operación —algo que
un conjunto de estrategias independientes no hace. Por eso Strategy no
encaja a pesar del parecido.

### Conexión entre necesidades

Hay un único punto de cambio de estado: `PublicadorCambioEstado.cambiarEstado`.
`ContextoSolicitud` (State) lo usa en cada transición válida, de modo que
toda transición dispara las notificaciones (Observer). La aprobación
(Necesidad 1, en `ServicioAprobacionEncadenado.aplicarResultado`) y la
ejecución (Necesidad 2, en `EjecutorSolicitud.ejecutarCompra`) realizan sus
cambios de estado a través de ese contexto, así que aprobar, ejecutar o
cancelar quedan gobernados por las reglas de la Necesidad 4 y notificados
por la Necesidad 3.

### Reflexión — otros tres patrones (opcional)

1. **Iterator.** Para recorrer secuencialmente todas las solicitudes de un
   centro de costo sin exponer si están en una lista, un mapa u otra
   estructura: un iterador ofrece `hasNext()/next()` ocultando el
   almacenamiento interno.
2. **Template Method.** Para los tres comprobantes (orden de compra,
   comprobante de reserva, acta de rechazo) con el mismo esqueleto de
   impresión (encabezado, cuerpo, pie) variando solo el cuerpo: una clase
   base fija el algoritmo y deja el cuerpo como paso abstracto que cada
   comprobante implementa.
3. **Memento.** Para guardar y restaurar instantáneas completas del estado
   de una solicitud sin que el código que las guarda conozca su interior.
   Se diferencia de la Necesidad 2 (Command) en que Command deshace la
   *última operación* paso a paso, mientras que Memento restaura un
   *estado completo* capturado en un punto cualquiera de la historia.

## Herramientas utilizadas

- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code o IntelliJ IDEA, Git, GitHub

## Conclusiones

El laboratorio mostró que elegir un patrón de comportamiento no depende de
su forma sino de la intención del problema: Chain of Responsibility y
Command parten de estructuras parecidas pero uno enruta una petición y el
otro registra y revierte acciones; Observer y State se confunden porque
ambos "reaccionan a un cambio", pero uno avisa a terceros ajenos y el otro
define qué puede hacer el propio objeto. Lo más difícil fue distinguir
State de Strategy, casi idénticos en código: la clave fue preguntar **quién
decide** el comportamiento —un cliente externo o el objeto según su
estado— y si el comportamiento **transiciona** por sí mismo. Mantener el
código DADO intacto obligó a diseñar alrededor de contratos fijos, que es
precisamente donde estos patrones aportan: desacoplan lo que cambia (niveles,
operaciones, reacciones, reglas de estado) de lo que no puede tocarse.
