import template from "./mainLayout.html";

export default {
	controller: function ($scope, authenticationService, $state) {
		"ngInject";

		authenticationService.isApproved().then(function (response) {
			$scope.messge = response.data.approved;
		})

		authenticationService.getTutorDashboard().then(function (response) {
			if(response.data.isOnline){
				$scope.user.availability = "Available";
			}else{
				$scope.user.availability = "Not Available";
			}
		})
		$scope.user={
		};
		$scope.setAvailability=function (a) {
			var bool = true;
			if(a =="Available"){
				bool = true;
			}
			else{
				bool = false;
			}
			var data={
				available:bool
			}
			console.log(data);
			authenticationService.setAvailability(data).then(function (response) {
				if(response.data.availability){
					$scope.user.availability = "Available";
				}else{
					$scope.user.availability = "Not Available";
				}
			},
				function () {
				console.log("unable to load availability");
					if(bool){
						$scope.user.availability = "Not Available";
					}else{
						$scope.user.availability = "Available";
					}
				})
		}

		authenticationService.getSelf().then(function (response) {
			$scope.showTabs = {
				student: response.data.studentId,
				tutor: response.data.tutorId,
				manager: response.data.studentManager
			};

			$scope.userName = response.data.username;
		})

		$scope.logout = function () {
			authenticationService.logout();
		}
	},
	template: template


};
