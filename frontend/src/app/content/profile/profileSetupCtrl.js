import template from './initialProfileSetup.html';

export default {
	controller: function ($scope, $state, authenticationService) {
		"ngInject";

		$scope.logout = function () {
			authenticationService.logout();
			$state.go('app.auth');
		}
	},
	template: template
}