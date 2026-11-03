pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    stages {
        stage('Checkout code') {
            steps {
                git branch: 'main', url: 'https://github.com/nourmeskine/country-service1'
            }
        }
        stage('Build maven') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Build Dockerfile') {
    steps {
        sh 'docker build . -t my-country-service:$BUILD_NUMBER'
        withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials',
                                          usernameVariable: 'DOCKER_USER',
                                          passwordVariable: 'DOCKER_PASS')]) {
            sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
            sh 'docker tag my-country-service:$BUILD_NUMBER $DOCKER_USER/my-country-service:$BUILD_NUMBER'
            sh 'docker push $DOCKER_USER/my-country-service:$BUILD_NUMBER'
        }
    }
}
        stage('Deploy micro-service') {
            steps {
                sh 'docker rm -f $(docker ps -aq)'
                sh 'docker run -d -p 8082:8082 my-country-service:$BUILD_NUMBER'
            }
        }
    }
}
