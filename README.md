Sistema Multiagente para Gestión de Requisitos y Pruebas

Descripción del Proyecto

El presente proyecto desarrolla un sistema multiagente sobre el framework JADE (Java Agent DEvelopment Framework), orientado a la simulación del proceso de gestión de requisitos y casos de prueba en el desarrollo de software. JADE ofrece una infraestructura que facilita la comunicación, autonomía y cooperación entre agentes, lo que lo convierte en una herramienta idónea para modelar procesos distribuidos y colaborativos.
En este sistema, cada agente cumple un rol especializado: el POAgent genera historias de usuario, el CUAgent las transforma en casos de uso, el RFAgent deriva requisitos funcionales y el TestCaseAgent produce los casos de prueba. De esta forma, se automatiza un flujo de trabajo típico en ingeniería de software, asegurando trazabilidad y reduciendo errores humanos.


    POAgent genera Historias de Usuario y las distribuye

    CUAgent y RFAgent procesan las HUs en paralelo

    TestCaseAgent recibe CUs y RFs para generar Casos de Prueba

    Todo el sistema utiliza el Directory Facilitator de JADE para descubrimiento de servicios

🛠️ Tecnologías Utilizadas

    Java JDK 8+

    JADE Framework (Java Agent DEvelopment Framework)

    Maven para gestión de dependencias
