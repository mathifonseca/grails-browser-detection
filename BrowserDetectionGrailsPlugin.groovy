class BrowserDetectionGrailsPlugin {
    def version = '2.9.0'
    def grailsVersion = "2.2 > *"
    def title = "Browser Detection Plugin"
    def author = "Gennady Tsarik, Mathias Fonseca"
    def authorEmail = "mathifonseca@gmail.com"
    def description = '''\
This plugin helps you detect browsers, versions, language and operating systems from the request headers.
'''
    def documentation = "http://grails.org/plugin/browser-detection"
    def license = "APACHE"
    def issueManagement = [ system: "GITHUB", url: "https://github.com/mathifonseca/grails-browser-detection/issues" ]
    def scm = [ url: "https://github.com/mathifonseca/grails-browser-detection" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }

}
