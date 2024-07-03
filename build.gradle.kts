import java.util.*

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

tasks.register("countCompiledSize") {
    group = "build"
    description = "Count the size of the compiled classes"
    dependsOn(
        ":subproject1:countCompiledSize",
        ":subproject2:countCompiledSize",
        ":subproject3:countCompiledSize"
    )
}

class KotlinFilesReportPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("generateKotlinFilesReport") {
            group = "reporting"
            description = "Generates a report of all Kotlin files in the project."

            doLast {
                val kotlinFiles = project.fileTree(".").matching {
                    include("**/*.kt", "**/*.kts")
                }.files

                println("Kotlin Files Report:\n\n")
                for (file in kotlinFiles) {
                    println(file.name)
                }
            }
        }
    }
}

apply<KotlinFilesReportPlugin>()

tasks.register("advancedTask") {
    doFirst {
        println("First action - Preparation Step 1")
    }
    doFirst {
        println("First action - Preparation Step 2")
    }
    doLast {
        println("Last action - Cleanup Step 1")
    }
    doLast {
        println("Last action - Cleanup Step 2")
    }
}

abstract class FibonacciTask : DefaultTask() {
    @get:Input
    abstract val number: Property<Int>

    @TaskAction
    fun calculateFibonacci() {
        val n = number.get()
        if (n < 0) {
            throw StopExecutionException("The number must be positive")
        }
        var first = 0
        var second = 1
        repeat(n) {
            second += first
            first = second - first
        }
        println("The $n-th Fibonacci number is $first")
    }
}

tasks.register<FibonacciTask>("Fib_10") {
    number.set(10)
    doFirst {
        println("Preparing to calculate the 10th Fibonacci number")
    }
    doLast {
        println("Finished calculating the 10th Fibonacci number")
    }
}

tasks.register<FibonacciTask>("Fib_20") {
    number.set(20)
}

abstract class TextProcessingTask : DefaultTask() {

    @InputFile
    lateinit var inputFile: File

    @OutputFile
    lateinit var outputFile: File

    @TaskAction
    fun processText() {
        val inputText = inputFile.readText()
        val processedText = inputText.uppercase(Locale.getDefault())
        outputFile.writeText(processedText)
        println("Text has been processed and written to ${outputFile.absolutePath}")
    }
}

tasks.register<TextProcessingTask>("processText") {
    inputFile = file("src/main/resources/input.txt")
    outputFile = file("build/output.txt")

    // Acción que se ejecuta antes de la acción principal
    doFirst {
        println("Preparing to process text from ${inputFile.absolutePath}")
    }

    // Acción que se ejecuta después de la acción principal
    doLast {
        println("Finished processing text. Output written to ${outputFile.absolutePath}")
    }
}
