pipeline {
    environment {
        TARGET_PATH     = '/home/apps/user'
    }

    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Deliver') {
            steps {
                sh 'chmod +x ./scripts/deliver.sh'
                sh './scripts/deliver.sh ${TARGET_PATH}'
            }
        }
    }
}