import template from './tutor.publicProfile.html';

export default {
	controller: function ($scope,authenticationService) {
		"ngInject";
		authenticationService.getTutorProfile().then(function (response) {
			$scope.tutor = response.data.person;
			$scope.tutor.subject = response.data.subject;
			$scope.tutor.schedule = response.data.schedule;
		})
	},
	template: template
}
