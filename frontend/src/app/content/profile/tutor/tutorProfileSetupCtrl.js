import template from "./tutor.profileSetup.html";

export default {
	controller: function ($scope, authenticationService, $state) {
		"ngInject";
		$scope.time=["All day","Morning","Afternoon","Evening","Midnight - 6:00am"];

		$scope.schedules=[
		{day:"MONDAY"},
			{day:"TUESDAY"},
			{day:"WEDNESDAY"},
			{day:"THURSDAY"},
			{day:"FRIDAY"},
			{day:"SATURDAY"},
			{day:"SUNDAY"}
		]
		$scope.states = ['Alabama','Alaska','American Samoa','Arizona','Arkansas','California','Colorado','Connecticut','Delaware','District of Columbia','Federated States of Micronesia','Florida','Georgia','Guam','Hawaii','Idaho','Illinois','Indiana','Iowa','Kansas','Kentucky','Louisiana','Maine','Marshall Islands','Maryland','Massachusetts','Michigan','Minnesota','Mississippi','Missouri','Montana','Nebraska','Nevada','New Hampshire','New Jersey','New Mexico','New York','North Carolina','North Dakota','Northern Mariana Islands','Ohio','Oklahoma','Oregon','Palau','Pennsylvania','Puerto Rico','Rhode Island','South Carolina','South Dakota','Tennessee','Texas','Utah','Vermont','Virgin Island','Virginia','Washington','West Virginia','Wisconsin','Wyoming', 'International']


		var subjectIds = [];
		$scope.expertise = {
			subjects: []
		}
		$scope.other = {};

		$scope.showit = false;

		$scope.submit = function () {
			//sends to admin for approval.
			if ($scope.tutor.firstName && $scope.tutor.lastName && $scope.tutor.dateOfBirth && $scope.tutor.streetAddress &&
				$scope.tutor.city && $scope.tutor.state && $scope.tutor.country && $scope.tutor.postalCode && $scope.tutor.expYears &&
				$scope.other.field && $scope.other.subject && $scope.other.description) {
				alert("submitted subject suggestion for admin review");
				$scope.showit = false;
				$scope.other.field = '';
				$scope.other.subject = '';
				$scope.other.description = '';
			} else {
				alert("Please fill out profile");
				$scope.showit = false;
			}


		}


		authenticationService.getAllSubjects().then(function (response) {
			var newData = {};
			$scope.expertise.fields = [];
			$scope.expertise.fields.push(response.data[0]);
			angular.forEach(response.data, function (r) {
				var j = $scope.expertise.fields.length;
				for (var i = 0; i < j; i++) {
					if (r.field == $scope.expertise.fields[i].field) {
						console.log("not adding");
						break;
					}
					if (i == $scope.expertise.fields.length - 1) {
						$scope.expertise.fields.push(r);
					}
				}

			})
		})

		$scope.getFieldSubjects = function (field) {
			var temp = [];
			var list = [];
			authenticationService.getSubjects(field).then(function (response) {
				angular.forEach(response.data, function (record) {
					for (var i = 0; i < $scope.tutor.expertise.length; i++) {
						if ($scope.tutor.expertise[i].id == record.id) {
							temp.push(record);
						}
					}
					if (temp.length == 0) {
						list.push(record);
					}
					temp = [];
				})
				$scope.expertise.subjects = list;
			})
		}
		$scope.tutor = {
			firstName: '',
			lastName: '',
			dateOfBirth: new Date(),
			streetAddress: '',
			city: '',
			state: '',
			postalCode: '',
			expYears: '',
			expertise: []
		}

		$scope.minDate = new Date(
			$scope.tutor.dateOfBirth.getFullYear() - 100,
			$scope.tutor.dateOfBirth.getMonth(),
			$scope.tutor.dateOfBirth.getDate()
		);

		$scope.maxDate = new Date(
			$scope.tutor.dateOfBirth.getFullYear()-13,
			$scope.tutor.dateOfBirth.getMonth(),
			$scope.tutor.dateOfBirth.getDate()
		);

		$scope.addNew = function () {
			subjectIds.push($scope.selectedSubject.id);
			$scope.tutor.expertise.push({
				id: $scope.selectedSubject.id,
				field: $scope.selectedField.field,
				subject: $scope.selectedSubject.name,
				description: $scope.selectedSubject.description
			});
			$scope.selectedField = null;
			$scope.selectedSubject = null;
		};


		$scope.remove = function (e) {
			var newDataList = [];
			angular.forEach($scope.tutor.expertise, function (selected) {
				if (selected.subject != e.subject) {
					newDataList.push(selected);
				}
			});
			$scope.tutor.expertise = newDataList;
		};

		$scope.complete = function () {
			//console.log($scope.schedules);
			var schedule=[];
			var wrong = false;
			angular.forEach($scope.schedules,function (s) {
				if (s.selected) {
					if(s.start!==null && s.end !== null){
						if(s.start.getTime()>=s.end.getTime()){
							wrong = true;
						}
						else{
							var sc ={
								day:s.day,
								time:s.start.toLocaleTimeString()+" to "+s.end.toLocaleTimeString()
							}
							schedule.push(sc);
						}
					}
					else{
						wrong = true;
					}

				}
			})

			if(schedule.length < 1 || wrong){
				alert("Please setup a schedule or a proper time interval");
			}
			else{
				var data = {
					firstName: $scope.tutor.firstName,
					lastName: $scope.tutor.lastName,
					dateOfBirth: $scope.tutor.dateOfBirth,
					streetAddress: $scope.tutor.streetAddress,
					city: $scope.tutor.city,
					state: $scope.tutor.state,
					country: $scope.tutor.country,
					postalCode: $scope.tutor.postalCode,
					expYears: $scope.tutor.expYears,
					expertise: subjectIds,
					schedule:schedule
				}

				authenticationService.tutorProfileSetup(data).then(function (response) {
					authenticationService.setUserToken(response);
					authenticationService.setUserInfo(response.data);
					$state.go('app.account.profile.list');
				}, function (err) {
					alert("Profile could not be setup please try again.");
				})
				console.log(data);
			}

		}
	},
	template: template

}
