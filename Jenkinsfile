pipeline {
    agent { label 'campus-slave' }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser to run tests on')
        choice(name: 'SUITE_XML_FILE', choices: ['testng-regression.xml', 'testng-smoke.xml', 'testng-selenide.xml', 'testng.xml'], description: 'TestNG suite to run')
        choice(name: 'ENV', choices: ['ci', 'local'], description: 'Environment config to use')
    }

    triggers {
        pollSCM('H/5 * * * *')
        cron('H 0 * * *')
    }

    stages{
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/eduardorichards/campusautomation-v3.git'
            }
        }

        stage('Run Tests') {
            steps {
                sh "mvn clean test -Denv=${params.ENV} -Dsuite.xml.file=${params.SUITE_XML_FILE} -Dbrowser=${params.BROWSER}"

            }
        }

        stage('Publish Reports') {
            steps {
                junit 'target/surefire-reports/TEST-*.xml'
                archiveArtifacts artifacts: 'target/screenshots/**', allowEmptyArchive: true
            }
        }
    }
}