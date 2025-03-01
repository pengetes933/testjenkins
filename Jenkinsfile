pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'tixfest-api'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        GITHUB_REPO = 'https://github.com/pengetes933/testjenkins' // Ganti dengan repo GitHub Anda
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.8.6-eclipse-temurin-17-alpine'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Unit Tests') {
            agent {
                docker {
                    image 'maven:3.8.6-eclipse-temurin-17-alpine'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                script {
                    // Menggunakan sh untuk menjalankan docker-compose
                    sh '''
                    if [ -f docker-compose.yml ]; then
                        cp docker-compose.yml docker-compose.yml.backup
                    fi
                    '''
                    
                    // Skip Docker commands jika Docker tidak tersedia
                    sh '''
                    if command -v docker &> /dev/null; then
                        docker-compose down || true
                        docker-compose up -d
                    else
                        echo "Docker not available - skipping deployment"
                    fi
                    '''
                }
            }
        }
    }
    
    post {
        success {
            echo 'Build and deployment successful!'
        }
        failure {
            echo 'Build or deployment failed!'
        }
        always {
            script {
                // Skip Docker commands jika Docker tidak tersedia
                sh '''
                if command -v docker &> /dev/null; then
                    docker system prune -f || true
                else
                    echo "Docker not available - skipping cleanup"
                fi
                '''
            }
        }
    }
}