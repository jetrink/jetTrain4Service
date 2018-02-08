import template from "./profile.html";

export default {
	controller: function ($scope, authenticationService,$window) {
		"ngInject";



		var subjectIds = [];
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
		$scope.expertise = {
			subjects: []
		}
		$scope.show = true;

		$scope.change=function () {
			$scope.show = !$scope.show;
		}

		authenticationService.getSelf().then(function (response) {
			$scope.showProfile = {
				student: response.data.studentId,
				tutor: response.data.tutorId,
				manager: response.data.studentManager
			};
		})



		authenticationService.getProfile().then(function (response) {
			$scope.student = response.data;
			$scope.tutor = response.data;
			$scope.manager = response.data;
			//$scope.student.gradeLevel = response.data.gradeLevel;
		})


		$scope.studentsetup = function () {
			var data = {
				firstName: $scope.student.firstName,
				lastName: $scope.student.lastName,
				dateOfBirth: $scope.student.dateOfBirth,
				streetAddress: $scope.student.streetAddress,
				city: $scope.student.city,
				state: $scope.student.state,
				country: $scope.student.country,
				postalCode: $scope.student.postalCode
			}
			authenticationService.studentProfileSetup(data).then(function (response) {
				authenticationService.setUserToken(response,false);
				$scope.show =true;
				$window.location.reload();
			}, function (err) {
				console.log("error: " + err.data);
				alert("unable to setup up another profile");
				$scope.show =true;
			})
		}




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
					if($scope.tutor.expertise){
						for (var i = 0; i < $scope.tutor.expertise.length; i++) {
							if ($scope.tutor.expertise[i].id == record.id) {
								temp.push(record);
							}
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

		$scope.addNew = function (s) {
			subjectIds.push(s.id);
			$scope.newlist.push({
				id: s.id,
				field: s.field,
				subject: s.name,
				description: s.description
			});
			$scope.tutor.expertise =  $scope.newlist;
			$scope.selectedField = "";
			$scope.selectedSubject = "";
		};

$scope.newlist=[];
		$scope.remove = function (e) {
			var newDataList = [];
			angular.forEach($scope.tutor.expertise, function (selected) {
				if (selected.subject != e.subject) {
					newDataList.push(selected);
				}
			});
			$scope.tutor.expertise = newDataList;
			$scope.newlist = $scope.tutor.expertise;
		};

		$scope.tutorsetup = function () {
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
					authenticationService.setUserToken(response,false);
					authenticationService.setUserInfo(response.data);
					$scope.show =true;
					$window.location.reload();
				}, function (err) {
					alert("Profile could not be setup please try again.");
					$scope.show =true;
				})
			}

		}



		$scope.manager = {
			firstName: '',
			lastName: '',
			dateOfBirth: new Date(),
			streetAddress: '',
			city: '',
			state: '',
			postalCode: ''
		}

		$scope.managersetup = function () {
			var data = {
				firstName: $scope.manager.firstName,
				lastName: $scope.manager.lastName,
				dateOfBirth: $scope.manager.dateOfBirth,
				streetAddress: $scope.manager.streetAddress,
				city: $scope.manager.city,
				state: $scope.manager.state,
				country: $scope.manager.country,
				postalCode: $scope.manager.postalCode
			}
			authenticationService.managerProfileSetup(data).then(function () {
				authenticationService.setUserToken(response,false);
				$scope.show =true;
				$window.location.reload();
			}, function (err) {
				alert("Profile could not be setup please try again.");
				$scope.show =true;
			})
		}






	},
	template: template
}
