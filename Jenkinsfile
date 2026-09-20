pipeline {
    agent any
    environment {
        ECR_REGISTRY = "912178478938.dkr.ecr.ap-south-1.amazonaws.com"
        ECR_REPO = "currency-conversion"
        CLUSTER_NAME = "demo-cluster"
        REGION = "ap-south-1"
        NAMESPACE = "currency-conversion"
    }
    stages {
        stage('checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/NaYaGK/currency-conversion-devops.git'
            }
        }
        stage('clean') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('validate') {
            steps {
                sh 'mvn validate'
            }
        }
        stage('test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('sonar scan') {
            steps {
                timeout(time: 1, unit: 'MINUTES')
                withSonarQubeEnv('sonar') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('quality gate') {
            steps {
                timeout(time: 1, unit: 'MINUTES')
                script {
                    def check = waitForQualityGate()
                    if (check.status != 'OK')
                        error "pipeline aborted due to quality gate failure: ${check.status}"
                }
            }
        }
        stage('package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        stage('jfrog upload') {
            steps {
                script {
                    def server = Artifactory.server('jfrog')
                    def uploadSpec = """{
                        "files": [{
                            "pattern": "**/target/*.jar",
                            "target": "currency-conversion-local/"
                        }]
                    }"""
                    server.upload(uploadSpec)
                }
            }
        }
        stage('docker build') {
            steps {
                sh 'docker build -t ${ECR_REPO}:${BUILD_NUMBER} .'
            }
        }
        stage('trivy scan') {
            steps {
                sh '''
                    trivy image \
                    --exit-code 1 \
                    --severity HIGH,CRITICAL \
                    ${ECR_REPO}:${BUILD_NUMBER}
                '''
            }
        }
        stage('ecr push') {
            steps {
                sh '''
                    aws ecr get-login-password --region ${REGION} | \
                    docker login --username AWS \
                    --password-stdin ${ECR_REGISTRY}

                    docker tag ${ECR_REPO}:${BUILD_NUMBER} \
                    ${ECR_REGISTRY}/${ECR_REPO}:${BUILD_NUMBER}

                    docker push \
                    ${ECR_REGISTRY}/${ECR_REPO}:${BUILD_NUMBER}
                '''
            }
        }
        
        stage('deploy to eks') {
            steps {
                sh '''
                    aws eks update-kubeconfig \
                    --name ${CLUSTER_NAME} \
                    --region ${REGION}
                    
                    kubectl apply -f k8s/namespace.yaml    
                    kubectl apply -f k8s/deployment.yaml -n ${NAMESPACE}
                    kubectl apply -f k8s/service.yaml -n ${NAMESPACE}

                    kubectl set image deployment/currency-conversion \
                    currency-conversion=${ECR_REGISTRY}/${ECR_REPO}:${BUILD_NUMBER} \
                    -n ${NAMESPACE}

                    kubectl rollout status deployment/currency-conversion \
                    -n ${NAMESPACE}
                '''
            }
        }
    }
    post {
        failure {
            echo 'pipeline failed'
        }
        success {
            echo 'pipeline succeeded'
        }
    }
}
