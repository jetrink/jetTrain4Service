import template from './student.profileSetup.html';

export default {
	controller: function ($scope, authenticationService, $state) {
		"ngInject";
		$scope.student = {
			firstName: '',
			lastName: '',
			dateOfBirth: new Date("11/22/2004"),
			gradeLevel:1,
			streetAddress: '',
			city: '',
			state: '',
			postalCode: '',
			dob:new Date()
		}

		$scope.states = ['Alabama','Alaska','American Samoa','Arizona','Arkansas','California','Colorado','Connecticut','Delaware','District of Columbia','Federated States of Micronesia','Florida','Georgia','Guam','Hawaii','Idaho','Illinois','Indiana','Iowa','Kansas','Kentucky','Louisiana','Maine','Marshall Islands','Maryland','Massachusetts','Michigan','Minnesota','Mississippi','Missouri','Montana','Nebraska','Nevada','New Hampshire','New Jersey','New Mexico','New York','North Carolina','North Dakota','Northern Mariana Islands','Ohio','Oklahoma','Oregon','Palau','Pennsylvania','Puerto Rico','Rhode Island','South Carolina','South Dakota','Tennessee','Texas','Utah','Vermont','Virgin Island','Virginia','Washington','West Virginia','Wisconsin','Wyoming', 'International']

		$scope.minDate = new Date(
			$scope.student.dateOfBirth.getFullYear()-100,
			$scope.student.dateOfBirth.getMonth(),
			$scope.student.dateOfBirth.getDate()
		);

		$scope.maxDate = new Date(
			$scope.student.dob.getFullYear()-13,
			$scope.student.dob.getMonth(),
			$scope.student.dob.getDate()
		);

		$scope.completeProfile = function () {
			var data = {
				firstName: $scope.student.firstName,
				lastName: $scope.student.lastName,
				dateOfBirth: new Date($scope.student.dateOfBirth.getTime()),
				gradeLevel:$scope.student.gradeLevel,
				streetAddress: $scope.student.streetAddress,
				city: $scope.student.city,
				state: $scope.student.state,
				country: $scope.student.country,
				postalCode: $scope.student.postalCode
			}
			authenticationService.studentProfileSetup(data).then(function (response) {
				authenticationService.setUserToken(response,false);
				$state.go('app.account.profile.list');
			}, function (err) {
				alert("Could not setup account. logging out. please login and try again");
				authenticationService.logout();
			})
		}

	},
	template: template
}