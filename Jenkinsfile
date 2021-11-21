#!/usr/bin/env groovy
node {
    def mvnHome
    def javaHome
    mvnHome = tool 'maven3.6.3'
    javaHome = tool 'jdk1.8'
    env.PATH = "${javaHome}/bin:${mvnHome}/bin;${env.PATH}"

    stage('git clone') { // for display purposes
        // Get some code from a GitHub repository
        git credentialsId: 'lxyeah', url: 'http://212.129.149.40/181250092_seiiimaskon/backend-seiiiassignment.git'
        // Get the Maven tool.
        // ** NOTE: This 'M3' Maven tool must be configured
        // **       in the global configuration.

    }
    stage('Test') {

        sh 'cd ./SpringbootDemo&&mvn clean test -Dmaven.test.failure.igonre=true -DfailIfNoTests=false'


                jacoco (
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '',
                        inclusionPattern: '',
                )

    }

    stage('Build') {

        echo "build start"
        echo "cd /SpringbootDemo"
        sh 'pwd'

        sh 'cd ./SpringbootDemo&&mvn clean install'


    }

    stage('Develop') {
        echo "develop start"

        sh 'JENKINS_NODE_COOKIE=dontKillMe sh develop.sh'
    }




}