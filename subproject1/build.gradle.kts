val kotestVersion = extra["kotest.version"] as String
val kotlinxDatetimeVersion = extra["kotlinx.datetime.version"] as String

plugins {
    id("io.gitlab.arturbosch.detekt")
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.register("countCompiledSize") {
    group = "build"
    description = "Count the size of the compiled classes"
    dependsOn("compileKotlin")
    doLast {
        val files = fileTree("build/classes/kotlin/main")
        var size = 0L
        for (file in files) {
            size += file.length()
        }
        println("The size of the compiled classes in subproject1 is $size bytes")
    }
}

class KotlinFilesReportPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("generateKotlinFilesReport") {
            group = "reporting"
            description = "Generates a report of all Kotlin files in the project."

            doLast {
                val kotlinFiles = project.fileTree("src").matching {
                    include("**/*.kt")
                }.files

                println("Kotlin Files Report:\n\n")
                for (file in kotlinFiles) {
                    println(file.name)
                }
            }
        }
    }
}
