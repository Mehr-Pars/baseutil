## Installation

Add to _build.gradle_ (app):
```groovy
implementation '{{libraryGroup}}:{{libraryName}}:{{versionName}}'
```

Add to _build.gradle_ (Project):
```groovy
allprojects {
    repositories {
        
        maven {
            url "http://maven.worthnet.ir:8081/artifactory/libs-release-local"
            credentials {
                username = "${artifactory_username}"
                password = "${artifactory_password}"
            }
        }
       
    }
}
```
 
## License  
[Arzesh Network][link]


[link]: http://worthnet.ir