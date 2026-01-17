pipeline {
    agent any
    tools {
        jdk 'JAVA_HOME'
    }
    environment {
        SONAR_TOKEN = credentials('sonarcloud-token1')
        DOCKER_IMAGE = "store-app"
        CONTAINER_NAME = "store-app-container"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/benazirshaik11/spring-api-starter.git'
            }
        }

        stage('Build Application') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Build & SonarCloud Analysis') {
            steps {
                withSonarQubeEnv('SonarCloud') {
                    bat """
                    mvn sonar:sonar ^
                    -Dsonar.projectKey=benazirshaik11_spring-api-starter ^
                    -Dsonar.organization=benazirshaik11
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t %DOCKER_IMAGE%:latest .'
            }
        }

//          Use this stage when we run application in docker container
//          stage('Run Docker Container') {
//              steps {
//                   bat """
//                      docker stop %CONTAINER_NAME% || echo Container not running
//                      docker rm %CONTAINER_NAME% || echo Container not present
//                      docker run -d -p 8080:8080 --name %CONTAINER_NAME% %DOCKER_IMAGE%:latest
//                       """
//              }
//          }

    }
     post {
            success {
                echo 'Build and SonarCloud analysis completed successfully.Docker image is created successfully'
            }
            failure {
                echo 'pipeline failed'
            }
        }
}
