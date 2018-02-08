import template from './tutor.editProfile.html';

export default {
	controller: function ($scope, authenticationService, $state) {
		"ngInject";
		$scope.states = ['Alabama', 'Alaska', 'American Samoa', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware', 'District of Columbia', 'Federated States of Micronesia', 'Florida', 'Georgia', 'Guam', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Marshall Islands', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota', 'Northern Mariana Islands', 'Ohio', 'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virgin Island', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming', 'International']


		authenticationService.getTutorProfile().then(function (response) {
			$scope.tutor = response.data.person;


		})

		$scope.update = function () {
			var data = {
				firstName: $scope.tutor.firstName,
				lastName: $scope.tutor.lastName,
				dateOfBirth: $scope.tutor.dateOfBirth,
				expertise: $scope.tutor.subject,
				schedule: $scope.tutor.schedule,
				streetAddress: $scope.tutor.streetAddress,
				city: $scope.tutor.city,
				state: $scope.tutor.state,
				country: $scope.tutor.country,
				postalCode: $scope.tutor.postalCode
			}
			authenticationService.updateTutor(data).then(function () {
				alert("successfully updated!");
				$state.go("app.account.profile.list");
			},
				function () {
					alert("unable to update profile at this moment");
				})
		}

	},
	template: template
}
