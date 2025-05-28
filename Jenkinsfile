pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-credentials-id' // Replace with your Jenkins Docker credentials ID
        DOCKER_IMAGE_NAME = "${env.PROJECT_NAME}:latest" // Define the Docker image name
        DOCKER_REGISTRY = 'your-docker-registry-url' // Replace with your Docker registry URL
    }

    parameters {
        string(name: 'PROJECT_NAME', defaultValue: 'itau', description: 'Name of the project to build')
        string(name: 'BRANCH', defaultValue: 'master', description: 'Your Git branch to build')
        string(name: 'GIT_REPO', defaultValue: 'https://github.com/gabriellfe/itau.git', description: 'Git repository URL') // Replace with your Git repo
    }

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    git branch: params.BRANCH, url: params.GIT_REPO
                }
            }
        }

        stage('Build with Maven') {
            steps {
                dir(params.PROJECT_NAME + '-backend') {
                    def mvnHome = tool name: 'maven', type: 'maven'
                    def mvnCMD = "${mvnHome}/bin/mvn "
                    sh "${mvnCMD} clean install"
                    
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image
                    sh "docker build -t ${DOCKER_IMAGE_NAME} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Log in to Docker Registry
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo $DOCKER_PASSWORD | docker login ${DOCKER_REGISTRY} --username $DOCKER_USERNAME --password-stdin"
                        // Push the Docker image to the registry
                        sh "docker push ${DOCKER_IMAGE_NAME}"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                        // Log in to Docker Registry
                        withAWS(credentials:'jenkins-aws', region:'us-west-1') {
                        sh 'aws sts get-caller-identity'
                        sh 'aws eks update-kubeconfig --name eks --region us-east-1'
                        sh 'helm install itau-eks itau-helm'
                        }
                }
            }
        }
    }
}
