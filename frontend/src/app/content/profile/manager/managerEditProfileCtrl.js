import template from './manager.editProfile.html';

export default {
	controller: function ($scope, authenticationService, $state) {
		"ngInject";
		authenticationService.getManagerProfile().then(function (response) {
			$scope.manager = response.data;
		})

		$scope.states = ['Alabama', 'Alaska', 'American Samoa', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware', 'District of Columbia', 'Federated States of Micronesia', 'Florida', 'Georgia', 'Guam', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Marshall Islands', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota', 'Northern Mariana Islands', 'Ohio', 'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virgin Island', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming', 'International']


		$scope.update = function () {
			let data = {
				firstName: $scope.manager.firstName,
				lastName: $scope.manager.lastName,
				dateOfBirth: $scope.manager.dateOfBirth,
				gradeLevel: $scope.manager.gradeLevel,
				streetAddress: $scope.manager.streetAddress,
				city: $scope.manager.city,
				state: $scope.manager.state,
				country: $scope.manager.country,
				postalCode: $scope.manager.postalCode
			};
			authenticationService.updateManager(data).then(function () {
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