pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('sonarcloud-token')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/benazirshaik11/spring-api-starter.git'
            }
        }

        stage('Build & SonarCloud Analysis') {
            steps {
                withSonarQubeEnv('SonarCloud') {
                    sh """
                    mvn clean verify sonar:sonar \
                    -Dsonar.projectKey=benazirshaik11_spring-api-starter\
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
    }
}
