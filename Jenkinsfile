node {
    def mvnHome = tool name: 'Maven', type: 'maven'
    def mvnCMD = "${mvnHome}/bin/mvn "
    def serviceName = "itau-backend"
    def imageName = "gabriellfe31222/itau"
    def branch = "${params.BRANCH}"
    echo "${branch}"
    
    stage('checkout') {
            deleteDir()
            checkout([$class: 'GitSCM',
            branches: [[name: "${branch}"]],
            extensions: [],
            userRemoteConfigs: [[url: 'https://github.com/gabriellfe/itau.git']]])
    }
    stage('Build and Push Image') {
			dir("${serviceName}"){
            sh "${mvnCMD} clean install"
            def pom = readMavenPom file: 'pom.xml'

            withCredentials([usernamePassword(credentialsId: 'docker', usernameVariable: 'DOCKER_LOGIN', passwordVariable: 'DOCKER_PASSWORD')]) {
            sh "docker login --username ${DOCKER_LOGIN} --password ${DOCKER_PASSWORD}"
			def customImage = docker.build("${imageName}:${pom.version}")
			customImage.push()
			}
			}
    }
    stage('Deploy') {
        	dir("k8s"){
        	sh "sed -i 's/IMAGE_URL/${pom.version}/g' ${k8sName}-deployment.yaml"
			sh "cat ${k8sName}-deployment.yaml"
			
			// cloud
			withAWS(credentials:'jenkins-aws', region:'us-east-1') {
			    sh "aws sts get-caller-identity"
        	  //  sh "aws eks update-kubeconfig --name eks --region us-east-1"
              //  sh "kubectl apply -f ./${k8sName}-deployment.yaml -n default --validate=false"
				}
			}
    }
}