#### checking gradle dependency versions:
- build environment
`./gradlew buildEnvironment | grep sshd`
- runtime
`./gradlew dependencyInsight --dependency org.apache.sshd --configuration runtimeClasspath`