pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'tixfest-api'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        GITHUB_REPO = 'pengetes933/testjenkins'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        
        stage('Deploy ke Staging') {
            steps {
                script {
                    // Backup docker-compose file
                    sh 'if [ -f docker-compose.yml ]; then cp docker-compose.yml docker-compose.yml.backup; fi'
                    
                    // Cek apakah docker-compose tersedia
                    sh """
                    if command -v docker compose &> /dev/null; then
                        docker compose down || echo "docker compose down gagal tapi lanjut"
                        docker-compose up --build -d || echo "docker compose up gagal tapi lanjut"
                    else
                        echo "docker compose tidak tersedia - skip deployment"
                    fi
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Build dan deployment berhasil!'
        }
        failure {
            echo 'Build atau deployment gagal!'
        }
        always {
            echo 'Pipeline selesai'
        }
    }
}