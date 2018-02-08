import template from "./manager.requests.html";

export default {
	controller: function ($scope, authenticationService) {
		"ngInject";

		$scope.parent={

		}
		authenticationService.getManagerDashboard().then(function (response) {
			$scope.students = response.data.managedStudents;
		})
		$scope.getRequest = function (id) {
			authenticationService.getManagerRequest(id).then(function (response) {
				$scope.errorMsg = '';
				angular.forEach(response.data, function (obj) {
					obj.requestDate.toLocaleString();
				})
				$scope.studentRequests = response.data;
			}, function () {
				$scope.errorMsg = "Unable to retrieve your requests at this moment";
			})
		}

	},
	template: template
}