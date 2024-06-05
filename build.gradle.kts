// Define los plugins que se aplicarán al proyecto raíz
plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

allprojects {
    // Configura el grupo y la versión para todos los proyectos
    group = "build-systems-kt"
    version = extra["build-systems-kt.version"] as String
}

subprojects {
    repositories {
        // Define el repositorio de Maven Central como fuente para las dependencias de los subproyectos
        mavenCentral()
    }
}

tasks.register("cleanAll") {
    // Define una tarea que limpia todos los subproyectos
    dependsOn(":subproject1:clean", ":subproject2:clean", ":subproject3:clean")
}


