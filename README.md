Sistema Multiagente para Gestión de Requisitos y Pruebas
Descripción del Proyecto

Este proyecto implementa un sistema multiagente basado en el framework JADE que simula el proceso completo de gestión de requisitos y casos de prueba en el desarrollo de software. Los agentes colaboran autónomamente para transformar historias de usuario en casos de prueba mediante un proceso distribuido y especializado.
🤖 Agentes y Funcionalidades
Agentes Principales
Agente	Función Principal	Servicio Registrado
POAgent	Product Owner que genera Historias de Usuario	creador-hu
CUAgent	Transforma Historias de Usuario en Casos de Uso	procesador-cu
RFAgent	Deriva Requisitos Funcionales de Historias de Usuario	procesador-rf
TestCaseAgent	Genera Casos de Prueba a partir de CUs y RFs	generador-cp
Flujo de Trabajo

    POAgent genera Historias de Usuario y las distribuye

    CUAgent y RFAgent procesan las HUs en paralelo

    TestCaseAgent recibe CUs y RFs para generar Casos de Prueba

    Todo el sistema utiliza el Directory Facilitator de JADE para descubrimiento de servicios

🛠️ Tecnologías Utilizadas

    Java JDK 8+

    JADE Framework (Java Agent DEvelopment Framework)

    Maven para gestión de dependencias
