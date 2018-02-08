import "./auth.scss";
import template from "./auth.view.pug";


export default {
	controller: function ($scope, authenticationService, $state) {
		"ngInject";

		$scope.login = {
			user: '',
			password: ''
		};

		$scope.signup = {
			user: '',
			email: '',
			confirmEmail: '',
			pass: '',
			confirmPass: ''
		};

		$scope.error = {
			email: '',
			password: ''
		};

		$scope.loginUser = function () {
			let userData = {
				username: $scope.login.user,
				password: $scope.login.password
			};
			login(userData);
		};

		function login(userData) {
			authenticationService.authenticateUser(userData).then(function (response) {
				localLogin(response);
			}, function (err) {
				$scope.login = {};
				alert(err.data.message);
			});
		}

		function localLogin(response) {
			authenticationService.setUserToken(response, false);
			authenticationService.setUserInfo(response.data);
			if (authenticationService.getUserInfo().isSetup) {
				$state.go('app.profileSetup.student');
			}
			else {
				$state.go('app.account.profile.list');
				console.log("go to profileList");
			}
		}

		$scope.emailMsg = '';
		$scope.passMsg = '';
		$scope.signUp = function () {
			if ($scope.signup.email === $scope.signup.confirmEmail && $scope.signup.pass === $scope.signup.confirmPass) {
				let userData = {
					username: $scope.signup.user,
					email: $scope.signup.email,
					password: $scope.signup.pass
				};

				authenticationService.signUp(userData).then(function (response) {
					alert("You signed up successfully");
					localLogin(response);
					$scope.signup = {};
				}, function (response) {
					if (response.status === 400) {
						// bad signup
						alert(response.data.message)
					}
					else if (response.status >= 500) {
						alert("Server error! Try again later.")
					}
					else {
						alert("Signup failed for an unknown reason. Contact an admin.")
						console.log("error " + response);
					}
				})
			}
			else {
				if ($scope.signup.email !== $scope.signup.confirmEmail) {
					$scope.emailMsg = "email";
					$scope.signup.confirmEmail = '';
				}
				if ($scope.signup.pass !== $scope.signup.confirmPass) {
					$scope.passMsg = "password";
					$scope.signup.confirmPass = '';
				}
				alert("The confirm fields for the following do not match: " + $scope.emailMsg + " " + $scope.passMsg);
				$scope.emailMsg = '';
				$scope.passMsg = '';
			}
		};
	},
	template: template
};
