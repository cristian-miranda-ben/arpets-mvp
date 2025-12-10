# 🐾 ARPets

**Mascota Virtual con Realidad Aumentada para Android**

ARPets es una aplicación Android que permite tener una mascota virtual usando tecnología de Realidad Aumentada. La mascota aparece en tu entorno real a través de la cámara y puedes interactuar con ella alimentándola, acariciándola y jugando.

---

## 📱 Capturas de Pantalla

> *Próximamente*

---

## ✨ Características

- 🎮 **Realidad Aumentada** - Ve tu mascota en el mundo real usando ARCore
- 🍖 **Alimentar** - Dale de comer cada 2 horas (+2 ánimo)
- 💜 **Acariciar** - Hazle cariño cada 1 hora (+1 ánimo)
- ⚽ **Jugar** - Diviértete con ella cada 1 hora (+1 ánimo)
- 📊 **Sistema de ánimo** - Tu mascota tiene un nivel de felicidad (máx. 10)
- 💾 **Persistencia** - El estado se mantiene durante la sesión

---

## 🛠️ Tecnologías

| Tecnología | Versión |
|------------|---------|
| Kotlin | 2.0.21 |
| Jetpack Compose | BOM 2024.09.00 |
| Navigation Compose | 2.9.5 |
| ARCore | 1.51.0 |
| ARSceneView | 2.3.0 |
| Kotlinx Serialization | 1.7.3 |
| Gradle | 8.13.0 |

---

## 📋 Requisitos

- Android 8.0 (API 26) o superior
- Dispositivo compatible con [ARCore](https://developers.google.com/ar/devices)
- Cámara funcional
- ~100 MB de espacio libre

---

## 🚀 Instalación

### Opción 1: Desde Android Studio

1. **Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/ARPets.git
```

2. **Abrir en Android Studio**
```
File → Open → Seleccionar carpeta ARPets
```

3. **Sincronizar Gradle**
```
Esperar a que Android Studio sincronice las dependencias
```

4. **Ejecutar**
```
Run → Run 'app' (o Shift + F10)
```

### Opción 2: Instalar APK

1. Descargar el APK desde [Releases](https://github.com/tu-usuario/ARPets/releases)
2. En tu dispositivo, habilitar "Instalar desde fuentes desconocidas"
3. Abrir el archivo APK e instalar
4. Conceder permisos de cámara al abrir la app

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/example/ar2/
│
├── MainActivity.kt              # Actividad principal + NavHost
│
├── ui/
│   └── navigation/
│       └── NavRoutes.kt         # Rutas de navegación (Serializable)
│
├── scenes/
│   ├── SplashScreen.kt          # Pantalla de bienvenida
│   ├── InicioScreen.kt          # Menú principal
│   └── UiPetScreen.kt           # Pantalla AR con la mascota
│
└── acciones/
    └── EstadoPet.kt             # Lógica de estado y acciones
```

---

## 🎮 Cómo Usar

1. **Abrir la app** - Verás la pantalla Splash y luego el menú principal

2. **Entrar a la experiencia AR** - Presiona el botón para ver tu mascota

3. **Apuntar a una superficie** - Dirige la cámara hacia una mesa o piso plano

4. **Interactuar** - Usa los botones:
   - 🍖 **Alimentar** - Disponible cada 2 horas
   - 💜 **Acariciar** - Disponible cada 1 hora  
   - ⚽ **Jugar** - Disponible cada 1 hora

5. **Ver mensajes** - La app te indica si la acción fue exitosa o si debes esperar

---

## ⚙️ Configuración de Build

**build.gradle.kts (app)**
```kotlin
android {
    namespace = "com.example.ar2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ar2"
        minSdk = 26
        targetSdk = 36
    }
}

dependencies {
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.material3:material3")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.5")
    
    // AR
    implementation("com.google.ar:core:1.51.0")
    implementation("io.github.sceneview:arsceneview:2.3.0")
    
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
```

---

## 🔮 Próximas Mejoras

- [ ] Persistencia con Room Database
- [ ] Múltiples tipos de mascotas
- [ ] Animaciones según estado de ánimo
- [ ] Notificaciones push
- [ ] Sistema de logros y niveles
- [ ] Migración a ViewModel/MVVM

---

## 🐛 Problemas Conocidos

- El daemon de Kotlin puede terminar inesperadamente en algunos builds
- Algunos caracteres especiales pueden mostrarse incorrectamente en Toast

---

## 👨‍💻 Autor

**Cristian**  
Bootcamp Android - Chile 2025

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

---

## 🙏 Agradecimientos

- [ARCore](https://developers.google.com/ar) - Google
- [SceneView](https://github.com/SceneView/sceneview-android) - Comunidad
- Bootcamp Android Chile

---

⭐ Si te gustó el proyecto, dale una estrella!
