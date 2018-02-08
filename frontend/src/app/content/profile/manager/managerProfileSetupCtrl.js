import template from './manager.profileSetup.html';

export default {
	controller: function ($scope,authenticationService,$state) {
		"ngInject";
		$scope.manager = {
			firstName: '',
			lastName: '',
			dateOfBirth: new Date("11/22/2004"),
			streetAddress: '',
			city: '',
			state: '',
			postalCode: '',
			dob:new Date()
		}
		$scope.states = ['Alabama','Alaska','American Samoa','Arizona','Arkansas','California','Colorado','Connecticut','Delaware','District of Columbia','Federated States of Micronesia','Florida','Georgia','Guam','Hawaii','Idaho','Illinois','Indiana','Iowa','Kansas','Kentucky','Louisiana','Maine','Marshall Islands','Maryland','Massachusetts','Michigan','Minnesota','Mississippi','Missouri','Montana','Nebraska','Nevada','New Hampshire','New Jersey','New Mexico','New York','North Carolina','North Dakota','Northern Mariana Islands','Ohio','Oklahoma','Oregon','Palau','Pennsylvania','Puerto Rico','Rhode Island','South Carolina','South Dakota','Tennessee','Texas','Utah','Vermont','Virgin Island','Virginia','Washington','West Virginia','Wisconsin','Wyoming', 'International']


		$scope.minDate = new Date(
			$scope.manager.dateOfBirth.getFullYear() - 100,
			$scope.manager.dateOfBirth.getMonth(),
			$scope.manager.dateOfBirth.getDate()
		);

		$scope.maxDate = new Date(
			$scope.manager.dob.getFullYear()-13,
			$scope.manager.dob.getMonth(),
			$scope.manager.dob.getDate()
		);
		
		/*$scope.addStudent = function () {
			$scope.manager.manages.push({ studentFirstName:'', studentLastName: '', studentEmail:''});
		}
		$scope.remove = function(managed){
			managed.selected = true;
			var newList=[];
			angular.forEach($scope.manager.manages, function (m) {
				if(!m.selected){
					newList.push(m);
				}
			});
			$scope.manager.manages = newList;
		};*/

		$scope.completeProfile = function () {
			var data = {
				firstName: $scope.manager.firstName,
				lastName: $scope.manager.lastName,
				dateOfBirth: new Date($scope.manager.dateOfBirth.getTime()),
				streetAddress: $scope.manager.streetAddress,
				city: $scope.manager.city,
				state: $scope.manager.state,
				country: $scope.manager.country,
				postalCode: $scope.manager.postalCode
			}
			authenticationService.managerProfileSetup(data).then(function () {
				$state.go('app.account.profile.list');
			}, function (err) {
				console.log("error: " + err.data);
				authenticationService.logout();
			})
		}

	},
	template: template
}