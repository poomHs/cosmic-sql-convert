plugins {
  id("java")
  id("org.jetbrains.intellij") version "1.1.2"
}

group = "com.poom.cosmic"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version.set("2021.3.3")
  type.set("IC") // Target IDE Platform


  plugins.set(listOf(/* Plugin Dependencies */))
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}

tasks {
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
  }

  patchPluginXml {
    sinceBuild.set("212")
    untilBuild.set("223.*")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }
}
