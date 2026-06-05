plugins {
  java
  `java-library`
  `maven-publish`
}

java {
  withJavadocJar()
  withSourcesJar()
}

repositories {
  mavenCentral()
  maven("https://repo.hpfxd.com/releases/")
}

dependencies {
  compileOnly(libs.spigot)
  compileOnlyApi(libs.adventure)
  compileOnlyApi(libs.guava)
  compileOnlyApi(libs.lombok)
  annotationProcessor(libs.lombok)
}

publishing {
  publications.create<MavenPublication>("maven") {
    from(components["java"])

    groupId = project.group.toString()
    artifactId = project.name
    version = project.version.toString()
  }
}
