Planificador de Builds de Warframe 🚀
Este proyecto es una aplicación de escritorio desarrollada en JavaFX diseñada para ayudar a los jugadores de Warframe a planificar sus configuraciones de mods sin necesidad de gastar recursos in-game (Endo o Créditos). Permite visualizar el impacto estadístico de cada mod en Warframes y armas de forma dinámica.

🛠️ Requisitos previos
Antes de empezar, asegúrate de tener instalado lo siguiente:
	-Java JDK 22 o superior.
	-NetBeans IDE 22.

🚀 Despliegue y Ejecución
Sigue estos pasos para poner en marcha el proyecto en tu máquina local:

1. Clonar el repositorio
Abre una terminal y ejecuta el siguiente comando:

git clone https://github.com/SantiPrice28/Planificador_Builds_Warframe.git

2. Configuración de la Base de Datos
Nota técnica: Actualmente, el proyecto utiliza una conexión directa a MySQL para la persistencia. Sin embargo, se está trabajando en una migración hacia una arquitectura basada en API REST, lo que permitirá desacoplar totalmente la lógica de datos de la interfaz JavaFX.

Debido a este proceso de transición, el script SQL completo no se incluye en esta versión del repositorio. Para pruebas de despliegue sin base de datos, el sistema permite la navegación por las vistas principales de la interfaz mediante el bypass configurado en la clase principal.

3. Abrir y Construir en NetBeans
Abre NetBeans 22.

Ve a File > Open Project y selecciona la carpeta clonada.

Haz clic derecho sobre el proyecto y selecciona "Clean and Build". Esto descargará automáticamente todas las dependencias (JavaFX, MySQL Connector, BCrypt) mediante Maven.

4. Ejecución
Debido a que el proyecto utiliza JavaFX con Maven, no uses el botón "Run" convencional si no has configurado las acciones. La forma más segura de ejecutarlo es:

Clic derecho sobre el proyecto.

Selecciona Run Maven > Goals.

Escribe el comando: javafx:run

📦 Dependencias Principales
El proyecto gestiona automáticamente las siguientes librerías a través de pom.xml:

JavaFX (Controls y FXML): Para la interfaz gráfica moderna.

MySQL Connector/J: Para la comunicación con el servidor de base de datos.

jBCrypt: Para el hashing de seguridad en las contraseñas de usuario.
