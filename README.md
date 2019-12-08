## Installation

Add to _build.gradle_ (app):
```groovy
implementation 'epeyk.mobile.lib:basemodule:1.0.4'
```

Add to _build.gradle_ (Project):
```groovy
allprojects {
    repositories {
        
        maven {
            url "http://maven.worthnet.ir:8081/artifactory/libs-release-local"
            credentials {
                username = "${mpars_artifactory_username}"
                password = "${mpars_artifactory_password}"
            }
        }
       
    }
}
```
 
## License
Arzesh
