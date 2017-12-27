pipeline {
    environment {
        SAUCE_ACCESS     = credentials('test')
        test = 'blablable'
    }

    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Hello') {
            steps {
                sh 'chmod +x ./jenkins/scripts/deliver.sh'
                sh './jenkins/scripts/deliver.sh ${SAUCE_ACCESS_USR} ${SAUCE_ACCESS} ${SAUCE_ACCESS_PSW}'
                sh 'echo ${hello}'
                withCredentials([usernameColonPassword(credentialsId: 'test', variable: 'PW1')]) {
                    echo "My password is '${PW1}'!"
                }
                sh 'echo ${PW1}'
                withCredentials([usernamePassword(credentialsId: 'test', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh 'echo $PASSWORD'
                    echo "$USERNAME"
                }
            }
        }
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
                sh 'chmod +x ./jenkins/scripts/deliver.sh'
                sh './jenkins/scripts/deliver.sh ${SAUCE_ACCESS_USR} ${SAUCE_ACCESS} ${SAUCE_ACCESS_PSW}'
                sh 'echo ${hello}'
                withCredentials([usernameColonPassword(credentialsId: 'test', variable: 'PW1')]) {
                    echo "My password is '${PW1}'!"
                }
                echo 'AAAAAAAAA'
                withCredentials([usernamePassword(credentialsId: 'test', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh 'echo $PASSWORD'
                    echo "$USERNAME"
                }
            }
        }
    }
}