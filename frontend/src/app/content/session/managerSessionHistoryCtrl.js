import template from "./manager.sessionHistory.html";


export default {
    controller: function ($scope, authenticationService) {
        "ngInject";
        $scope.parent={};
        authenticationService.getManagerDashboard().then(function (response) {
			$scope.parent.students = response.data.managedStudents;
		})
        $scope.getRequest = function (id) {
            authenticationService.getManagerSessionHistory(id).then(function (response) {
                $scope.students = response.data;
			},function (err) {
				alert("Failed to retrieve requests")
			})
		}

    },
    template:template
}