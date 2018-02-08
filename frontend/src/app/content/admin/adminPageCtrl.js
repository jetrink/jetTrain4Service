import template from "./adminpage.html"

export default {
	controller: function ($scope, authenticationService, $window) {
		"ngInject";

		// initialize subs
		$scope.sub = {};
		$scope.editSub = {};

		$scope.logout = function () {
			authenticationService.logoutAdmin();
		}

		authenticationService.getAdminTutors().then(function (response) {
			$scope.tutors = response.data;
		})

		$scope.approve = function (id) {
			authenticationService.adminApprove(id).then(function () {
				$window.location.reload();
				$scope.function = "tutor";
			}, function () {
				alert("Failed to approve")
			})
		}

		$scope.deny = function (id) {
			authenticationService.adminDeny(id).then(function () {
				$window.location.reload();
				$scope.function = "tutor";
			}, function () {
				alert("Failed to deny")
			})
		}
		authenticationService.getAdmins().then(function (response) {
			$scope.admins = response.data;
		})

		$scope.delete = function (id) {
			authenticationService.deleteAdmin(id).then(function (response) {
				alert("successfully deleted admin");
				$window.location.reload();
				$scope.function = "admin";
			}, function (err) {
				alert("failed to delete admin");
			})
		}

		$scope.create = function () {
			authenticationService.createAdmin($scope.admin).then(function (response) {
				alert("successfully created admin");
				$window.location.reload();
				$scope.function = "admin";
			}, function () {
				alert("failed to create admin");
			})
		}

		$scope.createSubject = function () {
			authenticationService.createSubject($scope.sub).then(function (response) {
				alert("successfully created subject");
				$window.location.reload();
				$scope.sub = {};
				$scope.function = "subject";
			}, function () {
				alert("failed to create subject");
			})
		}

		authenticationService.getAdminSubjects().then(function (response) {
			$scope.subjects = response.data;
		})

		$scope.toEditSubjectSelected = function(selected)
		{
			$scope.editSub.field = selected.field;
			$scope.editSub.name = selected.name;
			$scope.editSub.description = selected.description;
		};

		$scope.editSubject = function (id) {
			authenticationService.editSubject($scope.editSub, id).then(function () {
				alert("successfully edited subject");
				$window.location.reload();
				$scope.editSub = {};
				$scope.selectedSub = "";
				$scope.function = "subject";
			}, function () {

				alert("failed to create subject");
			})
		}
		$scope.deleteSub = function (s) {
			var data = {
				"field": s.field,
				"name": s.name,
				"description": s.description
			}
			authenticationService.deleteSubject(s.id, data).then(function () {
				alert("successfully deleted subject");
				$window.location.reload();
				$scope.selectedSub = "";
				$scope.function = "subject";
			}, function () {

				alert("failed to delete subject");
			})
		}
	},
	template: template
}