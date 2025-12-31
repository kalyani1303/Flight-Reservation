pipeline{
    agent any
    stages{
        stage('code-pull'){
            steps{
                git branch: 'main', url: 'https://github.com/kalyani1303/Flight-Reservation.git'
            }
        }
        stage('build'){
            steps{
                sh '''
                    cd frontend
                    npm install
                    npm run build
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh '''
                    cd frontend
                    aws s3 sync dist/ s3://cbz-frontend-project-bux-013/
                '''
            }
        }
    }
}