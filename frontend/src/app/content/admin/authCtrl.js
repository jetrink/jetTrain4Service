import template from "./auth.html";

export default {
	controller: function ($scope, authenticationService, $state) {
		"ngInject";
		console.log("hey");
		$scope.loginAdmin = function () {
			console.log("here");
			var data = {
				username: $scope.admin.username,
				password: $scope.admin.password
			}
			authenticationService.loginAdmin(data).then(function (response) {
				authenticationService.setUserToken(response, true);
				alert("logged in successfully");
				$state.go("app.admin.home");
				$scope.admin = {}
			},function () {
				alert("Failed to login");
			})
		}
	},
	template: template
}