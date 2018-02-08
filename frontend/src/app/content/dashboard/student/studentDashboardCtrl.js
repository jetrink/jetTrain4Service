import template from "./student.dashboard.html";

export default {
	controller: function ($scope, authenticationService, $state) {
		"ngInject";

		$scope.student = {};

		reloadData();

		function reloadData() {
			authenticationService.getStudentDashboard().then(function (response) {
				$scope.student = response.data;
			});
		}

		$scope.enter = function (sess) {
			$state.go("app.session.student", {sessionId: sess.id});
		};

		$scope.endSession = function (sess) {
			authenticationService.endSession(sess.id).then(reloadData);
		};

		$scope.canJoin = function (sess) {

			if (sess.status === 'ENDED') {
				return false;
			}

			let today = new Date();
			// console.log(new Date(sess.startDateTime).getMinutes() - today.getMinutes());
			if ((new Date(sess.startDateTime).getHours() - today.getHours()== 0 && new Date(sess.startDateTime).getMinutes() - today.getMinutes() > 5)||new Date(sess.startDateTime).getHours() - today.getHours()>0) {
				return false;
			}

			//TODO: more validation?

			return true;
		}
	},
	template: template
}
