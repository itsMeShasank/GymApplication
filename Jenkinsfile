pipeline {
	agent any

	stages {
		stage("Build Project") {
			steps {
				script {
					def modules = ['GymService', 'GymReportService','NotificationService','EurekaServer','GatewayServer','AuthenticationService','GymCommons']
					for (def module in modules) {
						dir("${module}") {
							echo "Building ${module}..."
							bat "mvn clean install"
						}
					}
				}
			}
		}
		stage("SonarQube Scanner") {
		    steps {
		    bat "mvn sonar:sonar -Dsonar.projectKey=Gym-Application -Dsonar.projectName='Gym-Application' -Dsonar.host.url=http://localhost:9000 -Dsonar.token=sqp_412629cc903949528b74f1608e4606eb75d871e5"
		    }
		}
	}
}