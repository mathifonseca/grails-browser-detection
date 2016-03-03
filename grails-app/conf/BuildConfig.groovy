grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.fork = [
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits("global") {
    }
    log "warn"
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo 'https://oss.sonatype.org/content/repositories/public'
    }
    dependencies {
        compile 'eu.bitwalker:UserAgentUtils:1.19'
    }

    plugins {
        build(":release:3.0.1",
              ":rest-client-builder:2.0.3") {
            export = false
        }

        provided(":version-update:1.3.2"){
            export = false
        }
    }
}
