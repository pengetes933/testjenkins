pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'tixfest-api'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        GITHUB_REPO = 'your-github-username/your-repo-name' // Replace with your actual GitHub repo
        DOCKER_REGISTRY_CREDENTIALS = 'docker-registry-credentials' // Set up in Jenkins credentials
        GITHUB_CREDENTIALS = 'github-credentials' // Set up in Jenkins credentials
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://your-docker-registry-url', DOCKER_REGISTRY_CREDENTIALS) {
                        dockerImage.push("${DOCKER_TAG}")
                        dockerImage.push("latest")
                    }
                }
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                script {
                    // Back up existing docker-compose.yml if it exists
                    sh 'if [ -f docker-compose.yml ]; then cp docker-compose.yml docker-compose.yml.backup; fi'
                    
                    // Update docker-compose.yml with the new image tag
                    sh """
                    cat > docker-compose.yml << EOF
services:
  app:
    image: your-docker-registry-url/${DOCKER_IMAGE}:${DOCKER_TAG}
    ports:
      - "8443:8443"
    depends_on:
      - db
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/tixfest_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      REDIS_HOST: redis
      REDIS_PORT: 6379
      REDIS_PASSWORD: 
      PASSWORD_SUPER_ADMIN: tixfestbergembira
      EMAIL_SUPER_ADMIN: b4mb4ng.hiruzen@gmail.com
      JWT_SECRET: \${JWT_SECRET}
      JWT_EXPIRATION_IN_MINUTES_ACCESS_TOKEN: 5
      REFRESH_TOKEN_EXPIRATION_IN_HOUR: 24
      JWT_ISSUER: "Tixfest API"
      AES_SECRET: \${AES_SECRET}
      MIDTRANS_CLIENT_KEY: \${MIDTRANS_CLIENT_KEY}
      MIDTRANS_SERVER_KEY: \${MIDTRANS_SERVER_KEY}
      MIDTRANS_ISPRODUCTION: "false"
      MIDTRANS_MERCHANT_ID: \${MIDTRANS_MERCHANT_ID}
      MAIL_SMTP_USERNAME: \${MAIL_SMTP_USERNAME}
      MAIL_SMTP_PASSWORD: \${MAIL_SMTP_PASSWORD}
      STORAGE_URL: \${STORAGE_URL}
      STORAGE_TOKEN: \${STORAGE_TOKEN}
      STORAGE_FILE_MAX_SIZE: 2097152
      SERVER_SSL_KEY_STORE: file:/app/keystore.jks
      SERVER_SSL_KEY_STORE_PASSWORD: \${SSL_KEY_STORE_PASSWORD}
  db:
    image: postgres:13-alpine
    environment:
      POSTGRES_DB: tixfest_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    volumes:
      - postgres_data:/var/lib/postgresql/data
  redis:
    image: redis:6-alpine
    volumes:
      - redis_data:/data
volumes:
  postgres_data:
  redis_data:
EOF
                    """
                    
                    // Deploy using docker-compose
                    sh 'docker-compose down || true'
                    sh 'docker-compose up -d'
                }
            }
        }
    }
    
    post {
        success {
            echo 'Build and deployment successful!'
            // You can add notifications here (email, Slack, etc.)
        }
        failure {
            echo 'Build or deployment failed!'
            // You can add notifications here (email, Slack, etc.)
        }
        always {
            // Clean up Docker images to avoid filling up disk space
            sh 'docker system prune -f'
        }
    }
}