import template from './searchTutor.html';

export default {
	controller: function ($scope, authenticationService) {
		"ngInject";

		$scope.textsearch = "";
		$scope.searchCriteria = ["subject", "tutor username", "tutor name"];
		$scope.showsearch = true;
		$scope.search = {
			tutorFirstName: "",
			tutorLastName: "",
			showsearch: true
		}
		$scope.filters = {
			subjects: [],
			availables: []
		}
		$scope.oldresults = [];
		$scope.oldavailables = [];


		$scope.expertise = {}
		authenticationService.getAllSubjects().then(function (response) {
			$scope.expertise.subjects = response.data;
		})

		$scope.filter = function () {
			var newResults = [];
			$scope.filters.results = $scope.results;
			angular.forEach($scope.expertise.subjects, function (sub) {
				if (sub.selected) {
					for (var i = 0; i < $scope.results.length; i++) {
						if (sub.name in $scope.filters.results[i] && !(newResults.includes($scope.filters.results[i]))) {
							newResults.push($scope.filters.results[i]);
						}
					}
				}
			})
			$scope.filters.results = newResults;
		}

		$scope.results = {};
		$scope.hide = true;
		$scope.searchBySubject = function () {
			$scope.filters.availables = [];
			var data = {
				subjectId: $scope.search.subject.id
			}
			authenticationService.findBySubject(data).then(function (response) {
				$scope.results = response.data;
				angular.forEach(response.data, function (data) {
					if (data.available == "true") {
						$scope.filters.availables.push(data);
					}
				})
				var newResults = [];
				angular.forEach($scope.results, function (data) {
					if (!($scope.filters.availables.includes(data))) {
						newResults.push(data);
					}
				})
				$scope.results = newResults;
				$scope.filters.results = $scope.results;

				$scope.oldresults = $scope.filters.results;
				$scope.oldavailables = $scope.filters.availables;
				$scope.hide = false;
			}, function (err) {
				console.log(err);
			})
		}
		$scope.searchByTutor = function () {
			$scope.filters.availables = [];
			var data = {
				username: $scope.search.tutorUsername
			}
			authenticationService.findByTutor(data).then(function (response) {
				$scope.results = response.data;
				angular.forEach(response.data, function (data) {
					if (data.available == "true") {
						$scope.filters.availables.push(data);
					}
				})
				var newResults = [];
				angular.forEach($scope.results, function (data) {
					if (!($scope.filters.availables.includes(data))) {
						newResults.push(data);
					}
				})
				$scope.results = newResults;
				$scope.filters.results = $scope.results;
				$scope.oldresults = $scope.filters.results;
				$scope.oldavailables = $scope.filters.availables;
				$scope.hide = false;
			}, function (err) {
				console.log(err);
			})
		}

		$scope.searchByName = function () {
			$scope.filters.availables = [];
			var data = {
				firstName: $scope.search.tutorFirstName,
				lastName: $scope.search.tutorLastName
			}
			if (data.firstName == null && data.lastName == null) {
				alert("Please fill out at least one of the fields");
			}
			else {
				authenticationService.findByTutorName(data).then(function (response) {
					$scope.results = response.data;
					angular.forEach(response.data, function (data) {
						if (data.available == "true") {
							$scope.filters.availables.push(data);
						}
					})
					var newResults = [];
					angular.forEach($scope.results, function (data) {
						if (!($scope.filters.availables.includes(data))) {
							newResults.push(data);
						}
					})
					$scope.results = newResults;
					$scope.filters.results = $scope.results;
					$scope.oldresults = $scope.filters.results;
					$scope.oldavailables = $scope.filters.availables;
					$scope.hide = false;
				}, function (err) {
					console.log(err);
				})

			}
		}

		$scope.reset = function () {
			angular.forEach($scope.expertise.subjects, function (r) {
				r.selected = false;
			})
			$scope.filters.results = $scope.oldresults;
			$scope.filters.availables = $scope.oldavailables;
		}

		$scope.goToTutorPage = function (r) {
			$scope.search.showsearch = false;
			$scope.tutorUsername = r.username;
			$scope.tutor = {};
			$scope.reg = {
				date: new Date()
			};
			$scope.time = [
				{name: '12:00am', hour: 0, min: 0},
				{name: '12:30am', hour: 0, min: 30},
				{name: '1:00am', hour: 1, min: 0},
				{name: '1:30am', hour: 1, min: 30},
				{name: '2:00am', hour: 2, min: 0},
				{name: '2:30am', hour: 2, min: 30},
				{name: '3:00am', hour: 3, min: 0},
				{name: '3:30am', hour: 3, min: 30},
				{name: '4:00am', hour: 4, min: 0},
				{name: '4:30am', hour: 4, min: 30},
				{name: '5:00am', hour: 5, min: 0},
				{name: '5:30am', hour: 5, min: 30},
				{name: '6:00am', hour: 6, min: 0},
				{name: '6:30am', hour: 6, min: 30},
				{name: '7:00am', hour: 7, min: 0},
				{name: '7:30am', hour: 7, min: 30},
				{name: '8:00am', hour: 8, min: 0},
				{name: '8:30am', hour: 8, min: 30},
				{name: '9:00am', hour: 9, min: 0},
				{name: '9:30am', hour: 9, min: 30},
				{name: '10:00am', hour: 10, min: 0},
				{name: '10:30am', hour: 10, min: 30},
				{name: '11:00am', hour: 11, min: 0},
				{name: '11:30am', hour: 11, min: 30},
				{name: '12:00pm', hour: 12, min: 0},
				{name: '12:30pm', hour: 12, min: 30},
				{name: '1:00pm', hour: 13, min: 0},
				{name: '1:30pm', hour: 13, min: 30},
				{name: '2:00pm', hour: 14, min: 0},
				{name: '2:30pm', hour: 14, min: 30},
				{name: '3:00pm', hour: 15, min: 0},
				{name: '3:30pm', hour: 15, min: 30},
				{name: '4:00pm', hour: 16, min: 0},
				{name: '4:30pm', hour: 16, min: 30},
				{name: '5:00pm', hour: 17, min: 0},
				{name: '5:30pm', hour: 17, min: 30},
				{name: '6:00pm', hour: 18, min: 0},
				{name: '6:30pm', hour: 18, min: 30},
				{name: '7:00pm', hour: 19, min: 0},
				{name: '7:30pm', hour: 19, min: 30},
				{name: '8:00pm', hour: 20, min: 0},
				{name: '8:30pm', hour: 20, min: 30},
				{name: '9:00pm', hour: 21, min: 0},
				{name: '9:30pm', hour: 21, min: 30},
				{name: '10:00pm', hour: 22, min: 0},
				{name: '10:30pm', hour: 22, min: 30},
				{name: '11:00pm', hour: 23, min: 0},
				{name: '11:30pm', hour: 23, min: 30},
			];

			authenticationService.getATutorProfile($scope.tutorUsername).then(function (response) {
				$scope.tutor.profile = response.data.person;
			})

			authenticationService.getTutorSchedule($scope.tutorUsername).then(function (response) {
				$scope.tutor.schedule = response.data;
			})

			authenticationService.getTutorSubjects($scope.tutorUsername).then(function (response) {
				$scope.tutor.subjects = response.data;
				console.log(response.data);
			})

			$scope.register = function () {
				$scope.reg.date.setHours($scope.reg.time.hour, $scope.reg.time.min);
				var data = {
					username: $scope.tutorUsername,
					subjectId: $scope.reg.subject.id,
					date: $scope.reg.date

				}
				authenticationService.registerSession(data).then(function (response) {
					console.log(response.data);
					alert("successfully registered for a session");
					$scope.reg = {};
				})
			}
		}
	},
	template: template
}