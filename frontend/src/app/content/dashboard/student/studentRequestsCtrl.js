import template from "./student.requests.html";

export default {
	controller: function ($scope, authenticationService) {
		"ngInject";
		authenticationService.getStudentRequests().then(function (response) {
			$scope.errorMsg = '';
			console.log(response.data);
			angular.forEach(response.data, function (obj) {
				obj.requestDate.toLocaleString();
			})
			console.log(response.data);
			$scope.studentSessionRequests = response.data;
		}, function () {
			$scope.errorMsg = "Unable to retrieve your requests at this moment";
		})

	},
	template: template
}