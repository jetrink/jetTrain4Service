import template from './manager.publicProfile.html';

export default {
	controller: function ($scope,authenticationService) {
		"ngInject";
		authenticationService.getManagerProfile().then(function (response) {
			$scope.manager = response.data;
		})
	},
	template: template
}
