androidApplication {
    namespace = "org.example.app"

    dependencies {
        // Core AndroidX UI dependencies
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")

        // Keep existing sample dependencies (not used by the new app logic but retained for compatibility)
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))
    }

    // Unit test configuration (JUnit4) so tests are discovered by default
    testing {
        dependencies {
            implementation("junit:junit:4.13.2")
        }
    }
}
