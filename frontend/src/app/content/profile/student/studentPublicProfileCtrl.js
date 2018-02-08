import template from './student.publicProfile.html';

export default {
	controller: function ($scope,authenticationService) {
"ngInject";
		authenticationService.getStudentProfile().then(function (response) {
			$scope.student = response.data.person;
			$scope.student.gradeLevel = response.data.gradeLevel;
		})
	},
	template: template
}