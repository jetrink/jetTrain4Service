import template from './manager.dashboard.html';

export default {
	controller: function ($scope, authenticationService, $window, $state) {
		"ngInject";

		$scope.student = {
			firstName: '',
			lastName: '',
			dateOfBirth: new Date(),
			streetAddress: '',
			city: '',
			state: '',
			postalCode: ''
		};

		$scope.states = ['Alabama', 'Alaska', 'American Samoa', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware', 'District of Columbia', 'Federated States of Micronesia', 'Florida', 'Georgia', 'Guam', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Marshall Islands', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota', 'Northern Mariana Islands', 'Ohio', 'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virgin Island', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming', 'International']

		$scope.minDate = new Date(
			$scope.student.dateOfBirth.getFullYear() - 100,
			$scope.student.dateOfBirth.getMonth(),
			$scope.student.dateOfBirth.getDate()
		);

		$scope.maxDate = new Date(
			$scope.student.dateOfBirth.getFullYear(),
			$scope.student.dateOfBirth.getMonth(),
			$scope.student.dateOfBirth.getDate()
		);
		$scope.show = true;
		$scope.addForm = function () {
			$scope.show = false;
		}
		$scope.cancel = function () {
			$scope.show = true;
			$scope.student = {};
		}

		authenticationService.getManagerDashboard().then(function (response) {
			$scope.manager = response.data;
		})

		$scope.completeProfile = function () {
			authenticationService.setUpStudent($scope.student).then(function (response) {
				$scope.show = !$scope.show;
				alert("Successfully added student");
				$window.location.reload();
				$scope.student = {};
			}, function (err) {
				alert("Failed to add student");
			})
		}

		$scope.enter = function (sess) {
			$state.go("app.session.student", {sessionId: sess.id});
		};

		$scope.endSession = function (sess) {
			authenticationService.endSession(sess.id);
		};

		$scope.canJoin = function (sess) {

			if (sess.status === 'ENDED') {
				return false;
			}

			let today = new Date();
			if ((new Date(sess.startDateTime).getHours() - today.getHours() === 0 && new Date(sess.startDateTime).getMinutes() - today.getMinutes() > 5) || new Date(sess.startDateTime).getHours() - today.getHours() != 0) {
				return false;
			}

			//TODO: more validation?

			return true;
		}
	},
	template: template
}
