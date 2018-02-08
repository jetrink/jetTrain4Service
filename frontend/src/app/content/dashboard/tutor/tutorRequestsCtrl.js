import template from "./tutor.requests.html";

export default {
	controller: function ($scope, authenticationService) {
		"ngInject";

		$scope.tutorSessionRequests = {
			pending: [],
			req: []
		};

		function loadData() {
			authenticationService.getTutorRequests().then(function (response) {
				$scope.errorMsg = '';

				// clear them first.
				$scope.tutorSessionRequests.pending = [];
				$scope.tutorSessionRequests.req = [];

				for (let sr of response.data) {
					if (sr.status === "PENDING") {
						$scope.tutorSessionRequests.pending.push(sr);
					} else {
						$scope.tutorSessionRequests.req.push(sr);
					}
				}
				console.log($scope.tutorSessionRequests)
			}, function () {
				$scope.errorMsg = "Unable to retrieve your requests at this moment";
			});
		}

		loadData();

		$scope.decline = function (id) {
			let data = {
				requestId: id,
				status: "DECLINED"
			};
			authenticationService.respondToRequest(data).then(function (response) {
				$scope.tutorSessionRequests = response.data;
				alert("successfully declined request!")
				loadData(); // again.
			}, function () {
				$scope.errorMsg = "Failed to decline request";
			})
		};

		$scope.approve = function (id) {
			let data = {
				requestId: id,
				status: "APPROVED"
			};
			authenticationService.respondToRequest(data).then(function (response) {
				$scope.tutorSessionRequests = response.data;
				alert("successfully approved request!")
				loadData(); // again.
			}, function () {
				$scope.errorMsg = "Failed to approve request";
			})
		}

	},
	template: template
}
