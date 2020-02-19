import java.io.File
import java.io.InputStream

val variableList = listOf("versionName", "libraryName", "libraryGroup")

fun getVariables(): Map<String, String> {
    val variables: MutableMap<String, String> = mutableMapOf()
    val inputStream: InputStream = File("gradle.properties").inputStream()

    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val vList = variableList.filter { variable -> line.contains(variable) }
            if (vList.isNotEmpty()) {
                val key = vList[0]
                val value = line.trim().replace("$key=", "")
                variables[key] = value
            }
        }
    }
    inputStream.close()

    return variables
}

fun generateReadme() {
    val variables = getVariables()

    // read file and replace variables
    val inputStream: InputStream = File("README_raw.md").inputStream()
    var data = inputStream.bufferedReader().readText()
    variableList.forEach { variable ->
        data = data.replace("{{$variable}}", variables[variable] ?: "", false)
    }
    inputStream.close()

    // save result file
    File("README.md").writeText(data)

    println("README.md created successfully !!")
}

generateReadme()