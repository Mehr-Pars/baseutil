## Installation

Add to _build.gradle_ (app):
```groovy
implementation 'mehrpars.mobile.lib:baseutil:1.1.0'
```

Add to _build.gradle_ (Project). use GitHubPackages url or MehrPars artifactory:
```groovy
allprojects {
    repositories {
        // get library from GitHubPackages
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Mehr-Pars/android-packages")
            credentials {
                username = mpars_github_packages_username
                password = mpars_github_packages_consume_auth_key
            }
        }
        // or from Mehr Pars artifactory repository
        maven {
            url "http://maven2.mpars.ir/artifactory/libs-release-local"
            credentials {
                username = mpars_artifactory_username
                password = mpars_artifactory_password
            }
        }
       
    }
}
```
 
## License  
[Mehr Pars ICT][mp]


[mp]: https://www.mehrparsict.com