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
	}
}