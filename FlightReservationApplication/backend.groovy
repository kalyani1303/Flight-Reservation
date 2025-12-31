pipeline{
    agent any 
    stages{
        stage('Code-pull'){
            steps{
                git branch: 'main', url: 'https://github.com/kalyani1303/Flight-Reservation.git'
            }
        }
        stage('Code-build'){
            steps{
                sh '''
                cd FlightReservationApplication
                mvn clean package
                '''
            }
        }
        stage('QA-TEST'){
            steps{
                withSonarQubeEnv(installationName:'sonar', credentialsId: 'Sonar-token') {
                    sh '''
                        cd FlightReservationApplication
                        mvn sonar:sonar -Dsonar.projectKey=flight-reservation
                    '''
                }
            }     
        }
        stage('Docker-build'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    docker build . -t kalyani1303/flightreservation-new:latest
                    docker push kalyani1303/flightreservation-new:latest
                    docker rmi kalyani1303/flightreservation-new:latest
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    kubectl apply -f k8s/
                '''
            }
        }
    }
}