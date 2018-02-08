import template from './student.editProfile.html';

export default {
	controller: function ($scope, authenticationService, $state) {
		"ngInject";
		authenticationService.getStudentProfile().then(function (response) {
			$scope.student = response.data.person;
			$scope.student.gradeLevel = response.data.gradeLevel;
		})

		$scope.states = ['Alabama','Alaska','American Samoa','Arizona','Arkansas','California','Colorado','Connecticut','Delaware','District of Columbia','Federated States of Micronesia','Florida','Georgia','Guam','Hawaii','Idaho','Illinois','Indiana','Iowa','Kansas','Kentucky','Louisiana','Maine','Marshall Islands','Maryland','Massachusetts','Michigan','Minnesota','Mississippi','Missouri','Montana','Nebraska','Nevada','New Hampshire','New Jersey','New Mexico','New York','North Carolina','North Dakota','Northern Mariana Islands','Ohio','Oklahoma','Oregon','Palau','Pennsylvania','Puerto Rico','Rhode Island','South Carolina','South Dakota','Tennessee','Texas','Utah','Vermont','Virgin Island','Virginia','Washington','West Virginia','Wisconsin','Wyoming', 'International']


		$scope.update = function () {
			var data = {
				firstName: $scope.student.firstName,
				lastName: $scope.student.lastName,
				dateOfBirth: $scope.student.dateOfBirth,
				gradeLevel: $scope.student.gradeLevel,
				streetAddress: $scope.student.streetAddress,
				city: $scope.student.city,
				state: $scope.student.state,
				country: $scope.student.country,
				postalCode: $scope.student.postalCode
			}
			console.log(data);
			authenticationService.updateStudent(data).then(function () {
				alert("successfully updated!");
				$state.go("app.account.profile.list");
			}, function () {
				alert("Unable to update profile at this moment");
			})
		}

	},
	template: template
}